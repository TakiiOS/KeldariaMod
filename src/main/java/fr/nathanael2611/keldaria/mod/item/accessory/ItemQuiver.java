/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item.accessory;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ItemQuiver extends ItemAccessory
{

    public ItemQuiver()
    {
        setMaxStackSize(1);
        setCreativeTab(KeldariaTabs.KELDARIA);
    }

    @Override
    public EntityEquipmentSlot getRequiredEquipmentSlot()
    {
        return EntityEquipmentSlot.CHEST;
    }

    @Override
    public void use(EntityPlayer player)
    {
        if(player.world.isRemote) return;
        player.openGui(Keldaria.getInstance(), KeldariaGuiHandler.ID_QUIVER, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    public static boolean isEmpty(EntityPlayer owner, ItemStack quiver)
    {
        return getArrowCount(owner, quiver) == 0;
    }

    public static int getArrowCount(EntityPlayer owner, ItemStack quiver)
    {
        if(quiver.getItem() instanceof ItemQuiver)
        {
            InventoryQuiver inventoryQuiver = new InventoryQuiver(owner, quiver);
            int count = 0;
            for (int i = 0; i < inventoryQuiver.getSizeInventory(); i++)
            {
                count += inventoryQuiver.getStackInSlot(i).getCount();
            }
            return count;
        }
        return 0;
    }

}
