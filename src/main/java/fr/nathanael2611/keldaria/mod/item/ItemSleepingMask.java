package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSleepingMask extends ItemAccessory
{

    public ItemSleepingMask()
    {
        setMaxStackSize(8);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!ItemClothe.isClothValid(stack))
        {
            ItemClothe.setClothURL(stack, "http://keldaria.fr/skinhosting/sleeping_mask.png");
        }
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
}
