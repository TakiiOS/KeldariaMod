/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
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

import java.util.List;

public class TileEntityClothingMannequin extends TileEntity implements IInventory
{

    private final NonNullList<ItemStack> items = NonNullList.withSize(18, ItemStack.EMPTY);

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
        IBlockState state = world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
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
        return "Clothing Mannequin";
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
        this.clear();
        this.items.clear();
        ItemStackHelper.loadAllItems(compound, this.items);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public List<String> getClothesURL()
    {
        List<String> list = Lists.newArrayList();
        list.add("http://keldaria.fr/skinhosting/mannequin.png");
        for (int i = items.size()- 1; i >= 0; i --)
        {
            ItemStack item = items.get(i);
            if(ItemClothe.isClothValid(item))
            {
                list.add(ItemClothe.getClothURL(item));
            }
        }

        return list;
    }

}
