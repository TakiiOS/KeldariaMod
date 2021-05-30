/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.features.writable.FatBook;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenGui;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFatBook extends Item
{

    public static FatBook getFatBook(ItemStack stack)
    {
        FatBook book = new FatBook();
        NBTTagCompound compound = getCompoundTag(stack);
        if(compound.hasKey("FatBook"))
        {
            book.deserializeNBT(compound.getCompoundTag("FatBook"));
        }
        return book;
    }

    public static void setFatBook(ItemStack stack, FatBook book)
    {
        getCompoundTag(stack).setTag("FatBook", book.serializeNBT());
    }

    public static NBTTagCompound getCompoundTag(ItemStack stack)
    {
        if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if(worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        if(handIn == EnumHand.MAIN_HAND)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenGui(PacketOpenGui.FAT_BOOK), Helpers.getPlayerMP(playerIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        FatBook book = getFatBook(stack);
        return book.isSigned() ? book.getTitle() : "Livre obèse";
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return getFatBook(stack).isSigned();
    }
}
