package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityClothingMannequin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerClothingMannequin extends ContainerKeldaria
{

    private final TileEntityClothingMannequin tileMannequin;

    public ContainerClothingMannequin(InventoryPlayer playerInventory, TileEntityClothingMannequin tileMannequin)
    {
        super(playerInventory);
        this.tileMannequin = tileMannequin;

        int y = 0;
        for (int i = 0; i < 18; i++)
        {
            this.addSlotToContainer(new Slot(tileMannequin, i, 8 + (i * 18) - (i > 8 ? (18*9) : 0), 35 - 18/2 + y));
            if(i == 8)
            {
                y += 18;
            }
        }
        this.drawPlayerInventory(8, 84);
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileMannequin);
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileMannequin.isUsableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return this.transferStackInSlot(playerIn, index, stack -> true);
    }
}