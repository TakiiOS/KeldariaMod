/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.cookingfurnace;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.item.ItemStack;

public class BurntAliments
{

    public static final String BURNT_KEY = "IsBurnt";
    public static final String FUMED_KEY = "IsFumed";

    public static boolean isBurned(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            return stack.getTagCompound().getBoolean(BurntAliments.BURNT_KEY);
        }
        return false;
    }

    public static boolean isFumed(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).getBoolean(FUMED_KEY);
    }

}
