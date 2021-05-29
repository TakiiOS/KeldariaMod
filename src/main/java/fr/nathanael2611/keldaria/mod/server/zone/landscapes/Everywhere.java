package fr.nathanael2611.keldaria.mod.server.zone.landscapes;

import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import net.minecraft.world.World;

public class Everywhere extends RPZone
{

    public static Atmosphere DEFAULT_ATMOSPHERE = new Atmosphere();

    public Everywhere()
    {
        super("Everywhere", "§f=");
    }

    @Override
    public Atmosphere getAtmosphere(World world)
    {
        return DEFAULT_ATMOSPHERE;
    }
}
