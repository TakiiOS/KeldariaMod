/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.Keldaria;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class PacketSetArms implements IMessage
{

    public PacketSetArms()
    {

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        Keldaria.getInstance().getArmPoses().deserializeNBT(Objects.requireNonNull(ByteBufUtils.readTag(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, Keldaria.getInstance().getArmPoses().serializeNBT());
    }

    public static class Message implements IMessageHandler<PacketSetArms, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSetArms message, MessageContext ctx)
        {
            return null;
        }
    }

}
