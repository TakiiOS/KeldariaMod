package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityHRPSign;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateHRPSign implements IMessage
{

    private BlockPos pos;
    private String text;
    private int color;

    public PacketUpdateHRPSign()
    {
    }

    public PacketUpdateHRPSign(BlockPos pos, String text, int color)
    {
        this.pos = pos;
        this.text = text;
        this.color = color;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = Helpers.parseBlockPosFromString(ByteBufUtils.readUTF8String(buf));
        this.text = ByteBufUtils.readUTF8String(buf);
        this.color = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, Helpers.blockPosToString(this.pos));
        ByteBufUtils.writeUTF8String(buf, this.text);
        buf.writeInt(this.color);
    }

    public static class Message implements IMessageHandler<PacketUpdateHRPSign, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateHRPSign message, MessageContext ctx)
        {
            World world = ctx.getServerHandler().player.world;
            TileEntity te = world.getTileEntity(message.pos);
            if(te instanceof TileEntityHRPSign)
            {
                ((TileEntityHRPSign) te).updateText(message.text, message.color);
            }
            return null;
        }
    }

}
