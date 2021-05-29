package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Keldaria.MOD_ID)
public class ItemChop extends ItemFreshWater
{

    private int thirst;
    private int hydration;
    private PotionEffect effect;

    public ItemChop(int thirst, int hydratation, PotionEffect effect)
    {
        this.thirst = thirst;
        this.hydration = hydratation;
        this.effect = effect;
        setCreativeTab(KeldariaTabs.KELDARIA);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (!world.isRemote && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            //IThirst thirst = ThirstHelper.getThirstData(player);

            if (!player.capabilities.isCreativeMode) stack.shrink(1);

            //thirst.addStats(this.thirst, this.hydration);

            if(this.effect != null)
            {
                player.addPotionEffect(new PotionEffect(this.effect.getPotion(), this.effect.getDuration(), this.effect.getAmplifier()));
            }

            if (!player.capabilities.isCreativeMode)
            {
                if (stack.isEmpty()) return new ItemStack(KeldariaItems.EMPTY_CHOP);
            }
        }
        return stack;
    }

}
