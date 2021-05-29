package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import fr.nathanael2611.keldaria.mod.discord.impl.DiscordCommandSender;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(CommandHandler.class)
public abstract class MixinCommandHandler
{

    @Shadow @Final private Map<String, ICommand> commandMap;

    @Shadow protected abstract MinecraftServer getServer();

    @Shadow protected abstract boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input);

    @Shadow protected abstract int getUsernameIndex(ICommand command, String[] args) throws CommandException;

    @Shadow
    protected static String[] dropFirstString(String[] input) {
        return new String[0];
    }

    /**
     * @author Mojang
     */
  //  @Overwrite
    public int executeCommand(ICommandSender sender, String rawCommand)
    {
        String logMessage = sender.getName() + " try to execute command " + rawCommand;
        rawCommand = rawCommand.trim();


        if (rawCommand.startsWith("/"))
        {
            rawCommand = rawCommand.substring(1);
        }

        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = dropFirstString(astring);
        ICommand icommand = this.commandMap.get(s);
        int i = 0;

        try
        {
            int j = this.getUsernameIndex(icommand, astring);

            if (icommand == null)
            {
                String msg = TextFormatting.DARK_RED + "[Keldaria]" + TextFormatting.RED + " Commande introuvable !";
                if(sender instanceof EntityPlayer) Helpers.sendPopMessage((EntityPlayerMP) sender, msg, 3000);
                else sender.sendMessage(new TextComponentString(msg));
                if(sender instanceof EntityPlayer || sender instanceof DiscordCommandSender)
                {
                    PlayerSpy.COMMANDS_LOG.log(":x: " + logMessage + " but the command was not found.");
                }
            }
            else if (GroupManager.executePermissionCheck(icommand, sender))
            {
                if(sender instanceof EntityPlayer || sender instanceof DiscordCommandSender)
                {
                    if(
                            !(
                                    sender instanceof EntityPlayer &&
                                            MixinHooks.isCommandHide((EntityPlayer) sender, icommand)
                            )
                    )
                    {
                        PlayerSpy.COMMANDS_LOG.log(":white_check_mark: " + logMessage.replace("try to execute", "executed"));
                    }
                }
                net.minecraftforge.event.CommandEvent event = new net.minecraftforge.event.CommandEvent(icommand, sender, astring);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                {
                    if (event.getException() != null)
                    {
                        com.google.common.base.Throwables.throwIfUnchecked(event.getException());
                    }
                    return 1;
                }
                if (event.getParameters() != null) astring = event.getParameters();

                if (j > -1)
                {
                    List<Entity> list = EntitySelector.<Entity>matchEntities(sender, astring[j], Entity.class);
                    String s1 = astring[j];
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

                    if (list.isEmpty())
                    {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] {astring[j]});
                    }

                    for (Entity entity : list)
                    {
                        astring[j] = entity.getCachedUniqueIdString();

                        if (this.tryExecute(sender, astring, icommand, rawCommand))
                        {
                            ++i;
                        }
                    }

                    astring[j] = s1;
                }
                else
                {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                    if (this.tryExecute(sender, astring, icommand, rawCommand))
                    {
                        ++i;
                    }
                }
            }
            else
            {
                String msg = TextFormatting.DARK_RED + "[Keldaria Permission]" + TextFormatting.RED + " Vous n'avez pas la permission d'utiliser cette commande !";
                if(sender instanceof EntityPlayer) Helpers.sendPopMessage((EntityPlayerMP) sender, msg, 3000);
                else sender.sendMessage(new TextComponentString(msg));
                if(sender instanceof EntityPlayer || sender instanceof DiscordCommandSender)
                {
                    PlayerSpy.COMMANDS_LOG.log(":x: " + logMessage + " but " + sender.getName() + "doesn't have permission.");
                }
            }
        }
        catch (CommandException commandexception)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }

    /**
     * @author
     */
    @Overwrite
    public List<String> getTabCompletions(ICommandSender sender, String input, @Nullable BlockPos pos)
    {
        try {
            String[] astring = input.split(" ", -1);
            String s = astring[0];

            if (astring.length == 1)
            {
                List<String> list = Lists.<String>newArrayList();

                for (Map.Entry<String, ICommand> entry : this.commandMap.entrySet())
                {
                    if (CommandBase.doesStringStartWith(s, entry.getKey()) && GroupManager.executePermissionCheck(entry.getValue(), sender))
                    {
                        list.add(entry.getKey());
                    }
                }

                return list;
            }
            else
            {
                if (astring.length > 1)
                {
                    ICommand icommand = this.commandMap.get(s);

                    if (icommand != null && GroupManager.executePermissionCheck(icommand, sender))
                    {
                        return icommand.getTabCompletions(this.getServer(), sender, dropFirstString(astring), pos);
                    }
                }

                return Collections.<String>emptyList();
            }
        } catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

}
