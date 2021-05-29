package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemEmptyChop extends Item
{

    public ItemEmptyChop()
    {
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        this.setMaxStackSize(1);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) return new ActionResult(EnumActionResult.PASS, itemstack);

            if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
            {
                worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, PotionUtils.addPotionToItemStack(new ItemStack(KeldariaItems.WATER_CHOP), PotionTypes.WATER)));
            }
        }
        return new ActionResult(EnumActionResult.PASS, itemstack);
    }

    private ItemStack turnBottleIntoItem(ItemStack bottle, EntityPlayer player, ItemStack stack)
    {
        bottle.shrink(1);
        if (bottle.isEmpty()) return stack;
        else if (!player.inventory.addItemStackToInventory(stack)) player.dropItem(stack, false);
        return bottle;
    }

}