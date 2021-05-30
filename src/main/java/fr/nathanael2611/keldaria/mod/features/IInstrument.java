/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import net.minecraft.util.SoundEvent;

public interface IInstrument
{

    String getName();

    SoundEvent[] getSounds();

    SoundEvent[] getSneakSounds();

}
