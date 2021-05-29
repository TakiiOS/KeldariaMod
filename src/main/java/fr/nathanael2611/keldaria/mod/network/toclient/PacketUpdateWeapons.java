package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.Keldaria;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateWeapons implements IMessage
{

    public PacketUpdateWeapons()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        Keldaria.getInstance().getWeaponsManager().deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, Keldaria.getInstance().getWeaponsManager().serializeNBT());
    }

    public static class Handler implements IMessageHandler<PacketUpdateWeapons, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketUpdateWeapons message, MessageContext ctx)
        {
            //WeaponsManager manager = Keldaria.getInstance().getWeaponsManager();
            //manager.playerClothes.forEach((s, clothes) -> CustomSkins.FORCE_REFRESH.put(s, true));
            return null;
        }
    }
}