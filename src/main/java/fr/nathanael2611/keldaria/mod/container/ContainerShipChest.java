package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.inventory.InventoryShipChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShipChest extends ContainerKeldaria
{

    public boolean updateNotification;
    public InventoryShipChest inventory;

    public ContainerShipChest(InventoryShipChest inventoryPurse, InventoryPlayer inventoryplayer)
    {
        super(inventoryplayer);
        this.updateNotification = false;
        this.inventory = inventoryPurse;

        int index = 0;
        for(int i = 0; i < 6; i ++)
        {
            for (int k = 0; k < 9; k++)
            {

                this.addSlotToContainer(new Slot(inventoryPurse, index, 8 + (k * 18), 5 + (i * 17)));
                index ++;
            }
        }

        this.drawPlayerInventory(8, 110);
    }


    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        return itemstack;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        this.updateNotification = true;
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

}
