package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.rot.capability.Rot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntitySaltBag extends TileEntity implements IInventory, ITickable
{

    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    @Override
    public void update()
    {
        if(world.isRemote) return;
        for (ItemStack item : items)
        {
            if(item.getItem() instanceof ItemFood)
            {
                if (!ExpiredFoods.isExpired(item))
                {
                    Rot rot = ExpiredFoods.getRot(item);
                    if(!rot.isInBag())
                    {
                        rot.setSaltBagDay(KeldariaDate.lastDate.getTotalDaysInRP());
                    }
                }
                //if(!ExpiredFoods.isExpired(item)) ExpiredFoods.create(item);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.set(index, stack);
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
    @Override
    public int getField(int id) { return 0; }
    @Override
    public void setField(int id, int value) { }
    @Override
    public int getFieldCount() { return 0; }
    @Override
    public void clear() { this.items.clear(); }
    @Override
    public String getName() { return "Sac de sel"; }
    @Override
    public boolean hasCustomName() { return false; }

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


}
