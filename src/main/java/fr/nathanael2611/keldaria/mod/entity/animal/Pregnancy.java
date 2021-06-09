package fr.nathanael2611.keldaria.mod.entity.animal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class Pregnancy implements INBTSerializable<NBTTagCompound>
{

    public static final Pregnancy NOT_PREGNANT = new Pregnancy(-1, -1, null);

    private long fertilizationDate;
    private long childDevelopment;
    private NBTTagCompound babyTag;

    public Pregnancy(long fertilizationDate, long childDevelopment, EntityKeldAnimal baby)
    {
        this.fertilizationDate = fertilizationDate;
        this.childDevelopment = childDevelopment;
        if(baby != null)
        {
            this.babyTag = new NBTTagCompound();
            baby.writeToNBTAtomically(this.babyTag);
        }
    }

    public Pregnancy(NBTTagCompound compound)
    {
        this.deserializeNBT(compound);
    }

    public boolean isPregnant()
    {
        return this.babyTag != null && this.getFertilizationDate() != -1 && this.getChildDevelopment() != -1;
    }

    public boolean shouldGiveBirth()
    {
        return System.currentTimeMillis() - this.getFertilizationDate() > this.getChildDevelopment();
    }

    public long getFertilizationDate()
    {
        return fertilizationDate;
    }

    public long getChildDevelopment()
    {
        return childDevelopment;
    }

    public EntityKeldAnimal createBaby(World world)
    {
        Entity entity = EntityList.createEntityFromNBT(this.babyTag, world);
        if (entity instanceof EntityKeldAnimal)
        {
            return (EntityKeldAnimal) entity;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("FertilizationDate", this.fertilizationDate);
        compound.setLong("ChildDevelopment", this.childDevelopment);
        if(babyTag != null)
        {
            compound.setTag("BabyTag", this.babyTag);
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.fertilizationDate = nbt.getLong("FertilizationDate");
        this.childDevelopment = nbt.getLong("ChildDevelopment");
        this.babyTag = nbt.getCompoundTag("BabyTag");

    }
}
