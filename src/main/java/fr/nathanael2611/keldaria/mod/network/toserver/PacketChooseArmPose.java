/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.armoposes.Arms;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChooseArmPose implements IMessage
{

    public static final int REMOVE = 0;
    public static final int SET = 1;

    private int id;
    private Arms pose;

    public PacketChooseArmPose(int id, Arms pose)
    {
        this.id = id;
        this.pose = pose;
    }

    public PacketChooseArmPose()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        if(this.id == SET)
        {
            this.pose = new Arms(ByteBufUtils.readTag(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        if(id == SET)
        {
            ByteBufUtils.writeTag(buf, this.pose.serializeNBT());
        }
    }

    public static class Message implements IMessageHandler<PacketChooseArmPose, IMessage>
    {
        @Override
        public IMessage onMessage(PacketChooseArmPose message, MessageContext ctx)
        {
            if(message.id == REMOVE)
            {
                Keldaria.getInstance().getArmPoses().resetPose(ctx.getServerHandler().player);
            }
            else if(message.pose != null)
            {
                Keldaria.getInstance().getArmPoses().setPose(ctx.getServerHandler().player, message.pose);
            }
            return null;
        }
    }
}
