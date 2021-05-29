package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenAccessories implements IMessage
{

    public PacketOpenAccessories()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    public static class Message implements IMessageHandler<PacketOpenAccessories, IMessage>
    {
        @Override
        public IMessage onMessage(PacketOpenAccessories message, MessageContext ctx)
        {
            FMLNetworkHandler.openGui(ctx.getServerHandler().player, Keldaria.getInstance(), KeldariaGuiHandler.ID_ACCESSORIES, ctx.getServerHandler().player.getEntityWorld(), 0, 0, 0);
            return null;
        }
    }
}