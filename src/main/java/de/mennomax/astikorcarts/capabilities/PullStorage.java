/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PullStorage implements IStorage<IPull>
{
    @Override
    public NBTBase writeNBT(Capability<IPull> capability, IPull instance, EnumFacing side)
    {
        return null;
        
    }

    @Override
    public void readNBT(Capability<IPull> capability, IPull instance, EnumFacing side, NBTBase nbt)
    {
        
    }
}