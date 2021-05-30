/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.gui.GuiHomingPigeon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenHomingPigeon implements IMessage
{

    private int pigeonId;
    private String loc1;
    private String loc2;

    public PacketOpenHomingPigeon(int pigeonId, String loc1, String loc2)
    {
        this.pigeonId = pigeonId;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public PacketOpenHomingPigeon()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pigeonId = buf.readInt();
        this.loc1 = ByteBufUtils.readUTF8String(buf);
        this.loc2 = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pigeonId);
        ByteBufUtils.writeUTF8String(buf, this.loc1);
        ByteBufUtils.writeUTF8String(buf, this.loc2);
    }

    public static class Handler implements IMessageHandler<PacketOpenHomingPigeon, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketOpenHomingPigeon message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiHomingPigeon(message.pigeonId, message.loc1, message.loc2).getGuiScreen()));
            return null;
        }
    }
}
