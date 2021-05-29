package fr.nathanael2611.keldaria.mod.network.toclient;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class PacketCopyStringToClipboard implements IMessage
{

    private String strToCopy;

    public PacketCopyStringToClipboard()
    {}

    public PacketCopyStringToClipboard(String strToCopy)
    {
        this.strToCopy = strToCopy;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.strToCopy = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.strToCopy);
    }

    public static class Message implements IMessageHandler<PacketCopyStringToClipboard, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketCopyStringToClipboard message, MessageContext ctx)
        {
            StringSelection stringSelection = new StringSelection(message.strToCopy);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            return null;
        }
    }
}
