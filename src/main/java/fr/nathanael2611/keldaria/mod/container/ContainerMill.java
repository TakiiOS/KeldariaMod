/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.container.slot.SlotResult;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMill extends ContainerKeldaria
{

    private TileEntityMill mill;

    public ContainerMill(TileEntityMill mill, InventoryPlayer inventoryPlayer)
    {
        super(inventoryPlayer);
        this.mill = mill;
        this.addSlotToContainer(new Slot(this.mill, 0, 80, 4)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.WHEAT;
            }
        });
        this.addSlotToContainer(new SlotResult(this.mill, 1, 80, 56));
        this.drawPlayerInventory(8, 84);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}