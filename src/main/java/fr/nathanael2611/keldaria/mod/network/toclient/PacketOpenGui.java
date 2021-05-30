/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.gui.GuiFatBook;
import fr.nathanael2611.keldaria.mod.client.gui.GuiHRPSignEdit;
import fr.nathanael2611.keldaria.mod.client.gui.GuiLockpick;
import fr.nathanael2611.keldaria.mod.client.gui.GuiSkinChooser;
import fr.nathanael2611.keldaria.mod.item.ItemFatBook;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.reden.guiapi.component.panel.GuiFrame;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenGui implements IMessage
{

    public static final int NULL = 0;
    public static final int LOCKPICK = 1;
    public static final int SKINCHOOSER = 2;
    public static final int HRP_SIGN_EDIT = 4;
    public static final int FAT_BOOK = 5;

    private int guiId;
    private String args;

    public PacketOpenGui()
    {
    }

    public PacketOpenGui(int guiId)
    {
        this(guiId, "");
    }

    public PacketOpenGui(int guiId, String args)
    {
        this.guiId = guiId;
        this.args = args;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.guiId = buf.readInt();
        this.args = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.guiId);
        ByteBufUtils.writeUTF8String(buf, this.args);
    }

    public static class Message implements IMessageHandler<PacketOpenGui, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx)
        {
            if(message.guiId == LOCKPICK)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiLockpick(message.args));
            }
            else if(message.guiId == SKINCHOOSER)
            {
                //GuiScreen screen = new GuiSkinChooser().getGuiScreen();
                //Minecraft.getMinecraft().displayGuiScreen(screen);
                if(Minecraft.getMinecraft().currentScreen instanceof GuiFrame.APIGuiScreen)
                {
                    GuiFrame.APIGuiScreen screen = (GuiFrame.APIGuiScreen) Minecraft.getMinecraft().currentScreen;
                    if(screen.getFrame() instanceof GuiSkinChooser)
                    {
                        ((GuiSkinChooser) screen.getFrame()).needRefresh = true;
                    }
                }
            }
            else if(message.guiId == HRP_SIGN_EDIT)
            {
                BlockPos signPos = Helpers.parseBlockPosFromString(message.args);
                Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiHRPSignEdit(signPos).getGuiScreen()));
            }
            else if(message.guiId == FAT_BOOK)
            {
                Minecraft.getMinecraft().addScheduledTask(() ->
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiFatBook(ItemFatBook.getFatBook(Minecraft.getMinecraft().player.getHeldItemMainhand())).getGuiScreen());
                });
            }
            return null;
        }
    }
}
