/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class ClientSkinManager
{

    private static final HashMap<String, String> PLAYERS_SKINS = Maps.newHashMap();

    public static String getSkinURL(EntityPlayer player)
    {
        return getSkinURL(player.getName());
    }

    public static String getSkinURL(String playerName)
    {
        return PLAYERS_SKINS.getOrDefault(playerName, "default");
    }

    public static void deserialize(NBTTagCompound compound)
    {
        PLAYERS_SKINS.clear();
        for (String s : compound.getKeySet())
        {
            PLAYERS_SKINS.put(s, compound.getString(s));
        }
    }

    public static void update(String player, String skin)
    {
        PLAYERS_SKINS.put(player, skin);
    }

}
