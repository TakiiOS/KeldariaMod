/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.containment;

import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.world.World;

public abstract class ContainmentSystem
{

    public static Database getDB(String dbKey)
    {
        return Databases.getDatabase("Containment:" + dbKey);
    }

    public static DatabaseReadOnly getReadDB(World world, String dbKey)
    {
        return world.isRemote ? ClientDatabases.getDatabase("Containment:" + dbKey) : Databases.getDatabase("Containment:" + dbKey);
    }

}
