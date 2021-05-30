/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.registry;

import fr.nathanael2611.keldaria.mod.Keldaria;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class KeldariaSounds
{

    public static final SoundEvent SHARPENING = new SoundEvent(new ResourceLocation(Keldaria.MOD_ID, "sharpening")).setRegistryName(Keldaria.MOD_ID + ":sharpening");
    public static final SoundEvent GITTERN = new SoundEvent(new ResourceLocation(Keldaria.MOD_ID, "gittern")).setRegistryName(Keldaria.MOD_ID + ":gittern");
    public static final SoundEvent GITTERN_SNEAK = new SoundEvent(new ResourceLocation(Keldaria.MOD_ID, "gittern_sneak")).setRegistryName(Keldaria.MOD_ID + ":gittern_sneak");
    public static final SoundEvent LYRE = new SoundEvent(new ResourceLocation(Keldaria.MOD_ID, "lyre")).setRegistryName(Keldaria.MOD_ID + ":lyre");
    public static final SoundEvent LYRE_SNEAK = new SoundEvent(new ResourceLocation(Keldaria.MOD_ID, "lyre_sneak")).setRegistryName(Keldaria.MOD_ID + ":lyre_sneak");

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> e)
    {
        e.getRegistry().register(SHARPENING);
        e.getRegistry().register(GITTERN);
        e.getRegistry().register(GITTERN_SNEAK);
    }

}
