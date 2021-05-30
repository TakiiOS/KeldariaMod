/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendLog implements IMessage
{

    private String channelId;
    private String log;

    public PacketSendLog(PlayerSpy channel, String log)
    {
        this.channelId = channel.getChannelId();
        this.log = log;
    }

    public PacketSendLog()
    {
    }

    public String getChannelId()
    {
        return channelId;
    }

    public String getLog()
    {
        return log;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.channelId = ByteBufUtils.readUTF8String(buf);
        this.log = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.channelId);
        ByteBufUtils.writeUTF8String(buf, this.log);
    }

    public static class Message implements IMessageHandler<PacketSendLog, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSendLog message, MessageContext ctx)
        {
            PlayerSpy.fromClient(message);
            return null;
        }
    }
}
