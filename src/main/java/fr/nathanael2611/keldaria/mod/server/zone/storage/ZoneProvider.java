package fr.nathanael2611.keldaria.mod.server.zone.storage;

import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowStorage;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;
import fr.nathanael2611.keldaria.mod.server.zone.storage.impl.ZoneManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class ZoneProvider implements ICapabilitySerializable<NBTBase>
{

    private IZoneManager zoneManager;

    public ZoneProvider(IZoneManager manager)
    {
        this.zoneManager = manager;
    }

    public ZoneProvider()
    {
        this.zoneManager = new ZoneManager();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == ZoneStorage.CAPABILITY_ZONE;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return this.hasCapability(capability, facing) ? ZoneStorage.CAPABILITY_ZONE.cast(this.zoneManager) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return ZoneStorage.CAPABILITY_ZONE.writeNBT(this.zoneManager, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        ZoneStorage.CAPABILITY_ZONE.readNBT(this.zoneManager, null, nbt);
    }
}
