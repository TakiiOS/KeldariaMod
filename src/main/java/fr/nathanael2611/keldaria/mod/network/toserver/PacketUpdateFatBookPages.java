/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.writable.FatBook;
import fr.nathanael2611.keldaria.mod.item.ItemFatBook;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateFatBookPages implements IMessage
{

    private NBTTagCompound pages;

    public PacketUpdateFatBookPages()
    {
    }

    public PacketUpdateFatBookPages(NBTTagCompound pages)
    {
        this.pages = pages;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pages = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.pages);
    }

    public static class Message implements IMessageHandler<PacketUpdateFatBookPages, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateFatBookPages message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() == KeldariaItems.FAT_BOOK)
            {
                FatBook book = ItemFatBook.getFatBook(stack);
                book.incorporePages(message.pages);
                ItemFatBook.setFatBook(stack, book);
            }
            return null;
        }
    }

}
