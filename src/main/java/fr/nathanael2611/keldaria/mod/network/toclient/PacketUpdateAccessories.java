package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.Keldaria;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateAccessories implements IMessage
{

    public PacketUpdateAccessories()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        Keldaria.getInstance().getSyncedAccessories().deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, Keldaria.getInstance().getSyncedAccessories().serializeNBT());
    }

    public static class Handler implements IMessageHandler<PacketUpdateAccessories, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketUpdateAccessories message, MessageContext ctx)
        {
            return null;
        }
    }
}