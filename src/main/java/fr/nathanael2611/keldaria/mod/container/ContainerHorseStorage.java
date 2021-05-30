/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.inventory.InventoryHorseStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHorseStorage extends ContainerKeldaria
{
    private final InventoryHorseStorage horseStorage;

    public ContainerHorseStorage(InventoryPlayer inventoryPlayer, InventoryHorseStorage horseStorage)
    {
        super(inventoryPlayer);
        this.horseStorage = horseStorage;

        int x = 0;
        for(int i = 0; i < this.horseStorage.getSizeInventory(); i ++)
        {
            this.addSlotToContainer(new Slot(this.horseStorage, i, 17 + x + (18 * 2), 35));
            x += 18;
        }
        this.drawPlayerInventory(8, 84);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.horseStorage);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stackToReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackToReturn = stack.copy();

            if (index < 10)
            {
                if (!this.mergeItemStack(stack, 10, 40, true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, 4, false))
            {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            } else
            {
                slot.onSlotChanged();
            }
        }
        return stackToReturn;
    }
}