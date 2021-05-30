/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBow.class)
public abstract class MixinItemBow extends Item
{

    @Shadow
    protected abstract boolean isArrow(ItemStack stack);

    /**
     * @author
     */
    @Overwrite
    protected ItemStack findAmmo(EntityPlayer player)
    {
        IKeldariaPlayer keldariaPlayer = (IKeldariaPlayer) player;

        ItemStack quiver = player.world.isRemote ? Keldaria.getInstance().getSyncedAccessories().getAccessories(player).getInventory().getAccessory(EntityEquipmentSlot.CHEST) : keldariaPlayer.getInventoryAccessories().getAccessory(EntityEquipmentSlot.CHEST);

        if (quiver.getItem() instanceof ItemQuiver)
        {
            InventoryQuiver inventoryQuiver = new InventoryQuiver(player, quiver);
            for (int i = 0; i < inventoryQuiver.getSizeInventory(); i++)
            {
                ItemStack itemstack = inventoryQuiver.getStackInSlot(i);
                if (isArrow(itemstack))
                {
                    ItemStack stack = itemstack.copy();
                    return stack;
                }
            }
        }

        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }


        for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (this.isArrow(itemstack))
            {
                return itemstack;
            }
        }
        return ItemStack.EMPTY;
    }

}
