/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSpyRequest;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpyReply implements IMessage
{

    private int id;
    private String requester;
    private ITextComponent component;

    public PacketSpyReply(int id, String requester, ITextComponent component)
    {
        this.id = id;
        this.requester = requester;
        this.component = component;
    }

    public PacketSpyReply()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.requester = ByteBufUtils.readUTF8String(buf);
        this.component = Helpers.readTextComponent(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.requester);
        Helpers.writeTextComponent(buf, this.component);
    }


    public static class Message implements IMessageHandler<PacketSpyReply, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSpyReply message, MessageContext ctx)
        {
            EntityPlayerMP playerMP = Helpers.getPlayerByUsername(message.requester);
            if(playerMP != null)
            {
                playerMP.sendMessage(message.component);
            }
            PlayerSpy.SPY_LOG.log(
                    String.format(
                            "[**%s**] Requested __%s__.\n**Returned:**\n%s",
                            playerMP != null ? playerMP.getName() : "Console",
                            message.id,
                            Helpers.toRawUnformattedString(message.component)
                    )
            );
            return null;
        }
    }
}
