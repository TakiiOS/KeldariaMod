/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;
import java.util.List;

public class Remedy implements INBTSerializable<NBTTagCompound>
{

    private String name;
    private List<PotionEffect> potionEffects = Lists.newArrayList();
    private String message;
    private String color;

    public Remedy(String name, List<PotionEffect> potionEffects, String message, String color)
    {
        this.name = name;
        this.potionEffects = potionEffects;
        this.message = message;
        this.color = color;
    }

    public Remedy(NBTTagCompound compound)
    {
        this.deserializeNBT(compound);
    }

    public String getName()
    {
        return name;
    }

    public List<PotionEffect> getPotionEffects()
    {
        return potionEffects;
    }

    public String getMessage()
    {
        return message.replace("&", "§");
    }

    public String getColor()
    {
        return color;
    }

    public int getRGB()
    {
        int color = 0;
        try
        {
            color = Color.decode(this.color.startsWith("#") ? this.color : "#" + this.color).getRGB();
        } catch(Exception ignored)
        {
            color = 0;
        }
        return color;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Name", this.name);
        NBTTagList list = new NBTTagList();
        this.potionEffects.forEach(potionEffect -> list.appendTag(PotionEffectSerializer.getCompound(potionEffect)));
        compound.setTag("Effects", list);
        compound.setString("Message", this.message);
        compound.setString("Color", this.color);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound)
    {
        this.name = compound.getString("Name");
        this.potionEffects = Lists.newArrayList();
        NBTTagList list = compound.getTagList("Effects", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            if(nbtBase instanceof NBTTagCompound)
            {
                this.potionEffects.add(PotionEffectSerializer.getEffect((NBTTagCompound) nbtBase));
            }
        });
        this.message = compound.getString("Message");
        this.color = compound.getString("Color");
    }

    public boolean isAllValid()
    {
        return this.name != null && this.color != null && this.message != null && this.potionEffects != null;
    }
}
