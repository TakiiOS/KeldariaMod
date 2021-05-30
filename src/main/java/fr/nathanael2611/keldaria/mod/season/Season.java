/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.season;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketReloadChunks;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;

public enum Season
{

    SPRING("spring", 0x6F818F, 0x5F849F),
    SUMMER("summer", 0xFFFFFF, 0xFFFFFF),
    AUTUMN("autumn", 0x9F5F5F, 0xEF2121),
    WINTER("winter", 0xAF4F4F, 0.45F, 0xDB3030, 0.45F);

    private String name;

    private int grassColor;
    private float grassColorMultiplier;
    private int foliageColor;
    private float foliageColorMultiplier;

    Season(String name, int grassColor, int foliageColor)
    {
        this(name, grassColor, -1, foliageColor, -1);
    }

    Season(String name, int grassColor, float grassColorMultiplier, int foliageColor, float foliageColorMultiplier)
    {
        this.name = name;
        this.grassColor = grassColor;
        this.grassColorMultiplier = grassColorMultiplier;
        this.foliageColor = foliageColor;
        this.foliageColorMultiplier = foliageColorMultiplier;
    }

    public String getName()
    {
        return name;
    }

    public int getGrassColor()
    {
        return grassColor;
    }

    public int getFoliageColor()
    {
        return foliageColor;
    }

    public float getGrassColorMultiplier()
    {
        return grassColorMultiplier;
    }

    public float getFoliageColorMultiplier()
    {
        return foliageColorMultiplier;
    }

    public static Season getSeason(World world)
    {
        DatabaseReadOnly db = world.isRemote ? ClientDatabases.getDatabase("roleplayinfos") : Databases.getDatabase("roleplayinfos");
        String seasonStr = db.isString("season") ? db.getString("season") : SPRING.name;
        for (Season value : values())
        {
            if(value.name.equalsIgnoreCase(seasonStr)) return value;
        }
        return SPRING;
    }

    public static void setSeason(World world, Season season)
    {
        if(world.isRemote) return;
        Database db = Databases.getDatabase("roleplayinfos");
        if(db.isString("season") && db.getString("season").equalsIgnoreCase(season.name))
        {
            return;
        }
        db.setString("season", season.name);
        CommonProxy.executeAfter(() -> KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketReloadChunks()), 1000);
    }

    public static void registerEvents(Keldaria keldaria)
    {
        MinecraftForge.EVENT_BUS.register(new SeasonHandler(keldaria));
    }

    public static boolean isWinter(World world)
    {
        return getSeason(world) == WINTER;
    }

    public static boolean canSnowIn(Biome biome)
    {
        if(biome.getDefaultTemperature() > 1.1)
            return false;
        return true;
    }


    public static Season byName(String name)
    {
        for (Season value : Season.values())
        {
            if(value.name.equalsIgnoreCase(name))
            {
                return value;
            }
        }
        return null;
    }


}
