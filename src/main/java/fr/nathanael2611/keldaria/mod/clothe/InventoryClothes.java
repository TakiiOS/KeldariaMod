package fr.nathanael2611.keldaria.mod.clothe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryClothes implements IInventory
{

    public final NonNullList<ItemStack> clothesInventory = NonNullList.<ItemStack>withSize(9*2, ItemStack.EMPTY);

    public EntityPlayer player;

    public InventoryClothes(EntityPlayer playerIn)
    {
        this.player = playerIn;
    }

    public void loadInventoryFromNBT(NBTTagCompound compound)
    {
        ItemStackHelper.loadAllItems(compound, this.clothesInventory);
    }

    public NBTTagCompound saveInventoryToNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        ItemStackHelper.saveAllItems(compound, this.clothesInventory);
        return compound;
    }


    @Override
    public int getSizeInventory()
    {
        return this.clothesInventory.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.clothesInventory)
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
        return (index < this.clothesInventory.size() && index >= 0) ? this.clothesInventory.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return (index < this.clothesInventory.size() && index >= 0) && !this.clothesInventory.get(index).isEmpty() ? ItemStackHelper.getAndSplit(this.clothesInventory, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (!this.clothesInventory.get(index).isEmpty())
        {
            ItemStack itemstack = this.clothesInventory.get(index);
            this.clothesInventory.set(index, ItemStack.EMPTY);
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
        this.clothesInventory.set(index, stack);
        this.player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
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
        this.clothesInventory.clear();
    }

    @Override
    public String getName()
    {
        return "Clothes";
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
}
