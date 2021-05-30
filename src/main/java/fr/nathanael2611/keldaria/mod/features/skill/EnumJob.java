/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.skill;

import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

public enum EnumJob
{

    PEASANT("peasant", "Paysan"),
    MINER("miner", "Mineur"),
    LUMBERJACK("lumberjack", "Bûcheron"),
    ARTISAN("artisan", "Artisan"),
    BUILDER("builder", "Bâtisseur"),
    BLACKSMITH("blacksmith", "Forgeron"),
    COOK("cook", "Cuisinier"),
    APOTHECARY("apothecary", "Apothicaire"),
    COMBATANT("combatant", "Combattant"),
    DRESSMAKER("dressmaker", "Couturier");

    private String key;
    private String name;
    private String formattedName;

    EnumJob(String name, String formattedName)
    {
        this.key = "job:" + name;
        this.name = name;
        this.formattedName = formattedName;
    }

    public String getName()
    {
        return name;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public void set(EntityPlayer player, Level level)
    {
        Databases.getPlayerData(player).setInteger(this.key, level.getId());
    }

    public Level getLevel(EntityPlayer player)
    {
        return Level.byId(player.world.isRemote ? ClientDatabases.getPersonalPlayerData().getInteger(this.key) : Databases.getPlayerData(player).getInteger(this.key));
    }

    public boolean has(EntityPlayer player)
    {
        return getLevel(player).atLeast(Level.NOVICE);
    }

    public static EnumJob byName(String name)
    {
        for (EnumJob value : values())
        {
            if (value.getName().equalsIgnoreCase(name)) return value;
        }
        return null;
    }

    public enum Level
    {

        NONE(0, "none", "None"), NOVICE(1, "novice", "Novice"), MEDIUM(2, "medium", "Medium"), MASTER(3, "master", "Master");

        private int id;
        private String name;
        private String formattedName;

        Level(int id, String name, String formattedName)
        {
            this.id = id;
            this.name = name;
            this.formattedName = formattedName;
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

        public boolean atLeast(Level level)
        {
            return this.id >= level.getId();
        }

        public static Level byId(int id)
        {
            for (Level value : values())
            {
                if (value.getId() == id) return value;
            }
            return NONE;
        }


        public static Level byName(String name)
        {
            for (Level value : values())
            {
                if (value.getName().equalsIgnoreCase(name)) return value;
            }
            return null;
        }
    }

}
