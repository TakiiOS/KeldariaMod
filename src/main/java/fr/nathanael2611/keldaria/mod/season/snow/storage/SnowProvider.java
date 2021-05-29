package fr.nathanael2611.keldaria.mod.season.snow.storage;

import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.season.snow.storage.impl.SnowManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

public class SnowProvider implements ICapabilitySerializable<NBTBase>
{

    private ISnowManager snowManager;

    public SnowProvider(ISnowManager manager)
    {
        this.snowManager = manager;
    }

    public SnowProvider()
    {
        this.snowManager = new SnowManager();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == SnowStorage.CAPABILITY_SNOW;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return this.hasCapability(capability, facing) ? SnowStorage.CAPABILITY_SNOW.cast(this.snowManager) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return SnowStorage.CAPABILITY_SNOW.writeNBT(this.snowManager, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        SnowStorage.CAPABILITY_SNOW.readNBT(this.snowManager, null, nbt);
    }
}
