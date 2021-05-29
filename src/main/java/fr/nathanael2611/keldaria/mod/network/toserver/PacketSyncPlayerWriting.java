package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.ChatBubblesManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPlayerWriting implements IMessage
{

    private String playerName;
    private boolean writing;

    public PacketSyncPlayerWriting()
    {
    }

    public PacketSyncPlayerWriting(String playerName, boolean writing)
    {
        this.playerName = playerName;
        this.writing = writing;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.writing = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
        buf.writeBoolean(this.writing);
    }

    public static class Message implements IMessageHandler<PacketSyncPlayerWriting, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSyncPlayerWriting message, MessageContext ctx)
        {
            ChatBubblesManager manager = ChatBubblesManager.getInstance();
            String playerName = message.playerName;
            manager.setWriting(playerName, message.writing);
            return null;
        }
    }

}
