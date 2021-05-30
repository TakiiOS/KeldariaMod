/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public enum OpticIssue
{

    NONE(0, "none", "Rien", ""),
    MYOPIA(1, "myopia", "Myopie", "blur");

    public static final String KEY = Keldaria.MOD_ID + ":opticIssue";

    private int id;
    private String name;
    private String formattedName;
    private String shaderName;

    OpticIssue(int id, String name, String formattedName, String shaderName)
    {
        this.id = id;
        this.name = name;
        this.formattedName = formattedName;
        this.shaderName = shaderName;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public String getShaderName()
    {
        return shaderName;
    }

    public boolean has(EntityPlayer player)
    {
        return getIssue(player) == this;
    }

    public static OpticIssue getIssue(EntityPlayer player)
    {
        return OpticIssue.byId(player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getInteger(KEY) : Databases.getPlayerData(player).getInteger(KEY));
    }

    public static void setIssue(EntityPlayer player, OpticIssue issue)
    {
        Databases.getPlayerData(player).setInteger(KEY, issue.id);
    }

    public static OpticIssue byId(int id)
    {
        return Arrays.stream(values()).filter(issueType -> issueType.id == id).findFirst().orElse(NONE);
    }

    public static OpticIssue byName(String name)
    {
        return Arrays.stream(values()).filter(issueType -> issueType.name.equalsIgnoreCase(name)).findFirst().orElse(NONE);
    }


}
