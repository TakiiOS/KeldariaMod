/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.gui.GuiCraftingManager;
import fr.nathanael2611.keldaria.mod.crafting.PlayerCrafts;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenCraftManager implements IMessage
{

    private PlayerCrafts craftManager;

    public PacketOpenCraftManager()
    {
    }

    public PacketOpenCraftManager(PlayerCrafts craftManager)
    {
        this.craftManager = craftManager;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.craftManager = new PlayerCrafts();
        NBTTagCompound compound = ByteBufUtils.readTag(buf);
        this.craftManager.deserializeNBT(compound);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.craftManager.serializeNBT());
    }

    public static class Message implements IMessageHandler<PacketOpenCraftManager, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketOpenCraftManager message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
                    Minecraft.getMinecraft().displayGuiScreen(new GuiCraftingManager(message.craftManager).getGuiScreen())
            );
            return null;
        }
    }
}
