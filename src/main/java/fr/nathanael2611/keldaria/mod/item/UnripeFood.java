package fr.nathanael2611.keldaria.mod.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class UnripeFood extends ItemFoodBase
{
    public UnripeFood(int i)
    {
        super(i, 0, false);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        entityLiving.attackEntityFrom(DamageSource.GENERIC, 1);
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60, 1));
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
