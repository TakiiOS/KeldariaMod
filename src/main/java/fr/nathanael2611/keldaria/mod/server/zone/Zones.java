package fr.nathanael2611.keldaria.mod.server.zone;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.Everywhere;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.server.zone.storage.ZoneStorage;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.Set;

public class Zones
{

    public static final HashMap<String, RPZone> ZONES = Maps.newHashMap();

    public static RPZone EVERYWHERE = new Everywhere();

    public static RPZone TEST = new RPZone("Test", "§cU")
    {
        private final Atmosphere TESTATMOSPHERE = new Atmosphere(new Atmosphere.RGB(1, 0, 0), new Atmosphere.RGB(1, 0, 0), 3);

        Atmosphere.RGB rgb = new Atmosphere.RGB(1, 1, 1);
        long lastUpdated = -1;

        @Override
        public Atmosphere getAtmosphere(World world)
        {
            if(lastUpdated == -1 || System.currentTimeMillis() - lastUpdated > 4000)
            {
                rgb = new Atmosphere.RGB((float) Helpers.randomDouble(0, 1), (float) Helpers.randomDouble(0, 1), (float) Helpers.randomDouble(0, 1), true);
                lastUpdated = System.currentTimeMillis();
            }

            return new Atmosphere(rgb, rgb, 0.02);
        }
    };

    public static void registerAll()
    {
        registerZone(EVERYWHERE);
        registerZone(TEST);
    }

    public static void registerZone(RPZone zone)
    {
        ZONES.put(zone.getName(), zone);
    }

    public static boolean isZoneExist(String zoneName)
    {
        return ZONES.containsKey(zoneName);
    }

    public static RPZone byName(String name)
    {
        return ZONES.getOrDefault(name, EVERYWHERE);
    }

    public static RPZone getZone(Chunk chunk)
    {
        IZoneManager manager = chunk.getCapability(ZoneStorage.CAPABILITY_ZONE, null);
        if(manager != null)
        {
            return manager.getZone();
        }
        return EVERYWHERE;
    }

    public static RPZone getZone(Entity entity)
    {
        return getZone(entity.world.getChunk(entity.getPosition()));
    }

    public static void setZone(Chunk chunk, RPZone zone)
    {
        IZoneManager manager = chunk.getCapability(ZoneStorage.CAPABILITY_ZONE, null);
        if(manager != null)
            manager.setZone(zone);
    }

    public static void setZoneInRadius(Chunk center, RPZone zone, int radius)
    {
        ChunkPos pos = center.getPos();
        for (int x = pos.x - radius; x <= pos.x + radius; x++)
        {
            for (int z = pos.z - radius; z <= pos.z + radius; z++)
            {
                Chunk c = center.getWorld().getChunk(x, z);
                if (c != null && c.isLoaded())
                {
                    setZone(c, zone);
                }
            }
        }
    }

    public static Set<String> getZonesNames()
    {
        return ZONES.keySet();
    }
}
