/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.container.slot.SlotTyped;
import fr.nathanael2611.keldaria.mod.inventory.InventoryPurse;
import fr.nathanael2611.keldaria.mod.item.ItemCoin;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPurse extends ContainerItem
{

    public InventoryPurse inventory;

    public ContainerPurse(InventoryPurse inventoryPurse, InventoryPlayer inventoryplayer)
    {
        super(inventoryplayer, inventoryPurse);
        this.inventory = inventoryPurse;

        for (int i = 0; i < 8; i++)
        {
            this.addSlotToContainer(new SlotTyped(new Item[]{KeldariaItems.SILVER_COIN, KeldariaItems.GOLD_COIN}, inventoryPurse, i, 17 + (i * 18), 35));
        }

        this.drawPlayerInventory(8, 84);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return this.transferStackInSlot(playerIn, index, stack -> stack.getItem() instanceof ItemCoin);
    }

}