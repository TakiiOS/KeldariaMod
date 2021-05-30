/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.CustomSkins;
import fr.nathanael2611.keldaria.mod.clothe.ClothesManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendClothes implements IMessage
{

    public PacketSendClothes()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        Keldaria.getInstance().getClothesManager().deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, Keldaria.getInstance().getClothesManager().serializeNBT());
    }

    public static class Handler implements IMessageHandler<PacketSendClothes, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSendClothes message, MessageContext ctx)
        {
            ClothesManager manager = Keldaria.getInstance().getClothesManager();
            manager.playerClothes.forEach((s, clothes) -> CustomSkins.FORCE_REFRESH.put(s, true));
            return null;
        }
    }
}
