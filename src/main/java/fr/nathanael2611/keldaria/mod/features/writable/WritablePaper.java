package fr.nathanael2611.keldaria.mod.features.writable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class WritablePaper
{

    public static void sign(ItemStack stack, String title, String authorName)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        compound.setString("Title", title);
        compound.setString("Author", authorName);
        stack.setTagCompound(compound);
    }

    public static boolean isSigned(ItemStack stack)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        return compound.hasKey("Author", Constants.NBT.TAG_STRING) && compound.hasKey("Title", Constants.NBT.TAG_STRING);
    }

    public static void write(ItemStack stack, String content)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        compound.setString("PaperContent", content);
        stack.setTagCompound(compound);
    }

    public static String getContent(ItemStack stack)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        return compound.hasKey("PaperContent", Constants.NBT.TAG_STRING) ? compound.getString("PaperContent") : "";
    }

    public static String getTitle(ItemStack stack)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        return compound.hasKey("Title", Constants.NBT.TAG_STRING) ? compound.getString("Title") : "";
    }

    public static String getAuthor(ItemStack stack)
    {
        NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        return compound.hasKey("Author", Constants.NBT.TAG_STRING) ? compound.getString("Author") : "";
    }

}
