/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.Keldaria;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateClimbSystem implements IMessage
{

    public PacketUpdateClimbSystem()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        NBTTagCompound compound = ByteBufUtils.readTag(buf);
        if (compound != null)
        {
            Keldaria.getInstance().getClimbSystem().deserializeNBT(compound);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, Keldaria.getInstance().getClimbSystem().serializeNBT());
    }

    public static class Handler implements IMessageHandler<PacketUpdateClimbSystem, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketUpdateClimbSystem message, MessageContext ctx)
        {
            return null;
        }
    }
}
