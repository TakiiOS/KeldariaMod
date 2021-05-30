/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;

public class ContainerQuiver extends ContainerKeldaria
{
    private final InventoryQuiver quiver;

    public ContainerQuiver(InventoryPlayer inventoryPlayer, InventoryQuiver quiver)
    {
        super(inventoryPlayer);
        this.quiver = quiver;

        int x = 0;
        for (int i = 0; i < this.quiver.getSizeInventory(); i++)
        {
            this.addSlotToContainer(new Slot(this.quiver, i, 17 + x + (18 * 2), 35)
            {
                @Override
                public boolean isItemValid(ItemStack stack)
                {
                    return super.isItemValid(stack);
                }
            });
            x += 18;
        }
        this.drawPlayerInventory(8, 84);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.quiver);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return this.transferStackInSlot(playerIn, index, stack -> stack.getItem() instanceof ItemArrow);
    }
}