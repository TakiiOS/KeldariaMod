/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.fight;

import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class FightStorage implements Capability.IStorage<FightControl>
{
    @CapabilityInject(FightControl.class)
    public static final Capability<FightControl> CAPABILITY = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<FightControl> capability, FightControl instance, EnumFacing side)
    {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<FightControl> capability, FightControl instance, EnumFacing side, NBTBase nbt)
    {
        if(nbt instanceof NBTTagCompound)
        {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }
}
