/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.ChatBubblesManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendWritingUpdate implements IMessage
{

    private boolean writing;

    public PacketSendWritingUpdate()
    {
    }

    public PacketSendWritingUpdate(boolean writing)
    {
        this.writing = writing;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.writing = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.writing);
    }

    public static class Message implements IMessageHandler<PacketSendWritingUpdate, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSendWritingUpdate message, MessageContext ctx)
        {
            ChatBubblesManager manager = ChatBubblesManager.getInstance();
            String playerName = ctx.getServerHandler().player.getName();
            manager.setWriting(playerName, message.writing, true);
            return null;
        }
    }

}
