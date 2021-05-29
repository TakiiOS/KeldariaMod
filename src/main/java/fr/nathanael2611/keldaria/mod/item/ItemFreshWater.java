package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.thirst.Thirst;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFreshWater extends Item
{

    public ItemFreshWater()
    {
        this.setMaxStackSize(4);
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }


    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (!world.isRemote && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;

            Thirst.getThirst(player).drink(9, 5);
            if (!player.capabilities.isCreativeMode)
            {
                stack.shrink(1);
                player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return stack;
    }

}
