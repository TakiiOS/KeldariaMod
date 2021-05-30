/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotTyped extends Slot
{

    private Item[] acceptedItems;

    public SlotTyped(Item[] acceptedItems, IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.acceptedItems = acceptedItems;
    }


    @Override
    public boolean isItemValid(ItemStack stack)
    {
        for (Item acceptedItem : this.acceptedItems)
        {
            if(stack.getItem() == acceptedItem) return true;
        }
        return false;
    }
}
