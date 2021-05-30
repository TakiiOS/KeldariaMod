/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.inventory;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.item.ItemArmorPart;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class InventoryArmor implements IInventory
{

    public final NonNullList<ItemStack> armorItems = NonNullList.<ItemStack>withSize(2 + 5 + 3 + 2, ItemStack.EMPTY);

    public InventoryArmor()
    {
    }

    public List<ItemStack> getArmor(EntityEquipmentSlot slot)
    {
        switch (slot)
        {
            case HEAD:
                return Lists.newArrayList(getStackInSlot(0), getStackInSlot(1));
            case CHEST:
                return Lists.newArrayList(getStackInSlot(2), getStackInSlot(3), getStackInSlot(4), getStackInSlot(5), getStackInSlot(6));
            case LEGS:
                return Lists.newArrayList(getStackInSlot(7), getStackInSlot(8), getStackInSlot(9));
            case FEET:
                return Lists.newArrayList(getStackInSlot(10), getStackInSlot(11));
            default:
                return Lists.newArrayList();
        }
    }

    public boolean isWearingHelm()
    {
        for (ItemStack stack : this.getArmor(EntityEquipmentSlot.HEAD))
        {
            if(stack.getItem().getRegistryName().toString().endsWith("_helm"))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isArmorEmpty()
    {
        for (ItemStack armorItem : this.armorItems)
        {
            if(!armorItem.isEmpty()) return false;
        }
        return true;
    }

    public int getProtectionPercent(EnumAttackType attackType)
    {
        /*
         * Head:  20
         * Chest: 40
         * Legs: 30
         * Feet: 10
         */

        int headPercent = Helpers.crossMult(getProtectionPercent(attackType, EntityEquipmentSlot.HEAD), 100, 20);
        //System.out.println(headPercent);
        int chestPercent = Helpers.crossMult(getProtectionPercent(attackType, EntityEquipmentSlot.CHEST), 100, 40);
        int legsPercent = Helpers.crossMult(getProtectionPercent(attackType, EntityEquipmentSlot.LEGS), 100, 30);
        int feetPercent = Helpers.crossMult(getProtectionPercent(attackType, EntityEquipmentSlot.FEET), 100, 10);
        int totalPercent = headPercent + chestPercent + legsPercent + feetPercent;
        return totalPercent;
    }

    public int getProtectionPercent(EnumAttackType attackType, EntityEquipmentSlot equipmentSlot)
    {
        int percent = 0;
        for (ItemStack stack : this.getArmor(equipmentSlot))
        {
            if(stack.getItem() instanceof ItemArmorPart)
            {
                ItemArmorPart part = (ItemArmorPart) stack.getItem();
                percent += part.getProtectionPercent(attackType);
            }
        }
        return Math.min(Math.max(0, percent), 100);
    }

    public void loadInventoryFromNBT(NBTTagCompound compound)
    {
        ItemStackHelper.loadAllItems(compound, this.armorItems);
    }

    public NBTTagCompound saveInventoryToNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        ItemStackHelper.saveAllItems(compound, this.armorItems);
        return compound;
    }


    @Override
    public int getSizeInventory()
    {
        return this.armorItems.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.armorItems)
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
        return (index < this.armorItems.size() && index >= 0) ? this.armorItems.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return (index < this.armorItems.size() && index >= 0) && !this.armorItems.get(index).isEmpty() ? ItemStackHelper.getAndSplit(this.armorItems, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (!this.armorItems.get(index).isEmpty())
        {
            ItemStack itemstack = this.armorItems.get(index);
            this.armorItems.set(index, ItemStack.EMPTY);
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
        this.armorItems.set(index, stack);
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
        this.armorItems.clear();
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





}
