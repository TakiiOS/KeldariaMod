/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChangeRolePlayName implements IMessage
{

    private String name;

    public PacketChangeRolePlayName()
    {
    }

    public PacketChangeRolePlayName(String name)
    {
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.name);
    }

    public static class Message implements IMessageHandler<PacketChangeRolePlayName, IMessage>
    {
        @Override
        public IMessage onMessage(PacketChangeRolePlayName message, MessageContext ctx)
        {
            RolePlayNames.setName(ctx.getServerHandler().player, message.name);
            return null;
        }
    }
}
