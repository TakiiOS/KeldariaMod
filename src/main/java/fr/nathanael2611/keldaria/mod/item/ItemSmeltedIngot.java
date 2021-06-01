package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemSmeltedIngot extends Item
{

    private Item base;
    private int duration;

    public ItemSmeltedIngot(int duration, Item base)
    {
        this.base = base;
        this.duration = duration;

    }

    public Item getBase()
    {
        return base;
    }

    public int getDuration()
    {
        return duration;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn instanceof EntityPlayer)
        {
            if(!isDefined(stack))
            {
                define(stack);
            }
            int date = getSmeltTime(stack);
            if(getTime() - date >= duration)
            {
                int count = stack.getCount();
                stack.setCount(0);
                ((EntityPlayer) entityIn).addItemStackToInventory(new ItemStack(this.base, count));
            }
        }
    }

    public void define(ItemStack stack)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack);
        compound.setInteger("SmeltedTime", getTime());
        stack.setTagCompound(compound);
    }

    public boolean isDefined(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).hasKey("SmeltedTime");
    }

    public int getSmeltTime(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).getInteger("SmeltedTime");
    }

    public static int getTime()
    {
        return (int) ((System.currentTimeMillis() / 1000) / 60);
    }

}
