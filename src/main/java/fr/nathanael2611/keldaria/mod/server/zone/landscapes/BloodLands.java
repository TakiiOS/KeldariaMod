/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.zone.landscapes;

import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import net.minecraft.world.World;

public class BloodLands extends RPZone
{
    public BloodLands()
    {
        super("BloodLands", "§5B");
    }

    @Override
    public Atmosphere getAtmosphere(World world)
    {
        return new Atmosphere(new Atmosphere.RGB(-1, -1, -1), new Atmosphere.RGB(-1, -1, -1), -1);
    }
}
