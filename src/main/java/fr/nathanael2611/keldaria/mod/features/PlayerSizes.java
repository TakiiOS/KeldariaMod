package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class is used to manage the player-sizes
 */
public class PlayerSizes
{

    public static final String DB_NAME = Keldaria.MOD_ID + ":playersizes";

    public static double get(EntityPlayer player)
    {
        DatabaseReadOnly db = player.world.isRemote ? ClientDatabases.getDatabase(DB_NAME) : Databases.getDatabase(DB_NAME);
        return db.isDouble(player.getName()) ? db.getDouble(player.getName()) : 1;
    }

    public static void set(String playername, double size)
    {
        Databases.getDatabase(DB_NAME).setDouble(playername, size);
        Databases.getDatabase(DB_NAME).setDouble(playername, size);
    }
}
