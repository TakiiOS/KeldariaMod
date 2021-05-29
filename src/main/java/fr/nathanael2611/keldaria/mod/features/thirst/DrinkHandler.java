package fr.nathanael2611.keldaria.mod.features.thirst;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DrinkHandler
{

    @SubscribeEvent
    public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (event.getWorld().isRaining())
        {
            if(event.getEntityPlayer().rotationPitch > 90)
            {
            }
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        if(world.isRemote) return;
        if(Thirst.getThirst(player).isThirsty() && player.isSneaking() && world.getBlockState(event.getPos().up()).getBlock() == Blocks.WATER)
        {
            String biomeName = event.getWorld().getBiome(event.getPos()).getBiomeName();
            if(biomeName.toLowerCase().contains("ocean") || biomeName.toLowerCase().contains("beach"))
            {
                player.attackEntityFrom(DamageSource.GENERIC, 1);
                Helpers.sendPopMessage((EntityPlayerMP) player, "§cL'eau est très salée et désagréable.", 2000);
            }

            Thirst.getThirst(player).drink(3, 2);
        }
    }

    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving().world.isRemote)
        {
            return;
        }
        ItemStack drank = event.getItem();
        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            if(drank.getItem() == Items.POTIONITEM)
            {
                if(PotionUtils.getFullEffectsFromItem(drank).isEmpty())
                {
                    Thirst.getThirst((EntityPlayer) event.getEntityLiving()).drink(5, 2);
                }
            }
        }
    }

}
