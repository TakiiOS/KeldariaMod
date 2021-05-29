package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public abstract class InventoryItemContainer extends InventoryBasic
{

    public InventoryItemContainer(ItemStack stack, String title, int slotCount)
    {
        super(title, false, slotCount);
        if (stack.hasTagCompound())
        {
            this.readNBT(stack.getTagCompound());
        }
    }

    public void readNBT(NBTTagCompound compound)
    {
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, list);
        for (int i = 0; i < list.size(); i++)
        {
            this.setInventorySlotContents(i, list.get(i));
        }
    }

    public void writeNBT(NBTTagCompound compound)
    {
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++)
        {
            list.set(i, getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(compound, list);
    }

}
