package fr.nathanael2611.keldaria.mod.server.zone.landscapes;

import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import net.minecraft.world.World;

public class IceLands extends RPZone
{
    public IceLands()
    {
        super("IceLands", "§bI");
    }

    @Override
    public Atmosphere getAtmosphere(World world)
    {
        return new Atmosphere(new Atmosphere.RGB(-1, -1, -1), new Atmosphere.RGB(-1, -1, -1), -1);
    }
}
