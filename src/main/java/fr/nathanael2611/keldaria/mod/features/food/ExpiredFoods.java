/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.food;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.food.capability.Rot;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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


}
