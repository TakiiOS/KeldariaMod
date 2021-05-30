/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.item.ItemStack;

public class InventoryKeyRing extends InventoryItemContainer
{
    public InventoryKeyRing(ItemStack itemstack)
    {
        super(itemstack, "keyring", 8 * 3);
    }
}