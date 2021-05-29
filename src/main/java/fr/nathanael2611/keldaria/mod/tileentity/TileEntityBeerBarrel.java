package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.beerbarrel.BeerBarrelRegistry;
import fr.nathanael2611.keldaria.mod.features.beerbarrel.BeerCraft;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class TileEntityBeerBarrel extends TileEntity implements ITickable, IInventory
{

    private final NonNullList<ItemStack> barrelContent = NonNullList.withSize(5, ItemStack.EMPTY);

    private BeerBarrelRegistry registry = Keldaria.getInstance().getRegistry().getBeerBarrelRegistry();

    private int fermentationTime = 0;
    private int totalFermentationTime = 0;

    @Override
    public void update()
    {
        if(this.canFerment())
        {
            BeerCraft craft = this.getCraft();
            this.totalFermentationTime = craft.getFermentationTime();
            this.fermentationTime ++;
            if(this.fermentationTime >= this.totalFermentationTime)
            {
                this.fermentationTime = 0;
                this.totalFermentationTime = 0;
                this.ferment(craft);
            }
        }
        else
        {
            this.totalFermentationTime = 0;
            this.fermentationTime = 0;
        }
    }

    public boolean canFerment()
    {
        return this.registry.contains(getIngredients(), this.barrelContent.get(3).getItem()) &&
                this.barrelContent.get(4).isEmpty();
    }

    public void ferment(BeerCraft craft)
    {
        if(this.canFerment())
        {
            this.barrelContent.get(0).shrink(1);
            this.barrelContent.get(1).shrink(1);
            this.barrelContent.get(2).shrink(1);
            this.barrelContent.get(3).shrink(1);
            this.barrelContent.set(4, craft.getResult());
        }
    }

    public List<Item> getIngredients()
    {
        return Lists.newArrayList(this.barrelContent.get(0).getItem(), this.barrelContent.get(1).getItem(), this.barrelContent.get(2).getItem());
    }

    public BeerCraft getCraft()
    {
        return this.registry.get(this.getIngredients(), this.barrelContent.get(3).getItem());
    }

    @Override
    public int getSizeInventory()
    {
        return this.barrelContent.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.barrelContent)
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
        return (index < this.barrelContent.size() && index >= 0) ? this.barrelContent.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return (index < this.barrelContent.size() && index >= 0) && !this.barrelContent.get(index).isEmpty() ? ItemStackHelper.getAndSplit(this.barrelContent, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (!this.barrelContent.get(index).isEmpty())
        {
            ItemStack itemstack = this.barrelContent.get(index);
            this.barrelContent.set(index, ItemStack.EMPTY);
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
        this.barrelContent.set(index, stack);
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
        this.barrelContent.clear();
    }

    @Override
    public String getName()
    {
        return "Fermentation Barrel";
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

    public int getFermentationProgress()
    {
        if(this.totalFermentationTime == 0) return 0;
        return this.fermentationTime * 26 / this.totalFermentationTime;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.clear();
        this.barrelContent.clear();
        ItemStackHelper.loadAllItems(compound, this.barrelContent);
        this.fermentationTime = compound.getInteger("BurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.fermentationTime);
        ItemStackHelper.saveAllItems(compound, this.barrelContent);
        return compound;
    }

}
