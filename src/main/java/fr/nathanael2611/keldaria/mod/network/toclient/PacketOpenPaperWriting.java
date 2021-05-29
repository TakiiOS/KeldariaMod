package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.gui.GuiWritablePaper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenPaperWriting implements IMessage
{

    private String title, content;
    private boolean isSigned;

    public PacketOpenPaperWriting()
    {
    }

    public PacketOpenPaperWriting(String content)
    {
        this("", content, false);
    }

    public PacketOpenPaperWriting(String title, String content, boolean isSigned)
    {
        this.title = title;
        this.content = content;
        this.isSigned = isSigned;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.title = ByteBufUtils.readUTF8String(buf);
        this.content = ByteBufUtils.readUTF8String(buf);
        this.isSigned = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.title);
        ByteBufUtils.writeUTF8String(buf, this.content);
        buf.writeBoolean(this.isSigned);
    }

    public static class Message implements IMessageHandler<PacketOpenPaperWriting, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketOpenPaperWriting message, MessageContext ctx)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiWritablePaper(message.title, message.content, message.isSigned));
            return null;
        }
    }

}
