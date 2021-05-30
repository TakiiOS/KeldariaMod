/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;

public class SharpeningHelpers
{

    public static final String SHARPENING_KEY = "sharpening";

    public static final int MAX_SHARPENING = 2000;
    public static final int MAX_SHARPENING_SURPLUS = MAX_SHARPENING + (MAX_SHARPENING / 4);
    public static final int BASIC_SHARPENING = MAX_SHARPENING / 16;

    public static boolean canBeSharped(Item item)
    {
        return item instanceof ItemSword || item instanceof ItemAxe;
    }

    public static boolean canBeSharped(ItemStack stack)
    {
        return canBeSharped(stack.getItem());
    }

    public static NBTTagCompound getTag(ItemStack stack)
    {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static boolean hasSharpness(ItemStack stack)
    {
        return getTag(stack).hasKey(SHARPENING_KEY);
    }

    public static void setSharpness(ItemStack stack, int sharpness)
    {
        getTag(stack).setInteger(SHARPENING_KEY, sharpness > MAX_SHARPENING_SURPLUS ? MAX_SHARPENING_SURPLUS: (sharpness < 0 ? 0 : sharpness));
    }

    public static void initSharpness(ItemStack stack)
    {
        if(!hasSharpness(stack))
        {
            setSharpness(stack, BASIC_SHARPENING);
        }
    }

    public static void incrementSharpness(ItemStack stack, int amount, EntityLivingBase entity)
    {
        setSharpness(stack, getSharpness(stack) + amount);
        if(getSharpness(stack) > MAX_SHARPENING)
        {
            int i = Helpers.RANDOM.nextInt(5);
            stack.damageItem(i, entity);
        }
    }

    public static void decrementSharpness(ItemStack stack, int amount)
    {
        setSharpness(stack, getSharpness(stack) - (stack.getItem().getRegistryName().toString().contains("steel_") ? amount / 3 :  amount));
    }

    public static int getSharpness(ItemStack stack)
    {
        if(hasSharpness(stack))
        {
            return getTag(stack).getInteger(SHARPENING_KEY);
        }
        else
        {
            initSharpness(stack);
            return getSharpness(stack);
        }
    }

}
