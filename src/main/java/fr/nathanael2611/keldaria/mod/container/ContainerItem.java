/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.inventory.InventoryItemContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public abstract class ContainerItem extends ContainerKeldaria
{

    public InventoryItemContainer inventory;
    public boolean updateNotification;

    public ContainerItem(InventoryPlayer playerInventory, InventoryItemContainer inventory)
    {
        super(playerInventory);
        this.inventory = inventory;
        this.updateNotification = false;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        this.updateNotification = true;
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

}
