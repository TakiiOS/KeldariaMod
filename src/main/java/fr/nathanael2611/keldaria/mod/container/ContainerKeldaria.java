package fr.nathanael2611.keldaria.mod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public abstract class ContainerKeldaria extends Container
{

    private InventoryPlayer playerInventory;

    protected ContainerKeldaria(InventoryPlayer playerInventory)
    {
        this.playerInventory = playerInventory;
    }

    protected void drawPlayerInventory(int x, int y)
    {
        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(this.playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(this.playerInventory, k, x + k * 18, y + 58));
        }
    }

    protected ItemStack transferStackInSlot(EntityPlayer player, int index, Predicate<ItemStack> condition)
    {
        ItemStack stackToReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack() && condition.test(slot.getStack()))
        {
            ItemStack stack = slot.getStack();
            stackToReturn = stack.copy();


            int invSize = this.inventorySlots.size() - playerInventory.mainInventory.size();
            if (index < invSize)
            {
                if (!this.mergeItemStack(stack, invSize, 36 + invSize, true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack, 0, invSize, false))
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
