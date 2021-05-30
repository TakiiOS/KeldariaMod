/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs
{
    public static CreativeTabs astikor = new CreativeTabs("astikorcarts")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.WHEEL);
        }
    };
}
