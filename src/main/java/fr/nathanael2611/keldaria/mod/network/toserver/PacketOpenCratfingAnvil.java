package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenCratfingAnvil implements IMessage
{

    public PacketOpenCratfingAnvil()
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

    public static class Message implements IMessageHandler<PacketOpenCratfingAnvil, IMessage>
    {
        @Override
        public IMessage onMessage(PacketOpenCratfingAnvil message, MessageContext ctx)
        {
            CraftManager.openManager("blacksmith", ctx.getServerHandler().player);
            //FMLNetworkHandler.openGui(ctx.getServerHandler().player, Keldaria.getInstance(), KeldariaGuiHandler.ID_CLOTHS_WARDROBE, ctx.getServerHandler().player.getEntityWorld(), 0, 0, 0);
            return null;
        }
    }
}