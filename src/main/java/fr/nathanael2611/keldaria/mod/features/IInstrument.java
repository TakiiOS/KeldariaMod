package fr.nathanael2611.keldaria.mod.features;

import net.minecraft.util.SoundEvent;

public interface IInstrument
{

    String getName();

    SoundEvent[] getSounds();

    SoundEvent[] getSneakSounds();

}
