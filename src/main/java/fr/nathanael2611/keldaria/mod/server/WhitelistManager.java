/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;

import java.util.List;

public class WhitelistManager
{


    public static boolean canJoin(String playerName)
    {
        return isOnlyStaffMode() ? isStaff(playerName) : isWhitelisted(playerName) || isStaff(playerName);
    }

    public static boolean isWhitelisted(String playerName)
    {
        return getWLDatabase().getInteger(playerName) == 1;
    }

    public static boolean isOnlyStaffMode()
    {
        return getWLDatabase().getInteger("IsOnlyStaffMode") == 1;
    }

    public static void switchStaffMode()
    {
        getWLDatabase().setInteger("IsOnlyStaffMode", isOnlyStaffMode() ? 0 : 1);
    }

    public static boolean isStaff(String playerName)
    {
        return Databases.getPlayerData(playerName).getInteger("IsStaff") == 1;
    }

    public static Database getWLDatabase()
    {
        return Databases.getDatabase(Keldaria.MOD_ID + ":whitelist");
    }

    public static List<String> getWhitelistedNames()
    {
        Database db = getWLDatabase();
        List<String> names = Lists.newArrayList();
        for (String entry : db.getAllEntryNames())
        {
            if(db.isInteger(entry) && !entry.equalsIgnoreCase("IsOnlyStaffMode"))
            {
                names.add(entry);
            }
        }
        return names;
    }

    public static void whitelist(String player)
    {
        getWLDatabase().setInteger(player, 1);
    }

    public static void unWhitelist(String player)
    {
        getWLDatabase().remove(player);
    }

}
