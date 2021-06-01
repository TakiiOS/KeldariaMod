/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.food.capability;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Rot implements INBTSerializable<NBTTagCompound>
{

    private ItemStack stack;
    private int createdDay;
    private int saltBagDay = -1;

    public Rot(ItemStack stack)
    {
        this.stack = stack;
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        if(compound.hasKey("Rot"))
        {
            this.deserializeNBT(compound.getCompoundTag("Rot"));
        }
        else
        {
            this.createdDay = 0;
        }
    }

    public int getCreatedDay()
    {
        return this.createdDay;
    }

    public void setCreatedDay(int day)
    {
        this.createdDay = day;
        this.save();
    }

    public void save()
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        compound.setTag("Rot", this.serializeNBT());
        this.stack.setTagCompound(compound);
    }

    public int getSaltBagDay()
    {
        return this.saltBagDay;
    }

    public void setSaltBagDay(int day)
    {
        this.saltBagDay = day;
        this.save();

    }

    public boolean isInBag()
    {
        return getSaltBagDay() != -1;
    }

    public int getRotPercent(ItemStack stack)
    {
        int maxDays = ExpiredFoods.getPeremptionTime(stack);
        int days = getDays();
        return Math.min(Helpers.getPercent(days, maxDays), 100);
    }

    public int getDays()
    {
        if(isInBag())
        {
            int daysInBag = KeldariaDate.lastDate.getTotalDaysInRP() - getSaltBagDay();
            int day = KeldariaDate.lastDate.getTotalDaysInRP() - getCreatedDay();
            int noSaltBag = day - daysInBag;

            return (int) (noSaltBag + (daysInBag * 0.3));
        }
        return KeldariaDate.lastDate.getTotalDaysInRP() - getCreatedDay();
    }

    public void extractFromBag()
    {
        this.setCreatedDay(KeldariaDate.lastDate.getTotalDaysInRP() - this.getDays());
        this.setSaltBagDay(-1);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("CreatedDay", this.getCreatedDay());
        compound.setInteger("SaltBagDay", this.getSaltBagDay());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.setCreatedDay(nbt.getInteger("CreatedDay"));
        this.setSaltBagDay(nbt.getInteger("SaltBagDay"));
    }
}
