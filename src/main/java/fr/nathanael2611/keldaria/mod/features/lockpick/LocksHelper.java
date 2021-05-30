/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.lockpick;

import fr.nathanael2611.keldaria.mod.inventory.InventoryKeyRing;
import fr.nathanael2611.keldaria.mod.item.ItemKey;
import fr.nathanael2611.keldaria.mod.item.ItemLock;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class LocksHelper
{

    public static final int CODE_SIZE = 4;

    public static final Lock EMPTY_LOCK = new Lock(BlockPos.ORIGIN, 00000000, false);

    public static Database getDB() {
        return Databases.getDatabase("locks");
    }

    public static boolean isKeyValid(ItemStack key, int keyId)
    {
        if(key.getItem() == KeldariaItems.KEYRING)
        {
            InventoryKeyRing keyRing = new InventoryKeyRing(key);
            for (int i = 0; i < keyRing.getSizeInventory(); i++)
            {
                if(getKeyId(keyRing.getStackInSlot(i)) == keyId) return true;
            }
            return false;
        }
        else
        {
            return getKeyId(key) == keyId;
        }
    }

    public static Lock getLockFromPos(BlockPos pos)
    {
        Database db = getDB();
        if(db.isInteger(Helpers.blockPosToString(pos)))
        {
            return new Lock(pos, db.getInteger(Helpers.blockPosToString(pos)), true);
        }
        return EMPTY_LOCK;
    }

    public static void lockDoor(BlockPos doorPos, int keyId)
    {
        Database db = getDB();
        db.setInteger(Helpers.blockPosToString(doorPos), keyId);
    }

    public static void unLockDoor(BlockPos doorPos)
    {
        Database db = getDB();
        if(db.isInteger(Helpers.blockPosToString(doorPos)))
        {
            db.remove(Helpers.blockPosToString(doorPos));
        }
    }

    public static int getKeyId(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) compound = new NBTTagCompound();
        if(hasKeyId(stack))
        {
            return compound.getInteger(ItemKey.TAG_KEY_ID);
        }
        return randomCode();
    }

    public static boolean hasKeyId(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemKey.TAG_KEY_ID);
    }

    public static int randomCode()
    {
        StringBuilder builder = new StringBuilder();
        Random rand = Helpers.RANDOM;
        for (int i = 0; i < CODE_SIZE; i++)
        {
            builder.append(rand.nextInt(10));
        }
        return Integer.parseInt(builder.toString());
    }

    public static void giveRandomKeyCode(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null) tag = new NBTTagCompound();
        if(!LocksHelper.hasKeyId(stack))
        {
            tag.setInteger(ItemKey.TAG_KEY_ID, LocksHelper.randomCode());
            stack.setTagCompound(tag);
        }
    }

    public static void setKeyCode(ItemStack stack, int code){
        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null)tag = new NBTTagCompound();

        tag.setInteger(ItemKey.TAG_KEY_ID, code);
        stack.setTagCompound(tag);

    }

    public static ItemStack createWithId(Item item, int keyId)
    {
        ItemStack stack = new ItemStack(item);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger(ItemKey.TAG_KEY_ID, keyId);
        stack.setTagCompound(compound);
        return stack;
    }

    public static boolean canContainsKeyID(ItemStack stack)
    {
        return stack.getItem() instanceof ItemKey || stack.getItem() instanceof ItemLock;
    }

    public static boolean isMatrixWillCraftKey(InventoryCrafting inventoryCrafting)
    {
        return LocksHelper.canContainsKeyID(inventoryCrafting.getStackInSlot(0)) && LocksHelper.hasKeyId(inventoryCrafting.getStackInSlot(0)) && LocksHelper.canContainsKeyID(inventoryCrafting.getStackInSlot(1)) && !LocksHelper.hasKeyId(inventoryCrafting.getStackInSlot(1));
    }

}
