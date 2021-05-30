/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server;

import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;

public class OnlineModeBypasser
{

    private static Database getDb()
    {
        return Databases.getDatabase("OMB");
    }

    public static boolean doesPlayerBypass(String playerName)
    {
        return getDb().get(playerName).asInteger() == 1;
    }

    public static void addPlayer(String playerName)
    {
        getDb().setInteger(playerName, 1);
    }

    public static void removePlayer(String playerName)
    {
        getDb().setInteger(playerName, 0);
    }

}
