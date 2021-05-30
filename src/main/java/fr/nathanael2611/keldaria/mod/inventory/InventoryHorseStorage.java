/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.inventory;

import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class InventoryHorseStorage extends InventoryBasic


{


    private AbstractHorse horse = null;

    public InventoryHorseStorage(AbstractHorse horse)
    {
        super("Horse Storage", false, 4);
        this.horse = horse;
        //addInventoryChangeListener(this.horse);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        if(horse.world.isRemote) return;
        horse.getDataManager().set(MixinHooks.HORSE_STORAGE_1, getStackInSlot(0));
        horse.getDataManager().set(MixinHooks.HORSE_STORAGE_2, getStackInSlot(1));
        horse.getDataManager().set(MixinHooks.HORSE_STORAGE_3, getStackInSlot(2));
        horse.getDataManager().set(MixinHooks.HORSE_STORAGE_4, getStackInSlot(3));
    }
}
