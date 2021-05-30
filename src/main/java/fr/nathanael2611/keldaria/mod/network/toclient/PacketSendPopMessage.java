/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.PopMessages;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendPopMessage implements IMessage {

    private String message;
    private int duration;

    public PacketSendPopMessage(){}
    public PacketSendPopMessage(String message, int duration){
        this.message = message;
        this.duration = duration;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.message  = ByteBufUtils.readUTF8String(buf);
        this.duration = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.message);
        buf.writeInt(this.duration);
    }

    public static class Message implements IMessageHandler<PacketSendPopMessage, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSendPopMessage message, MessageContext ctx)
        {
            PopMessages.add(message.message, message.duration);
            return null;
        }
    }
}
