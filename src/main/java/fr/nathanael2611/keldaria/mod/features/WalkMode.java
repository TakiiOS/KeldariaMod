package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class WalkMode
{

    public static final String KEY = Keldaria.MOD_ID + ":walk_mode";

    public static void toggle(EntityPlayer player)
    {
        Database data = Databases.getPlayerData(player);
        data.setInteger(KEY, data.getInteger(KEY) == 1 ? 0 : 1);
        Helpers.sendPopMessage((EntityPlayerMP) player, "§cKeldaria: §6Mode marche: " + (data.getInteger(KEY) == 1), 4000);
    }

    public static boolean isPlayerIsWalkingMode(EntityPlayer player)
    {
        return Databases.getPlayerData(player).getInteger(KEY) == 1;
    }

    public static boolean isWalkingOn()
    {
        return ClientDatabases.getPersonalPlayerData().getInteger(KEY) == 1;
    }

}
