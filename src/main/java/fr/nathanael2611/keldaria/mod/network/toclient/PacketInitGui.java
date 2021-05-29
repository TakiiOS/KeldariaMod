package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketInitGui implements IMessage
{

    public PacketInitGui()
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

    public static class Message implements IMessageHandler<PacketInitGui, IMessage>
    {
        @Override
        public IMessage onMessage(PacketInitGui message, MessageContext ctx)
        {
            if (Minecraft.getMinecraft().currentScreen != null)
            {
                ClientProxy.executeAfter(() -> Minecraft.getMinecraft().currentScreen.initGui(), 100);
            }
            return null;
        }
    }
}
