package fr.nathanael2611.keldaria.mod.server.zone.landscapes;

import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import net.minecraft.world.World;

public abstract class RPZone
{

    private String name;
    private String mapSymbol;

    public RPZone(String name, String mapSymbol)
    {
        this.name = name;
        this.mapSymbol = mapSymbol;
    }

    public String getName()
    {
        return name;
    }

    public String getMapSymbol()
    {
        return mapSymbol;
    }

    public abstract Atmosphere getAtmosphere(World world);
}
