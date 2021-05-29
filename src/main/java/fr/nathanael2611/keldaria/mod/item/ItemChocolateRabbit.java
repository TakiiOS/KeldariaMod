package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChocolateRabbit extends ItemFoodBase
{
    public ItemChocolateRabbit()
    {
        super(5, 6, false);
        this.setCreativeTab(KeldariaTabs.COOKING);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        entityLiving.addPotionEffect(new PotionEffect(MobEffects.SPEED, 40*20, 3));
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("§7Vous donne §cspeed III§7 pendant 40 secondes.");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}