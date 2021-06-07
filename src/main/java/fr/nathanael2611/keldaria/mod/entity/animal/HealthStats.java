package fr.nathanael2611.keldaria.mod.entity.animal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class HealthStats implements INBTSerializable<NBTTagCompound>
{

    private boolean tamed = false;
    private long lastFeed = System.currentTimeMillis();
    private long lastDrink = System.currentTimeMillis();


    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("LastFeed", lastFeed);
        compound.setLong("LastDrink", lastDrink);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("LastFeed"))
        {
            this.lastFeed = nbt.getLong("LastFeed");
        }
        if(nbt.hasKey("LastDrink"))
        {
            this.lastDrink = nbt.getLong("LastDrink");
        }
    }
}
