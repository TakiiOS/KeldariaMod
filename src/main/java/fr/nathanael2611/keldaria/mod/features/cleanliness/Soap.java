/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.cleanliness;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;

public class Soap implements INBTSerializable<NBTTagCompound>
{

    private String soapName;
    private String smellDescription;
    private double cleanlinessValue;
    private String color;

    private Soap(String soapName, String smellDescription, double cleanlinessValue, String color)
    {
        this.soapName = soapName;
        this.smellDescription = smellDescription;
        this.cleanlinessValue = cleanlinessValue;
        this.color = color;
    }

    public static Soap create(String soapName, String smellDescription, double cleanlinessValue, String color)
    {
        return new Soap(soapName, smellDescription, cleanlinessValue, color);
    }

    public static Soap create(NBTTagCompound compound)
    {
        if(compound == null) compound = new NBTTagCompound();
        Soap soap = new Soap("", "", 0, "#fff");
        soap.deserializeNBT(compound);
        return soap;
    }


    public String getSoapName()
    {
        return soapName;
    }

    public String getSmellDescription()
    {
        return smellDescription;
    }

    public double getCleanlinessValue()
    {
        return cleanlinessValue;
    }

    public void applyEffects(EntityPlayer player)
    {
        Helpers.sendInRadius(player.getPosition(), new TextComponentString(this.smellDescription.replace("&", "§")), 20);
        CleanilessManager.incrementCleaniless(player, this.cleanlinessValue);
    }

    public String getColor()
    {
        return color;
    }

    public int getRGB()
    {
        return Color.decode(this.color.startsWith("#") ? this.color : "#" + this.color).getRGB();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Name", this.soapName);
        compound.setString("Description", this.smellDescription);
        compound.setDouble("Value", this.cleanlinessValue);
        compound.setString("Color", this.color);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.soapName = nbt.hasKey("Name") ? nbt.getString("Name") : "Invalid";
        this.smellDescription = nbt.hasKey("Description") ? nbt.getString("Description") : "Invalid";
        this.cleanlinessValue = nbt.hasKey("Value") ? nbt.getDouble("Value") : 0;
        this.color = nbt.hasKey("Color") ? nbt.getString("Color") : "#fff";
    }
}
