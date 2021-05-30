/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Wine implements INBTSerializable<NBTTagCompound>
{

    private static final int APOGEE = 14 * 4;
    private static final int APOGEE_DURATION = 14 * 2;

    private int baseQuality;
    private int dateCreated;

    public Wine(int baseQuality, int dateCreated)
    {
        this.baseQuality = baseQuality;
        this.dateCreated = dateCreated;
    }

    public Wine(NBTTagCompound compound)
    {
        deserializeNBT(compound);
    }

    public int getDaysSinceCreation()
    {
        int days = KeldariaDate.lastDate.getTotalDaysInRP();
        return days - dateCreated;
    }

    private boolean isInApogee()
    {
        int days = getDaysSinceCreation();
        return days >= APOGEE && days <= APOGEE + APOGEE_DURATION;
    }

    public boolean isMaturating()
    {
        int days = getDaysSinceCreation();
        return days < APOGEE;
    }

    public boolean isDeclining()
    {
        return getDaysSinceCreation() > APOGEE;
    }

    public int getMaturatingPercent()
    {
        return Math.min(Helpers.getPercent(getDaysSinceCreation(), APOGEE), 100);
    }

    public int getDecliningPercent()
    {
        return Math.min(Math.max(0, Helpers.getPercent(getDaysSinceCreation() - APOGEE - APOGEE_DURATION, APOGEE + APOGEE_DURATION + APOGEE)), 100);
    }

    public int getQuality()
    {
        if(isInApogee()) return 100;
        if(isMaturating())
        {
            return this.baseQuality + (Helpers.crossMult(getMaturatingPercent(), 100, 100 - baseQuality));
        }
        else if(isDeclining())
        {
            return 100 - getDecliningPercent();
        }
        return 0;
    }

    public String getQualityString()
    {
        int quality = getQuality();
        if(quality < 20)
        {
            return "§4Misérable";
        }
        else if(quality < 30)
        {
            return "§cMédiocre";
        }
        else if(quality < 40)
        {
            return "§eMoyenne";
        }
        else if(quality < 50)
        {
            return "§6Normale";
        }
        else if(quality < 60)
        {
            return "§aBonne";
        }
        else if(quality < 80)
        {
            return "§2Très bonne";
        }
        else if(quality < 100)
        {
            return "§2Excellente";
        }
        else
        {
            return "§e ☆ Parfaite";
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("BaseQuality", baseQuality);
        compound.setInteger("DateCreated", dateCreated);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.baseQuality = nbt.getInteger("BaseQuality");
        this.dateCreated = nbt.getInteger("DateCreated");
    }

    public boolean isValid()
    {
        return this.dateCreated != 0;
    }

    public static Wine getWine(ItemStack stack)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        Wine wine = new Wine(compound.hasKey("Wine", Constants.NBT.TAG_COMPOUND) ? compound.getCompoundTag("Wine") : new NBTTagCompound());
        return wine;
    }

    public static void setWine(ItemStack stack, Wine wine)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        compound.setTag("Wine", wine.serializeNBT());
        stack.setTagCompound(compound);
    }

    public int getBaseQuality()
    {
        return baseQuality;
    }
}
