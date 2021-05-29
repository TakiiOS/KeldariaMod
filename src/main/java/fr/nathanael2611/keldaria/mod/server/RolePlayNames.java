package fr.nathanael2611.keldaria.mod.server;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

public class RolePlayNames
{

    private static final String VALUE_NAME = Keldaria.MOD_ID + ":roleplayname";

    public static String getName(EntityPlayer player)
    {
        DatabaseReadOnly readOnly = player.world.isRemote ? ClientDatabases.getPersonalPlayerData() : Databases.getPlayerData(player);
        return readOnly.isString(VALUE_NAME) ? readOnly.getString(VALUE_NAME) : player.getName();
    }

    public static void setName(EntityPlayer player, String name)
    {
        if(player.world.isRemote) return;
        Databases.getPlayerData(player).setString(VALUE_NAME, name);
    }

}
