package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.util.spy.ModsAndPacks;
import fr.nathanael2611.keldaria.mod.util.spy.ScreenshotManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSpyRequest implements IMessage
{

    public static final int ID_SCREENSHOT = 0;
    public static final int ID_MODS = 1;
    public static final int ID_PACKS = 2;

    private int id;
    private String requester;

    public PacketSpyRequest(int id, String requester)
    {
        this.id = id;
        this.requester = requester;
    }

    public PacketSpyRequest()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.requester = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.requester);
    }

    public static class Message implements IMessageHandler<PacketSpyRequest, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSpyRequest message, MessageContext ctx)
        {
            if(message.id == ID_SCREENSHOT)
            {
                ScreenshotManager.silentScreenAndSend(message.requester);
            }
            else if(message.id == ID_MODS)
            {
                ModsAndPacks.collectAndSendMods(message.requester);
            }
            else if(message.id == ID_PACKS)
            {
                ModsAndPacks.collectAndSendPacks(message.requester);
            }
            return null;
        }
    }

}
