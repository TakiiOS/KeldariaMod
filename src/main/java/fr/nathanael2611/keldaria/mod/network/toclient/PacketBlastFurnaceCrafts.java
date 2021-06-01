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

public class PacketBlastFurnaceCrafts implements IMessage
{

    private NBTTagCompound blastFurnaceNBT;

    public PacketBlastFurnaceCrafts()
    {
        this.blastFurnaceNBT = Keldaria.getInstance().getRegistry().getBlastFurnace().serializeNBT();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.blastFurnaceNBT = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.blastFurnaceNBT);
    }

    public static class Handler implements IMessageHandler<PacketBlastFurnaceCrafts, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketBlastFurnaceCrafts message, MessageContext ctx)
        {
            Keldaria.getInstance().getRegistry().getBlastFurnace().deserializeNBT(message.blastFurnaceNBT);
            return null;
        }
    }

}
