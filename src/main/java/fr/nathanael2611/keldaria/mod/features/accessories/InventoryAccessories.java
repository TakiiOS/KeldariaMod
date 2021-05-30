/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.accessories;

import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryAccessories implements IInventory
{

    public final NonNullList<ItemStack> accessoriesInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);

    public InventoryAccessories()
    {
    }

    public void loadInventoryFromNBT(NBTTagCompound compound)
    {
        ItemStackHelper.loadAllItems(compound, this.accessoriesInventory);
    }

    public NBTTagCompound saveInventoryToNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        ItemStackHelper.saveAllItems(compound, this.accessoriesInventory);
        return compound;
    }


    @Override
    public int getSizeInventory()
    {
        return this.accessoriesInventory.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.accessoriesInventory)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return (index < this.accessoriesInventory.size() && index >= 0) ? this.accessoriesInventory.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return (index < this.accessoriesInventory.size() && index >= 0) && !this.accessoriesInventory.get(index).isEmpty() ? ItemStackHelper.getAndSplit(this.accessoriesInventory, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (!this.accessoriesInventory.get(index).isEmpty())
        {
            ItemStack itemstack = this.accessoriesInventory.get(index);
            this.accessoriesInventory.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.accessoriesInventory.set(index, stack);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        this.accessoriesInventory.clear();
    }

    @Override
    public String getName()
    {
        return "Accessories";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }


    public ItemStack getAccessory(EntityEquipmentSlot slot)
    {
        switch (slot)
        {
            case HEAD:
                return getStackInSlot(0);
            case CHEST:
                return getStackInSlot(1);
            case LEGS:
                return getStackInSlot(2);
            case FEET:
                return getStackInSlot(3);
            default:
                return ItemStack.EMPTY;
        }
    }

    public void makeUse(EntityPlayer player, EntityEquipmentSlot slot)
    {
        Item item = getAccessory(slot).getItem();
        if(item instanceof ItemAccessory)
        {
            ((ItemAccessory) item).use(player);
        }
    }

    public boolean canSee()
    {
        ItemStack stack = getAccessory(EntityEquipmentSlot.HEAD);
        return stack.getItem() != KeldariaItems.SLEEPING_MASK;
    }
}
