package fr.nathanael2611.keldaria.mod.features.cleanliness;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class CleanilessManager
{

    public static final String DB_NAME = Keldaria.MOD_ID + ":cleaniless";

    public static double getCleanilessValue(EntityPlayer player)
    {
        if (player.world.isRemote)
        {
            if (player == Minecraft.getMinecraft().player)
            {
                return ClientDatabases.getDatabase(DB_NAME).getDouble(player.getName());
            }
        }
        return Databases.getDatabase(DB_NAME).getDouble(player.getName());
    }

    public static void setCleaniless(EntityPlayer player, double cleanliness)
    {
        if (player.world.isRemote) return;
        Databases.getDatabase(DB_NAME).setDouble(player.getName(), Math.max(0, Math.min(cleanliness, 100)));
    }

    public static void incrementCleaniless(EntityPlayer player, double value)
    {
        setCleaniless(player, getCleanilessValue(player) + value);
    }

    public static void decrementCleaniless(EntityPlayer player, double value)
    {
        incrementCleaniless(player, -value);
    }


    public static void updateCleaniless(EntityPlayer player)
    {
        if (player.isInWater() && getCleanilessValue(player) < 50.0)
        {
            incrementCleaniless(player, 0.5);
        } else if (player.world.isRainingAt(player.getPosition()))
        {
            incrementCleaniless(player, 0.005);
        } else
        {
            decrementCleaniless(player, 0.009);
        }
    }


}
