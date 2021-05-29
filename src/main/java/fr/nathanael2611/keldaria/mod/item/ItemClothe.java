package fr.nathanael2611.keldaria.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemClothe extends Item
{

    public ItemClothe()
    {

        this.setMaxStackSize(1);
    }

    public static boolean isClothValid(ItemStack stack)
    {
        if(stack.hasTagCompound())
        {
            NBTTagCompound compound = stack.getTagCompound();
            return compound.hasKey("ClothUrl", 8);
        }
        return false;
    }

    public static String getClothURL(ItemStack stack)
    {
        return isClothValid(stack) ? stack.getTagCompound().getString("ClothUrl") : "";
    }

    public static void setClothURL(ItemStack stack, String url)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) compound = new NBTTagCompound();
        compound.setString("ClothUrl", url);
        stack.setTagCompound(compound);
    }

}
