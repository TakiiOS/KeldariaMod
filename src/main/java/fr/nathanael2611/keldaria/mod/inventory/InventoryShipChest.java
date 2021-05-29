package fr.nathanael2611.keldaria.mod.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class InventoryShipChest extends InventoryBasic
{
    public InventoryShipChest(int slotCount)
    {
        super("ShipChest", false, slotCount);
    }

    public void readNBT(NBTTagCompound compound)
    {
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, list);
        for (int i = 0; i < list.size(); i++)
        {
            setInventorySlotContents(i, list.get(i));
        }
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NonNullList<ItemStack> list = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++)
        {
            list.set(i, getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(compound, list);
        return compound;
    }

}
