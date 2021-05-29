package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketActionHomingPigeon implements IMessage
{

    private int pigeonId = 0;
    private int location = 1;
    private String newName = "";

    public PacketActionHomingPigeon()
    {
    }

    public PacketActionHomingPigeon(int pigeonId, int location, String newName)
    {
        this.pigeonId = pigeonId;
        this.location = location;
        this.newName = newName;
    }

    public PacketActionHomingPigeon(int pigeonId, int location)
    {
        this(pigeonId, location, "");
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pigeonId = buf.readInt();
        this.location = buf.readInt();
        this.newName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pigeonId);
        buf.writeInt(this.location);
        ByteBufUtils.writeUTF8String(buf, this.newName);
    }

    public static class Handler implements IMessageHandler<PacketActionHomingPigeon, IMessage>
    {
        @Override
        public IMessage onMessage(PacketActionHomingPigeon message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            World world = player.world;
            Entity entity = world.getEntityByID(message.pigeonId);
            if (entity instanceof EntityHomingPigeon)
            {
                EntityHomingPigeon pigeon = (EntityHomingPigeon) entity;
                if (message.newName.length() > 0)
                {
                    EntityHomingPigeon.PigeonLocation loc = new EntityHomingPigeon.PigeonLocation(message.newName, player.getPosition().up());
                    if (message.location == 1)
                    {
                        pigeon.setLoc1(loc);
                    } else if (message.location == 2)
                    {
                        pigeon.setLoc2(loc);
                    }
                } else
                {
                    if (message.location == 1)
                    {
                        pigeon.setDestination(pigeon.getLoc1());
                    } else if (message.location == 2)
                    {
                        pigeon.setDestination(pigeon.getLoc2());
                    }
                }
            } return null;
        }
    }

}
