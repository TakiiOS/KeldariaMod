/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionEffectSerializer
{

    public static PotionEffect getEffect(NBTTagCompound compound)
    {
        Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(compound.getString("RegistryName")));
        if(potion == null)
        {
            return new PotionEffect(MobEffects.LUCK, 0, 0, false, false);
        }
        return new PotionEffect(potion, compound.getInteger("Duration"), compound.getInteger("Amplifier"), false, false);
    }

    public static NBTTagCompound getCompound(PotionEffect effect)
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("RegistryName", effect.getPotion().getRegistryName().toString());
        compound.setInteger("Duration", effect.getDuration());
        compound.setInteger("Amplifier", effect.getAmplifier());
        return compound;
    }

}
