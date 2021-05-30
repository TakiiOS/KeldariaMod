/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketProcessEmptyRightClick implements IMessage
{

    public PacketProcessEmptyRightClick()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    public static class Message implements IMessageHandler<PacketProcessEmptyRightClick, IMessage>
    {
        @Override
        public IMessage onMessage(PacketProcessEmptyRightClick message, MessageContext ctx)
        {
            net.minecraftforge.common.ForgeHooks.onEmptyClick(ctx.getServerHandler().player, EnumHand.MAIN_HAND);
            return null;
        }
    }

}