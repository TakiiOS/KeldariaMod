/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.container.slot.SlotResult;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityBeerBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBeerBarrel extends ContainerKeldaria
{

    private TileEntityBeerBarrel barrel;

    public ContainerBeerBarrel(TileEntityBeerBarrel barrel, InventoryPlayer inventoryPlayer)
    {
        super(inventoryPlayer);
        this.barrel = barrel;
        this.addSlotToContainer(new Slot(this.barrel, 0, 60, 5));
        this.addSlotToContainer(new Slot(this.barrel, 1, 78, 5));
        this.addSlotToContainer(new Slot(this.barrel, 2, 96, 5));
        this.addSlotToContainer(new Slot(this.barrel, 3, 78, 23)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == KeldariaItems.EMPTY_CHOP || stack.getItem() == KeldariaItems.EMPTY_WINE_BOTTLE;
            }
        });
        this.addSlotToContainer(new SlotResult(this.barrel, 4, 78, 65));
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