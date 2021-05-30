/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.api.IHasClothe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemClothe extends Item
{

    public ItemClothe()
    {

        this.setMaxStackSize(1);
    }

    public static boolean isClothValid(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound compound = stack.getTagCompound();
            return compound.hasKey("ClothUrl", 8);
        }
        return stack.getItem() instanceof IHasClothe;
    }

    public static String getClothURL(ItemStack stack)
    {
        return isClothValid(stack) && stack.getTagCompound() != null ? stack.getTagCompound().getString("ClothUrl") : stack.getItem() instanceof IHasClothe ? ((IHasClothe) stack.getItem()).getDefaultClothURL() : "";
    }

    public static void setClothURL(ItemStack stack, String url)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) compound = new NBTTagCompound();
        compound.setString("ClothUrl", url);
        stack.setTagCompound(compound);
    }

}
