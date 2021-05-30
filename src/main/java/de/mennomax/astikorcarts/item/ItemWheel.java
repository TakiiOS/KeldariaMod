/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.item;

import de.mennomax.astikorcarts.AstikorCarts;
import de.mennomax.astikorcarts.init.ModCreativeTabs;
import net.minecraft.item.Item;

public class ItemWheel extends Item
{
    public ItemWheel()
    {
        this.setRegistryName(AstikorCarts.MODID, "wheel");
        this.setTranslationKey(this.getRegistryName().toString());
        this.setCreativeTab(ModCreativeTabs.astikor);
    }
}
