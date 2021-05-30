/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.inventory;

import fr.nathanael2611.keldaria.mod.container.ContainerFlambadou;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class InventoryFlambadou extends InventoryBasic
{

    private ContainerFlambadou eventHandler;

    public InventoryFlambadou(ContainerFlambadou flambadou)
    {
        super("Flambadou", false, 3);
        this.eventHandler = flambadou;
    }


    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        super.setInventorySlotContents(index, stack);
        if(index != 2)
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = super.decrStackSize(index, count);
        if(index != 2)
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
    }
}
