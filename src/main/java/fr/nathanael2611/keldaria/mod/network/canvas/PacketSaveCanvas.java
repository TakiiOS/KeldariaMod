/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.canvas;

import fr.nathanael2611.keldaria.mod.features.canvas.CanvasContent;
import fr.nathanael2611.keldaria.mod.item.ItemCanvas;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSaveCanvas implements IMessage
{

    private NBTTagCompound transferCompound = new NBTTagCompound();
    private boolean sign = false;
    private String title = "Canvas Signé";

    public PacketSaveCanvas()
    {
    }

    public PacketSaveCanvas(CanvasContent canvasContent, boolean sign)
    {
        this.transferCompound.setString(ItemCanvas.NBT_PIXELS_ID, canvasContent.toString());
        this.sign = sign;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.transferCompound = ByteBufUtils.readTag(buf);
        this.sign = buf.readBoolean();
        this.title = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.transferCompound);
        buf.writeBoolean(this.sign);
        ByteBufUtils.writeUTF8String(buf, this.title);
    }

    public void setTitle(String title)
    {
        if(this.sign) this.title = title;
    }

    public static class Message implements IMessageHandler<PacketSaveCanvas, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSaveCanvas message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if(player.getHeldItemMainhand().getItem() instanceof ItemCanvas)
            {
                ItemCanvas.setPixels(player.getHeldItemMainhand(), message.transferCompound.getString(ItemCanvas.NBT_PIXELS_ID));
                if(message.sign)
                {
                    ItemCanvas.sign(player.getHeldItemMainhand(), message.title, RolePlayNames.getName(player) + " (" + player.getName() + ")");
                }
            }
            return null;
        }
    }

}