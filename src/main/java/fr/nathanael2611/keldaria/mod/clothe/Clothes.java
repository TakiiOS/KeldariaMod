/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.clothe;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.image.BufferedImage;
import java.util.List;

public class Clothes implements INBTSerializable<NBTTagCompound>
{

    private String playerName;
    private List<String> clotheUrls;
    private boolean[] armorOverrides;

    public Clothes(String playerName, List<String> clotheUrls, boolean[] armorOverrides)
    {
        this.playerName = playerName;
        this.clotheUrls = clotheUrls;
        this.armorOverrides = armorOverrides;
    }

    public Clothes(NBTTagCompound compound)
    {
        this.deserializeNBT(compound);
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public List<String> getClotheUrls()
    {
        return clotheUrls;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("PlayerName", this.playerName);
        NBTTagList list = new NBTTagList();
        this.clotheUrls.forEach(url -> list.appendTag(new NBTTagString(url)));
        compound.setTag("Clothes", list);
        compound.setBoolean("armorOverrideHelmet", this.armorOverrides[0]);
        compound.setBoolean("armorOverrideChest", this.armorOverrides[1]);
        compound.setBoolean("armorOverrideLegs", this.armorOverrides[2]);
        compound.setBoolean("armorOverrideBoots", this.armorOverrides[3]);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.playerName = nbt.getString("PlayerName");
        this.clotheUrls = Lists.newArrayList();
        NBTTagList list = nbt.getTagList("Clothes", Constants.NBT.TAG_STRING);
        list.forEach(string -> this.clotheUrls.add(((NBTTagString) string).getString()));
        this.armorOverrides = new boolean[] {nbt.getBoolean("armorOverrideHelmet"), nbt.getBoolean("armorOverrideChest"), nbt.getBoolean("armorOverrideLegs"), nbt.getBoolean("armorOverrideBoots")};

    }

    public BufferedImage assemble()
    {
        List<BufferedImage> images = Lists.newArrayList();
        this.clotheUrls.forEach(url -> images.add(ImageCache.get(url, /*new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)*/ null)));
        BufferedImage image;
        if(this.clotheUrls.size() > 0) image = RenderHelpers.superpose(images);
        else image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        return image;
    }

    public boolean[] getArmorOverrides()
    {
        return armorOverrides;
    }
}
