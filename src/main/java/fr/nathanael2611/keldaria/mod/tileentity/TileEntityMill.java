/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class TileEntityMill extends TileEntity implements ITickable, IInventory
{

    private static final int MAX_BURN_TIME = 100;

    private final NonNullList<ItemStack> millContents = NonNullList.withSize(2, ItemStack.EMPTY);
    private int burnTime;

    @Override
    public void update()
    {
        if(this.canGrind())
        {
            this.burnTime ++;
            if(this.burnTime >= MAX_BURN_TIME)
            {
                this.burnTime = 0;
                this.grind();
            }
        }
        else
        {
            this.burnTime = Math.max(this.burnTime --, 0);
        }
    }

    public boolean canGrind()
    {
        return !this.getStackInSlot(0).isEmpty() && this.getStackInSlot(0).getItem() == Items.WHEAT && this.getStackInSlot(1).getCount() < KeldariaItems.FLOUR.getItemStackLimit();
    }

    public void grind()
    {
        if(this.canGrind())
        {
            this.getStackInSlot(0).shrink(1);
            ItemStack stack = new ItemStack(KeldariaItems.FLOUR, this.getStackInSlot(1).getCount() + 1);
            this.setInventorySlotContents(1, stack);
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.millContents.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.millContents)
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
        return (index < this.millContents.size() && index >= 0) ? this.millContents.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return (index < this.millContents.size() && index >= 0) && !this.millContents.get(index).isEmpty() ? ItemStackHelper.getAndSplit(this.millContents, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (!this.millContents.get(index).isEmpty())
        {
            ItemStack itemstack = this.millContents.get(index);
            this.millContents.set(index, ItemStack.EMPTY);
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
        this.millContents.set(index, stack);
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
        this.millContents.clear();
    }

    @Override
    public String getName()
    {
        return "Mill";
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

    public int getGrindProgress()
    {
        return this.burnTime * 26 / MAX_BURN_TIME;
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.clear();
        this.millContents.clear();
        ItemStackHelper.loadAllItems(compound, this.millContents);
        this.burnTime = compound.getInteger("BurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        ItemStackHelper.saveAllItems(compound, this.millContents);
        return compound;
    }

}