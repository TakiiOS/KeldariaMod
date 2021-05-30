/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityHRPCarpet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHRPCarpet extends ContainerKeldaria
{

    private final TileEntityHRPCarpet hrpCarpet;

    public ContainerHRPCarpet(InventoryPlayer playerInventory, TileEntityHRPCarpet hrpCarpet)
    {
        super(playerInventory);
        this.hrpCarpet = hrpCarpet;
        for (int i = 0; i < 5; i++)
        {
            this.addSlotToContainer(new Slot(hrpCarpet, i, 44 + (i * 18), 35));
        }
        this.drawPlayerInventory(8, 84);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.hrpCarpet);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.hrpCarpet.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return this.transferStackInSlot(playerIn, index, stack -> true);
    }

}