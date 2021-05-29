package fr.nathanael2611.keldaria.mod.network.animation;


import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Used send a packet to server to handle a animationName animation
 *
 * @author Nathanael2611
 */
public class PacketHandleAnimationResponse implements IMessage
{

    private String animatedPlayerName;
    private String animationName;

    public PacketHandleAnimationResponse()
    {
    }

    public PacketHandleAnimationResponse(String animatedPlayerName, String animationName)
    {
        this.animatedPlayerName = animatedPlayerName;
        this.animationName = animationName;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.animatedPlayerName = ByteBufUtils.readUTF8String(buf);
        this.animationName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, animatedPlayerName);
        ByteBufUtils.writeUTF8String(buf, animationName);
    }


    public static class Message implements IMessageHandler<PacketHandleAnimationResponse, IMessage>
    {
        @Override
        public IMessage onMessage(PacketHandleAnimationResponse message, MessageContext ctx)
        {
            AnimationUtils.setPlayerHandledAnimation(message.animatedPlayerName, AnimationUtils.createAnimationFromStaticInstance(AnimationUtils.animations.get(message.animationName)));
            return null;
        }
    }
}