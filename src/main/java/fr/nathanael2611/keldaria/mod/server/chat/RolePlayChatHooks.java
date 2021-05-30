/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.chat;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class RolePlayChatHooks
{

    public static ConcurrentLinkedDeque<Runnable> toRun = new ConcurrentLinkedDeque<>();

    public static final Thread CHAT_THREAD = new Thread(() -> {
        while(true)
        {
            if(toRun.size() > 0)
            {
                toRun.getFirst().run();
                toRun.removeFirst();
            }
        }
    });

    static {
        CHAT_THREAD.setName("chat-thread");
        CHAT_THREAD.start();
    }

    public static void send(ServerChatEvent event)
    {
        if(event.getMessage().startsWith("£"))
        {
            sendToAll(
                    new TextComponentString(
                            "§e[§6Demande§e] §6" + event.getPlayer().getName() + "§e: "+
                                    event.getMessage().substring(1)).setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sr " + event.getPlayer().getName() + " "))), player ->
                            GroupManager.executePermissionCheck("staffchat.use", player));
            event.getPlayer().sendMessage(new TextComponentString("§2Demande envoyée au staff! Vous recevrez une réponse sous peu, si des staffs sont connectés!"));
        }
        else if(event.getMessage().startsWith("{"))
        {
            if(GroupManager.executePermissionCheck("staffchat.use", event.getPlayer()))
            {
                sendToAll(new TextComponentString("§4[§cStaff§4] §6" + event.getPlayer().getName() + "§6: "+ event.getMessage().substring(1)), player -> GroupManager.executePermissionCheck("staffchat.use", player));

            }
            else
            {
                event.getPlayer().sendMessage(new TextComponentString("§cCe mode de chat est réservé au staff!"));
            }
        }
        else
        {
            Map.Entry<String, ChatType> entry = ChatType.getFor(event.getMessage());
            String usedPrefix = entry.getKey();
            ChatType type = entry.getValue();
            sendInRadius(event.getPlayer(), event.getPlayer().server, type, usedPrefix, event.getMessage());
        }
    }

    public static boolean canSpeakHRP(MinecraftServer server, World world)
    {
        if (!world.getGameRules().hasRule("maxPlayersHRP"))
        {
            world.getGameRules().setOrCreateGameRule("maxPlayersHRP", "8");
        }
        return server.getOnlinePlayerNames().length < world.getGameRules().getInt("maxPlayersHRP");
    }

    public static void sendToAll(ITextComponent component, Function<EntityPlayer, Boolean> func)
    {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            if(func.apply(player))
            {
                player.sendMessage(component);
            }
        }
    }

    private static void sendInRadius(EntityPlayer sender, MinecraftServer server, ChatType type, String prefix, String entireMessage)
    {
        ITextComponent component = type.format(sender, prefix, entireMessage).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Auteur: " + sender.getName()))));


        if(type == ChatType.HRP)
        {
            if(!canSpeakHRP(server, server.getWorld(0)))
            {
                sender.sendMessage(new TextComponentString("§cErreur: Afin d'éviter le Méta-Gaming, le chat HRP est désactivé dû au grand nombre de joueurs. Si vous avez une demande à faire au staff, £ devant votre message."));
                return;
            }

            if(Databases.getPlayerData(sender).getInteger(Keldaria.MOD_ID + ":hideGlobalChat") == 1)
            {
                sender.sendMessage(new TextComponentString("§cLe chat HRP global est désactivé pour vous... Veuillez le ré-activer pour parler dans ce canal."));
                return;
            }
        }

        List<String> playerNames = new ArrayList<>();

       {// GO GO GO
            toRun.add(() ->
           {
               long started = System.currentTimeMillis();
               int strenght = type.getDistance();
               HashMap<BlockPos, Integer> poses = Maps.newHashMap();
               propagate(sender.world, sender.getPosition().up(), poses, Maps.newHashMap(), strenght);
               //System.out.println("Tooked " + (System.currentTimeMillis() - started) + " to calculate.");

               server.getPlayerList().getPlayers().forEach(player -> {
                   if(type.getDistance() == 0)
                   {
                       if(type == ChatType.HRP || Databases.getPlayerData(player).getInteger(Keldaria.MOD_ID + ":hideGlobalChat") != 1)
                       {

                               player.sendMessage(component);
                               playerNames.add(player.getName());


                       }
                   }
                   else if(poses.containsKey(player.getPosition()))
                   {
                       ITextComponent rpComp = component.createCopy();
                       if(type.isVoice())
                       {
                           rpComp  = type.format(sender, prefix, entireMessage).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Auteur: " + sender.getName()))));
                       }
                       player.sendMessage(rpComp);
                       playerNames.add(player.getName());
                   }
               });
               //System.out.println("Tooked " + (System.currentTimeMillis() - started) + " to calculate and blocks.");
           });
        }

        if(canSpeakHRP(server, server.getWorld(0)) && type == ChatType.HRP)
        {
            if(DiscordImpl.getInstance().hasBot())
            {
                DiscordImpl.getInstance().
                        getBot().
                        sendMessageOnDiscordLinkChannel(
                                sender.getName() + ": " +
                                        entireMessage.substring(
                                                prefix.length()));
            }
        }
        else
        {
            PlayerSpy.MESSAGE_LOG.log(sender.getName() + ":\n" + Helpers.toRawUnformattedString(type.format(sender, prefix, entireMessage)) + "\n" + playerNames);
        }
    }

    public static void propagate(World world, BlockPos pos, HashMap<BlockPos, Integer> propagatedPoses, HashMap<BlockPos, IBlockState> knowBlocks, int strength)
    {
        if(world != null)
        {
            if(strength > 0)
            {
                IBlockState state = getState(world, pos, knowBlocks);
                int nS = strength - getBlockResistance(state);
                if(nS > 0)
                {
                    if(!propagatedPoses.containsKey(pos) || propagatedPoses.get(pos) < nS)
                    {
                        propagatedPoses.put(pos, nS);
                        for (EnumFacing value : EnumFacing.values())
                        {
                            propagate(world, pos.offset(value), propagatedPoses, knowBlocks, nS);
                        }
                    }
                }
            }
        }
    }


    public static IBlockState getState(World world, BlockPos pos, HashMap<BlockPos, IBlockState> knowStates)
    {
        if(!knowStates.containsKey(pos))
        {
            knowStates.put(pos, world.getBlockState(pos));
        }
        return knowStates.getOrDefault(pos, Blocks.AIR.getDefaultState());
    }

    public static int getBlockResistance(IBlockState block)
    {
        return SoundResistance.getResistance(block);
    }




}
