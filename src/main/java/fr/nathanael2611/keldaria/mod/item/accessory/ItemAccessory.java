/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item.accessory;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public abstract class ItemAccessory extends Item
{

    public ItemAccessory()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

    public abstract EntityEquipmentSlot getRequiredEquipmentSlot();

    public abstract void use(EntityPlayer player);

}
