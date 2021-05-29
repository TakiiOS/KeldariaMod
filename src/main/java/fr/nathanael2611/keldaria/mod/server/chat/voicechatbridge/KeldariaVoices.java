package fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.nathanael2611.keldaria.mod.server.chat.ChatType;
import fr.nathanael2611.keldaria.mod.server.chat.RolePlayChatHooks;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeldariaVoices
{

    private static final HashMap<String, Boolean> TALKING_PLAYERS = Maps.newHashMap();

    private static final HashMap<String, HashMap<String, Integer>> HEAR_LIST = Maps.newHashMap();

    private static ConcurrentLinkedDeque<Runnable> toRun = new ConcurrentLinkedDeque<>();

    public static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(5, new ThreadFactoryBuilder().setNameFormat("voice-calculation-thread-%d").build());

    static {
        SERVICE.scheduleAtFixedRate(() ->
        {
            if(!toRun.isEmpty())
            {
                Runnable runnable = toRun.remove();
                runnable.run();
            }
        }, 50, 10, TimeUnit.MILLISECONDS);
    }

    public static ChatType getChatType(EntityPlayer player)
    {
        return ChatType.SPEAK;
    }

    public static int getSpeakStrength(EntityPlayer player)
    {
        return player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getInteger("SpeakStrength") : Databases.getPlayerData(player).getInteger("SpeakStrength");
    }

    public static void setSpeakStrength(EntityPlayer player, int strength)
    {
        Databases.getPlayerData(player).setInteger("SpeakStrength", strength);
    }

    public static void reloadHearList(EntityPlayer player)
    {
        if(hearTroughWalls(player.world))
        {
            return;
        }
        KeldariaVoices.toRun.add(() ->
        {
            int distance = getSpeakStrength(player);
            //ChatType type = getChatType(player);
            {
                HashMap<String, Integer> players = Maps.newHashMap();
                int strength = distance;
                HashMap<BlockPos, Integer> poses = Maps.newHashMap();

                RolePlayChatHooks.propagate(player.world, player.getPosition().up(), poses, Maps.newHashMap(), strength);

                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(p -> {
                    if(poses.containsKey(p.getPosition().up()))
                    {
                        players.put(p.getName(), poses.get(p.getPosition().up()));
                    }
                });
                KeldariaVoices.HEAR_LIST.put(player.getName(), players);
            }
        });
    }

    public static int getHearStrength(EntityPlayer speaker, EntityPlayer player)
    {
        return HEAR_LIST.getOrDefault(speaker.getName(), Maps.newHashMap()).getOrDefault(player.getName(), 0);
    }

    public static boolean canHear(EntityPlayer speaker, EntityPlayer player)
    {
        return getHearStrength(speaker, player) > 0;
    }

    public static byte getHearVolume(EntityPlayer speaker, EntityPlayer player)
    {
        int percent = Math.max(0, Math.min(100, Helpers.getPercent(getHearStrength(speaker, player), getChatType(player).getDistance())));
        return (byte) (percent);
    }

    public static boolean isTheoricallyTalking(EntityPlayer player)
    {
        return TALKING_PLAYERS.getOrDefault(player.getName(), false);
    }

    public static void updateTalking(EntityPlayer player, boolean talking)
    {
        TALKING_PLAYERS.put(player.getName(), talking);
    }

    public static boolean hearTroughWalls(World world)
    {
        return world.getGameRules().getBoolean("canHearTroughWalls");
    }

}
