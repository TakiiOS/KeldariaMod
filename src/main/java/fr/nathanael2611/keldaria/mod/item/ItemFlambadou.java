/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.cookingfurnace.BurntAliments;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import fr.nathanael2611.simpledatabasemanager.core.SyncedDatabases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFlambadou extends Item
{

    public ItemFlambadou()
    {
        setCreativeTab(KeldariaTabs.KELDARIA);
        setMaxDamage(100);
        setMaxStackSize(1);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(!playerIn.world.isRemote)
        {
            //playerIn.displayGui(new InterfaceFlambadou());
            playerIn.openGui(Keldaria.getInstance(), KeldariaGuiHandler.ID_FLAMBADOU, worldIn, 0, 0, 0);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static class FlambadouCrafts
    {
        public static final String DB_NAME = "FlambadouCrafts";

        public static void addSyncedDB()
        {
            SyncedDatabases.add(DB_NAME);
        }

        public static ItemStack getResult(EntityPlayer player, Item entry)
        {
            DatabaseReadOnly read = getDB(player);
            if(read.isString(entry.getRegistryName().toString()))
            {
                ItemStack stack = Helpers.getItemStackFromString(read.getString(entry.getRegistryName().toString()));
                ExpiredFoods.create(stack);
                {
                    NBTTagCompound compound = Helpers.getCompoundTag(stack);
                    compound.setBoolean(BurntAliments.FUMED_KEY, true);
                    stack.setTagCompound(compound);
                }
                return stack;
            }
            return ItemStack.EMPTY;
        }

        public static DatabaseReadOnly getDB(EntityPlayer player)
        {
            if(player.world.isRemote)
            {
                return ClientDatabases.getDatabase(DB_NAME);
            }
            else return Databases.getDatabase(DB_NAME);
        }

    }

}
