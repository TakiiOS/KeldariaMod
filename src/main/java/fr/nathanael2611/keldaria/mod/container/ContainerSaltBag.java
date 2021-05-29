package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntitySaltBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSaltBag extends ContainerKeldaria
{
    private final TileEntitySaltBag tileFurnace;

    public ContainerSaltBag(InventoryPlayer playerInventory, TileEntitySaltBag furnaceInventory)
    {
        super(playerInventory);
        this.tileFurnace = furnaceInventory;

        for (int i = 0; i < 9; i++)
        {
            this.addSlotToContainer(new Slot(furnaceInventory, i, 8 + (i * 18), 35));
        }
        this.drawPlayerInventory(8, 84);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileFurnace);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return this.transferStackInSlot(playerIn, index, stack -> true);
    }
}