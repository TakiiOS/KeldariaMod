package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.canvas.CanvasContent;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.canvas.PacketOpenCanvasGui;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCanvas extends Item
{

    public static final String NBT_PIXELS_ID = "pixels";

    public ItemCanvas()
    {
        this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(!worldIn.isRemote)
        {
            EntityPlayerMP p = Helpers.getPlayerMP(playerIn);
            ItemStack canvas = playerIn.getHeldItemMainhand();
            if(!canvas.hasTagCompound()) canvas.setTagCompound(new NBTTagCompound());
            NBTTagCompound tagCompound = canvas.getTagCompound();
            if(tagCompound == null) tagCompound = new NBTTagCompound();
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenCanvasGui(tagCompound, isSigned(canvas)), p);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(!worldIn.isRemote)
        {
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null) compound = new NBTTagCompound();
            if(!compound.hasKey(NBT_PIXELS_ID) || compound.getString(NBT_PIXELS_ID).equalsIgnoreCase("")) compound.setString(NBT_PIXELS_ID, new CanvasContent("0").toString());
        }
    }

    public static boolean isSigned(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("title") && stack.getTagCompound().hasKey("author");
    }

    public static void setPixels(ItemStack stack, String pixels)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) compound = new NBTTagCompound();
        compound.setString(NBT_PIXELS_ID, pixels);
    }

    public static void sign(ItemStack stack, String title, String author)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null) compound = new NBTTagCompound();
        compound.setString("title", title);
        compound.setString("author", author);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if(tagCompound == null) tagCompound = new NBTTagCompound();
        if(isSigned(stack)) return tagCompound.getString("title");
        return super.getItemStackDisplayName(stack);
    }
}