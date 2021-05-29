package fr.nathanael2611.keldaria.mod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.Date;

public class ItemIgnitedArrow extends ItemArrow
{

    public static final String KEY = "IgnitedDate";

    public ItemIgnitedArrow()
    {
        this.setMaxStackSize(1);
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        EntityArrow arrow = super.createArrow(worldIn, stack, shooter);
        arrow.setFire((int) ((new Date().getTime() - getIgnitDate(stack)) / 1000));
        return arrow;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!isIgnited(stack))
        {
            stack.shrink(1);
            entityIn.replaceItemInInventory(itemSlot, new ItemStack(Items.ARROW));
        }
    }



    public static boolean isIgnited(ItemStack stack)
    {
        return new Date().getTime() - getIgnitDate(stack) < (1000 * 60);
    }

    public static long getIgnitDate(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            return stack.getTagCompound().hasKey(KEY) ? stack.getTagCompound().getLong(KEY) : 0;
        }
        return 0;
    }

    public static void setIgnitDate(ItemStack stack, long ticks)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        compound.setLong(KEY, ticks);
        stack.setTagCompound(compound);

    }

}
