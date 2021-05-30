/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.api.IHasClothe;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSleepingMask extends ItemAccessory implements IHasClothe
{

    public ItemSleepingMask()
    {
        setMaxStackSize(8);
    }

    @Override
    public EntityEquipmentSlot getRequiredEquipmentSlot()
    {
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    public void use(EntityPlayer player)
    {

    }

    @Override
    public String getDefaultClothURL()
    {
        return "http://keldaria.fr/skinhosting/sleeping_mask.png";
    }
}
