/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.rot;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.rot.capability.Rot;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class ExpiredFoods
{

    public static final String KEY = "DateCreated";

    public static final HashMap<Item, Integer> PEREMPTIONS = Maps.newHashMap();

    public static void init()
    {
        PEREMPTIONS.put(KeldariaItems.PEMMICAN, (8 * 14) * 10);
        PEREMPTIONS.put(KeldariaItems.OMELET, 10);
    }

    public static int getPeremptionTime(Item item)
    {
        return PEREMPTIONS.getOrDefault(item, 30);
    }

    public static int getPeremptionTime(ItemStack stack)
    {
        return getPeremptionTime(stack.getItem());
    }

    public static boolean isExpired(ItemStack stack)
    {
        return getRot(stack).getDays() > getPeremptionTime(stack);
    }

    public static Rot getRot(ItemStack stack)
    {
        return new Rot(stack);
    }

    public static void create(ItemStack stack)
    {
        getRot(stack).setCreatedDay(KeldariaDate.getKyrgonDate().getTotalDaysInRP());
    }

    /*
    public static int getDaysSinceCreation(ItemStack stack)
    {
        return KeldariaDate.lastDate.getTotalDaysInRP() - getCreatedDate(stack);
    }

    public static int getCreatedDate(ItemStack stack)
    {
        return getCompoundTag(stack).getInteger(KEY);
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static void create(ItemStack stack)
    {
        getCompoundTag(stack).setInteger(KEY, KeldariaDate.getKyrgonDate().getTotalDaysInRP());
    }

    public static boolean canExpire(Item item)
    {
        return item instanceof ItemFood || PEREMPTIONS.containsKey(item);
    }*/


}
