package fr.nathanael2611.keldaria.mod.network.toserver;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDisplayInChatOf implements IMessage
{

    private String playerName;
    private String strToCopy;

    public PacketDisplayInChatOf()
    {}

    public PacketDisplayInChatOf(String playerName, String strToCopy)
    {
        this.playerName = playerName;
        this.strToCopy = strToCopy;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.strToCopy = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
        ByteBufUtils.writeUTF8String(buf, this.strToCopy);
    }

    public static class Message implements IMessageHandler<PacketDisplayInChatOf, IMessage>
    {
        @Override
        public IMessage onMessage(PacketDisplayInChatOf message, MessageContext ctx)
        {
            PlayerList list = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
            EntityPlayer p = list.getPlayerByUsername(message.playerName);
            if(p != null) p.sendMessage(new TextComponentString(message.strToCopy));
            return null;
        }
    }

}
