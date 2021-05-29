package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenHorseStorage implements IMessage
{

    public PacketOpenHorseStorage()
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

    public static class Message implements IMessageHandler<PacketOpenHorseStorage, IMessage>
    {
        @Override
        public IMessage onMessage(PacketOpenHorseStorage message, MessageContext ctx)
        {
            if(ctx.getServerHandler().player.isRiding())
            {
                if(ctx.getServerHandler().player.getRidingEntity() instanceof AbstractHorse)
                {
                    FMLNetworkHandler.openGui(ctx.getServerHandler().player, Keldaria.getInstance(),
                            KeldariaGuiHandler.ID_HORSE_STORAGE,
                            ctx.getServerHandler().player.getEntityWorld(),
                            0, 0, 0);
                }
            }
            return null;
        }
    }
}