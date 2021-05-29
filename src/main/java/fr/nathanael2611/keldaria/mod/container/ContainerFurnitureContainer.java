package fr.nathanael2611.keldaria.mod.container;

import fr.nathanael2611.keldaria.mod.block.furniture.TileEntityChessPlate;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFurnitureContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFurnitureContainer extends ContainerKeldaria
{
    private final TileEntityFurnitureContainer tileFurnace;

    public ContainerFurnitureContainer(InventoryPlayer playerInventory, TileEntityFurnitureContainer furnaceInventory)
    {
        super(playerInventory);
        this.tileFurnace = furnaceInventory;

        int totalSize = furnaceInventory.getSizeInventory();


        int rows = (int) Math.ceil(totalSize / tileFurnace.getRowSize());
        int startY = (18 * 2) - ((rows *18)/ 2);
        int y = 0;
        final int baseX = ((9 * 18)) / 2 - ((tileFurnace.getRowSize() * 18) /2);
        int x = 0;
        if(tileFurnace instanceof TileEntityChessPlate)
        {
            startY -= 2*18;
        }
        for (int i = 0; i < totalSize; i++)
        {
            this.addSlotToContainer(new Slot(furnaceInventory, i, baseX + 7 + (x * 18), startY + 10 + y * 18){
                @Override
                public boolean isItemValid(ItemStack stack)
                {
                    return tileFurnace.canAccept(stack);
                }
            });
            x++;
            if(x == this.tileFurnace.getRowSize())
            {
                x = 0;
                y += 1;
            }

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
        ItemStack stackToReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackToReturn = stack.copy();

            if (index < tileFurnace.getSizeInventory())
            {
                if (!this.mergeItemStack(stack, (tileFurnace.getSizeInventory()), 36 + (tileFurnace.getSizeInventory()), true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, tileFurnace.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            } else
            {
                slot.onSlotChanged();
            }
        }
        return stackToReturn;
    }
}