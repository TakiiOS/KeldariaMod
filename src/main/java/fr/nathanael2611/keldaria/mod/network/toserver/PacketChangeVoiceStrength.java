package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeVoiceStrength implements IMessage
{

    private int strength = 15;

    public PacketChangeVoiceStrength()
    {
    }

    public PacketChangeVoiceStrength(int strength)
    {
        this.strength = strength;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.strength = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.strength);
    }

    public static class Message implements IMessageHandler<PacketChangeVoiceStrength, IMessage>
    {
        @Override
        public IMessage onMessage(PacketChangeVoiceStrength message, MessageContext ctx)
        {
            if(message.strength >= 2 && message.strength <= 30)
            {
                KeldariaVoices.setSpeakStrength(ctx.getServerHandler().player, message.strength);
            }
            return null;
        }
    }
}
