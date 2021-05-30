/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.season.snow.storage;

import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class SnowStorage implements Capability.IStorage<ISnowManager>
{

    @CapabilityInject(ISnowManager.class)
    public static final Capability<ISnowManager> CAPABILITY_SNOW = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISnowManager> capability, ISnowManager snowManager, EnumFacing side)
    {
        NBTTagList compound = new NBTTagList();
        for (BlockPos diggedPose : snowManager.getDiggedPoses())
        {
            compound.appendTag(new NBTTagString(Helpers.blockPosToString(diggedPose)));
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<ISnowManager> capability, ISnowManager snowManager, EnumFacing side, NBTBase nbt)
    {
        if(nbt instanceof NBTTagList)
        {
            NBTTagList list = (NBTTagList) nbt;
            for (NBTBase nbtBase : list)
            {
                if(nbtBase instanceof NBTTagString)
                {
                    BlockPos pos = Helpers.parseBlockPosFromString(((NBTTagString) nbtBase).getString());
                    snowManager.dig(pos);
                }
            }
        }
    }
}
