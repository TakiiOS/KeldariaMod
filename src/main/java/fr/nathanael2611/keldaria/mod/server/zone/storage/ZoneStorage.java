/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.zone.storage;

import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class ZoneStorage implements Capability.IStorage<IZoneManager>
{

    @CapabilityInject(IZoneManager.class)
    public static final Capability<IZoneManager> CAPABILITY_ZONE = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IZoneManager> capability, IZoneManager snowManager, EnumFacing side)
    {
        return new NBTTagString(snowManager.getZoneName());
    }

    @Override
    public void readNBT(Capability<IZoneManager> capability, IZoneManager snowManager, EnumFacing side, NBTBase nbt)
    {
        if(nbt instanceof NBTTagString)
        {
            snowManager.setZone(Zones.byName(((NBTTagString) nbt).getString()));
        }
    }
}