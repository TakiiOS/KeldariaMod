package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.rot.capability.Rot;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemFood.class)
public abstract class MixinItemFood extends Item
{

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        Rot rot = ExpiredFoods.getRot(stack);
        if (rot.getCreatedDay() == 0)
        {
            ExpiredFoods.create(stack);
        }
        if(rot.isInBag())
        {
            rot.extractFromBag();
        }
    }

}
