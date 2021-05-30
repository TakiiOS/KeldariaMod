/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

import java.util.function.Function;

public class TileEntityFurnitureContainer extends TileEntity implements IInventory
{

    private int size;
    private int rowSize;
    private NonNullList<ItemStack> items;
    private boolean sync;
    protected Function<ItemStack, Boolean> itemConsumer = stack -> true;

    public TileEntityFurnitureContainer()
    {
    }

    public TileEntityFurnitureContainer(int size, int rowSize, boolean synchronize)
    {
        this.size = size;
        this.rowSize = rowSize;
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.sync = synchronize;
    }

    @Override
    public int getSizeInventory()
    {
        return items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        items.set(index, stack);
        markDirty();
        if (this.sync)
        {
            IBlockState state = world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        } else
        {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }


    public NonNullList<ItemStack> getItemsList()
    {
        return items;
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
        this.items.clear();
    }

    @Override
    public String getName()
    {
        return "Meuble";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if(this.items == null)
        {
            this.size = compound.getInteger("Size");
            this.rowSize = compound.getInteger("RowSize");
            this.items = NonNullList.withSize(this.size, ItemStack.EMPTY);
        }
        this.items.clear();
        ItemStackHelper.loadAllItems(compound, this.items);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Size", this.size);
        compound.setInteger("RowSize", this.rowSize);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        if (this.sync)
        {
            return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
        } else
        {
            return super.getUpdatePacket();
        }
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet)
    {
        if (this.sync)
        {
            this.readFromNBT(packet.getNbtCompound());
        } else {
            super.onDataPacket(net, packet);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        if (this.sync)
        {
            return this.writeToNBT(new NBTTagCompound());
        } else return super.getUpdateTag();
    }


    public int getRowSize()
    {
        return rowSize;
    }

    public boolean canAccept(ItemStack stack)
    {
        return this.itemConsumer.apply(stack);
    }



}
