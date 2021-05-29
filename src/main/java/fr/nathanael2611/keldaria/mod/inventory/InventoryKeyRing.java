package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.item.ItemStack;

public class InventoryKeyRing extends InventoryItemContainer
{
    public InventoryKeyRing(ItemStack itemstack)
    {
        super(itemstack, "keyring", 8 * 3);
    }
}