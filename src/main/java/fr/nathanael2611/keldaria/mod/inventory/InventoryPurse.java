package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.item.ItemStack;

public class InventoryPurse extends InventoryItemContainer
{
    public InventoryPurse(ItemStack itemstack)
    {
        super(itemstack, "purse", 8);
    }
}
