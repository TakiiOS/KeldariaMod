/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network;

import de.mennomax.astikorcarts.client.gui.inventory.GuiPlow;
import de.mennomax.astikorcarts.entity.EntityCargoCart;
import de.mennomax.astikorcarts.entity.EntityPlowCart;
import de.mennomax.astikorcarts.inventory.ContainerPlowCart;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import fr.nathanael2611.keldaria.mod.client.gui.*;
import fr.nathanael2611.keldaria.mod.container.*;
import fr.nathanael2611.keldaria.mod.inventory.InventoryKeyRing;
import fr.nathanael2611.keldaria.mod.inventory.InventoryPurse;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.item.ItemKeyring;
import fr.nathanael2611.keldaria.mod.item.ItemPurse;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import fr.nathanael2611.keldaria.mod.tileentity.*;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class KeldariaGuiHandler implements IGuiHandler
{

    public static int ID_CLOTHS_WARDROBE = 12;
    public static int ID_KEYRING_INV = 13;
    public static int ID_ACCESSORIES = 15;
    public static int ID_PURSE_INV = 16;
    public static int ID_HORSE_STORAGE = 17;
    public static int ID_QUIVER = 18;
    public static int ID_FLAMBADOU = 19;

    public static int OPEN_CARGO_CHEST = 30;
    public static int OPEN_PLOW_CART = 31;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
        ItemStack heldItem = player.getHeldItemMainhand();
        if (ID == ID_CLOTHS_WARDROBE)
        {
            return new ContainerCloths(((IKeldariaPlayer) player).getInventoryCloths());
        } else if (ID == ID_ACCESSORIES)
        {
            return new ContainerAccessories(player.inventory, ((IKeldariaPlayer) player).getInventoryAccessories(), ((IKeldariaPlayer) player).getInventoryArmor(), ((IKeldariaPlayer)player).getInventoryCloths());
        } else if (ID == ID_KEYRING_INV)
        {
            if (heldItem.getItem() instanceof ItemKeyring)
            {
                InventoryKeyRing inventory = new InventoryKeyRing(heldItem);
                return new ContainerKeyring(inventory, player.inventory);
            }
        } else if (ID == ID_PURSE_INV)
        {
            if (heldItem.getItem() instanceof ItemPurse)
            {
                InventoryPurse inventory = new InventoryPurse(heldItem);
                return new ContainerPurse(inventory, player.inventory);
            }
        } else if (ID == ID_QUIVER)
        {
            ItemStack quiver = keldariaPlayer.getInventoryAccessories().getAccessory(EntityEquipmentSlot.CHEST);
            if (quiver.getItem() instanceof ItemQuiver)
            {
                InventoryQuiver inventory = new InventoryQuiver(player, quiver);
                return new ContainerQuiver(player.inventory, inventory);
            }
        } else if (ID == ID_FLAMBADOU)
        {
            return new ContainerFlambadou(player.inventory, player);
        } else if (ID == OPEN_CARGO_CHEST)
        {
            EntityCargoCart cart = (EntityCargoCart) world.getEntityByID(x);
            return new ContainerChest(player.inventory, cart.inventory, player);
        } else if (ID == ID_HORSE_STORAGE)
        {
            if (player.isRiding())
            {
                if (player.getRidingEntity() instanceof AbstractHorse)
                {
                    ILivingHorse livingHorse = (ILivingHorse) player.getRidingEntity();
                    return new ContainerHorseStorage(player.inventory, livingHorse.getHorseStorage());
                }
            }
        } else if (ID == OPEN_PLOW_CART)
        {
            EntityPlowCart plow = (EntityPlowCart) world.getEntityByID(x);
            return new ContainerPlowCart(player.inventory, plow.inventory, plow, player);
        }
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile instanceof TileEntityCookingFurnace)
        {
            return new ContainerCookingFurnace(player.inventory, (TileEntityCookingFurnace) tile);
        } else if (tile instanceof TileEntityMill)
        {
            return new ContainerMill((TileEntityMill) tile, player.inventory);
        } else if (tile instanceof TileEntityBeerBarrel)
        {
            return new ContainerBeerBarrel((TileEntityBeerBarrel) tile, player.inventory);
        } else if (tile instanceof TileEntitySaltBag)
        {
            return new ContainerSaltBag(player.inventory, (TileEntitySaltBag) tile);
        } else if (tile instanceof TileEntityHRPCarpet)
        {
            return new ContainerHRPCarpet(player.inventory, (TileEntityHRPCarpet) tile);
        } else if (tile instanceof TileEntityClothingMannequin)
        {
            return new ContainerClothingMannequin(player.inventory, (TileEntityClothingMannequin) tile);
        } else if (tile instanceof TileEntityFurnitureContainer)
        {
            return new ContainerFurnitureContainer(player.inventory, (TileEntityFurnitureContainer) tile);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;
        ItemStack heldItem = player.getHeldItemMainhand();
        if (ID == ID_CLOTHS_WARDROBE)
        {
            return new GuiClothes(player);
        } else if (ID == ID_ACCESSORIES)
        {
            return new GuiArmorInventory(player);
        } else if (ID == ID_KEYRING_INV)
        {
            if (heldItem.getItem() instanceof ItemKeyring)
            {
                InventoryKeyRing inventory = new InventoryKeyRing(heldItem);
                return new GuiKeyring(player, inventory);
            }
        } else if (ID == ID_PURSE_INV)
        {
            if (heldItem.getItem() instanceof ItemPurse)
            {
                InventoryPurse inventory = new InventoryPurse(heldItem);
                return new GuiPurse(player, inventory);
            }
        } else if (ID == ID_QUIVER)
        {
            ItemStack quiver = Keldaria.getInstance().getSyncedAccessories().getAccessories(player).getInventory().getAccessory(EntityEquipmentSlot.CHEST);
            if (quiver.getItem() instanceof ItemQuiver)
            {
                InventoryQuiver inventory = new InventoryQuiver(player, heldItem);
                return new GuiQuiver(player, inventory);
            }
        } else if (ID == ID_FLAMBADOU)
        {
            return new GuiFlambadou(player);
        } else if (ID == OPEN_CARGO_CHEST)
        {
            return new GuiChest(player.inventory, ((EntityCargoCart) world.getEntityByID(x)).inventory);
        } else if (ID == ID_HORSE_STORAGE)
        {
            if (player.isRiding())
            {
                if (player.getRidingEntity() instanceof AbstractHorse)
                {
                    ILivingHorse livingHorse = (ILivingHorse) player.getRidingEntity();
                    return new GuiHorseStorage(player, livingHorse.getClientHorseStorage());
                }
            }
        } else if (ID == OPEN_PLOW_CART)
        {
            EntityPlowCart plow = (EntityPlowCart) world.getEntityByID(x);
            return new GuiPlow(player.inventory, plow.inventory, plow, player);
        }
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile instanceof TileEntityCookingFurnace)
        {
            return new GuiCookingFurnace(player.inventory, (TileEntityCookingFurnace) tile);
        } else if (tile instanceof TileEntityMill)
        {
            return new GuiMill((TileEntityMill) tile, player.inventory);
        } else if (tile instanceof TileEntityBeerBarrel)
        {
            return new GuiBeerBarrel((TileEntityBeerBarrel) tile, player.inventory);
        } else if (tile instanceof TileEntitySaltBag)
        {
            return new GuiSaltBag(player, (TileEntitySaltBag) tile);
        } else if (tile instanceof TileEntityHRPCarpet)
        {
            return new GuiHRPCarpet(player, (TileEntityHRPCarpet) tile);
        } else if (tile instanceof TileEntityClothingMannequin)
        {
            return new GuiClothingMannequin(player, (TileEntityClothingMannequin) tile);
        } else if (tile instanceof TileEntityFurnitureContainer)
        {
            return new GuiFurnitureContainer(player, (TileEntityFurnitureContainer) tile);
        }
        return null;
    }
}
