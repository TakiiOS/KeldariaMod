package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.entity.animal.KeldaChicken;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFertilizedEgg extends Item
{

    public ItemFertilizedEgg()
    {
        setMaxStackSize(1);
    }

    public ItemStack from(KeldaChicken.KeldaEgg egg)
    {
        ItemStack stack = new ItemStack(this);
        setEgg(stack, egg);
        return stack;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);

        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }

        KeldaChicken.KeldaEgg egg = getEggEntity(stack, worldIn);
        if(egg != null)
        {
            egg.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5);
            worldIn.spawnEntity(egg);
        }

        stack.shrink(1);
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    public static KeldaChicken.KeldaEgg getEggEntity(ItemStack stack, World world)
    {
        NBTTagCompound compound = Helpers.getCompoundTag(stack).getCompoundTag("Egg");
        Entity entity = EntityList.createEntityFromNBT(compound, world);
        System.out.println(compound);
        if(entity instanceof KeldaChicken.KeldaEgg)
        {
            return (KeldaChicken.KeldaEgg) entity;
        }
        return null;
    }

    public static void setEgg(ItemStack stack, KeldaChicken.KeldaEgg egg)
    {
        NBTTagCompound compound = new NBTTagCompound();
        egg.writeToNBTAtomically(compound);
        NBTTagCompound c = Helpers.getCompoundTag(stack);
        c.setTag("Egg", compound);
        stack.setTagCompound(c);
    }


}
