/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.containment;

import fr.nathanael2611.keldaria.mod.command.CommandDebugMode;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Containment
{

    public static boolean isSupervised(EntityPlayer player)
    {
        return player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getInteger("Supervised") == 1 : Databases.getPlayerData(player).getInteger("Supervised") == 1;
    }

    public static void supervise(EntityPlayer player, boolean bool)
    {

        Databases.getPlayerData(player).setInteger("Supervised", bool ? 1 : 0);
    }

    public static boolean isActive()
    {
        return Databases.getDatabase("roleplayinfos").getInteger("IsContainmentActive") == 1;
    }

    public static void setActive(boolean bool)
    {
        Databases.getDatabase("roleplayinfos").setInteger("IsContainmentActive", bool ? 1 : 0);
    }

    public static boolean canMoveOn(EntityPlayer player, Chunk chunk)
    {
        return !isActive()
                || (GroupManager.executePermissionCheck("containment.bypass", player)
                    && !CommandDebugMode.isInDebugMode(player.getName()))
                || isSupervised(player)
                || Regions.canMoveOn(player, chunk)
                || Points.canMoveOn(player, chunk);
    }

}
