/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.nathanael2611.keldaria.mod.asm.IEntityTrackerAccessor;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.Set;

public class CommandVanish extends KeldariaCommand
{

    public static final HashMap<String, Set<String>> HIDED_PLAYERS = Maps.newHashMap();

    public CommandVanish()
    {
        super("vanish", "/vanish", createAliases("v"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        Database data = Databases.getPlayerData(player);
        data.setInteger("Vanished", data.getInteger("Vanished") == 1 ? 0 : 1);
        boolean vanish = data.getInteger("Vanished") == 1;
        for (EntityPlayerMP p : user.getServer().getPlayerList().getPlayers())
        {
            if (vanish)
            {
                hidePlayerFor(p, player);
            } else
            {
                showPlayer(p, player);
            }
        }
        user.sendMessage("Vanish: " + vanish);
    }


    public static void hidePlayerFor(EntityPlayerMP viewer, EntityPlayerMP hidedPlayer)
    {
        //Validate.notNull(player, "hidden player cannot be null");
        if (viewer.connection == null) return;
        if (viewer.equals(hidedPlayer)) return;
        HIDED_PLAYERS.putIfAbsent(viewer.getName(), Sets.newHashSet());
        if (HIDED_PLAYERS.get(viewer.getName()).contains(hidedPlayer.getName())) return;
        HIDED_PLAYERS.get(viewer.getName()).add(hidedPlayer.getName());

        //remove this player from the hidden player's EntityTrackerEntry
        EntityTracker tracker = ((WorldServer) viewer.world).getEntityTracker();
        IntHashMap<EntityTrackerEntry> entries = ((IEntityTrackerAccessor) tracker).getTrackingEntries();

        EntityTrackerEntry entry = entries.lookup(hidedPlayer.getEntityId());
        if (entry != null)
        {
            entry.removeTrackedPlayerSymmetric(viewer);
        }

        //remove the hidden player from this player user list
        //getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, other));
        viewer.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, hidedPlayer));
    }


    public static void showPlayer(EntityPlayerMP viewer, EntityPlayerMP hidedPlayer)
    {
        //Validate.notNull(player, "shown player cannot be null");
        if (viewer.connection == null) return;
        if (viewer.equals(hidedPlayer)) return;
        HIDED_PLAYERS.putIfAbsent(viewer.getName(), Sets.newHashSet());
        if (!HIDED_PLAYERS.get(viewer.getName()).contains(hidedPlayer.getName())) return;
        HIDED_PLAYERS.get(viewer.getName()).remove(hidedPlayer.getName());

        EntityTracker tracker = ((WorldServer) viewer.world).getEntityTracker();
        EntityPlayer other = hidedPlayer;

        //viewer.connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, other));
        viewer.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, hidedPlayer));

        IntHashMap<EntityTrackerEntry> entries = ((IEntityTrackerAccessor) tracker).getTrackingEntries();
        EntityTrackerEntry entry = entries.lookup(other.getEntityId());
        if (entry != null && !entry.trackingPlayers.contains(viewer))
        {
            entry.updatePlayerEntity(viewer);
        }
    }


}
