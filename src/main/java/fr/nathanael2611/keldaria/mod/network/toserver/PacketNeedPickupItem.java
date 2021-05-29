package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.handler.KeldariaEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketNeedPickupItem implements IMessage
{

    private int entityId = 0;

    public PacketNeedPickupItem()
    {
    }

    public PacketNeedPickupItem(int entityId)
    {
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
    }

    public static class Message implements IMessageHandler<PacketNeedPickupItem, IMessage>
    {
        @Override
        public IMessage onMessage(PacketNeedPickupItem message, MessageContext ctx)
        {
            KeldariaEventHandler.PICKUP_NEEDED.put(ctx.getServerHandler().player.getName(), message.entityId);
            Entity entity = ctx.getServerHandler().player.world.getEntityByID(message.entityId);
            if(entity instanceof EntityItem)
            {
                ((EntityItem) entity).setNoPickupDelay();
            }
            return null;
        }
    }
}
