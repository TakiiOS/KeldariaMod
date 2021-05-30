/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.canvas;

import fr.nathanael2611.keldaria.mod.client.gui.GuiCanvas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenCanvasGui implements IMessage
{

    private NBTTagCompound canvasNBT;
    private boolean isSigned;

    public PacketOpenCanvasGui()
    {
    }

    public PacketOpenCanvasGui(NBTTagCompound canvasNBT, boolean isSigned)
    {
        this.canvasNBT = canvasNBT;
        this.isSigned = isSigned;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.canvasNBT = ByteBufUtils.readTag(buf);
        this.isSigned = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.canvasNBT);
        buf.writeBoolean(this.isSigned);
    }

    public static class Message implements IMessageHandler<PacketOpenCanvasGui, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketOpenCanvasGui message, MessageContext ctx)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiCanvas(message.canvasNBT, message.isSigned));
            return null;
        }
    }
}
