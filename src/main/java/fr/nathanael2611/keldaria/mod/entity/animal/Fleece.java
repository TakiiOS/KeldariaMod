package fr.nathanael2611.keldaria.mod.entity.animal;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Fleece implements INBTSerializable<NBTTagCompound>
{

    private boolean hasFleece;
    private EnumDyeColor color;

    public Fleece(boolean hasFleece, EnumDyeColor color)
    {
        this.hasFleece = hasFleece;
        this.color = color;
    }

    public Fleece(NBTTagCompound compound)
    {
        this.deserializeNBT(compound);
    }

    public boolean hasFleece()
    {
        return hasFleece;
    }

    public EnumDyeColor getColor()
    {
        return color;
    }

    public Fleece withFleece(boolean sheared)
    {
        this.hasFleece = sheared;
        return this;
    }

    public Fleece withColor(EnumDyeColor color)
    {
        this.color = color;
        return this;
    }

    public boolean isSheared()
    {
        return !this.hasFleece();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("Fleece", this.hasFleece);
        compound.setInteger("Color", this.color.getMetadata());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        this.hasFleece = compound.getBoolean("Fleece");
        this.color = EnumDyeColor.byMetadata(compound.getInteger("Color"));
    }
}
