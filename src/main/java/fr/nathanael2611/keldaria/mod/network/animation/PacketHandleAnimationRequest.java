package fr.nathanael2611.keldaria.mod.network.animation;

import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSendPopMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Used send a packet to server to handle a animationName animation
 *
 * @author Nathanael2611
 */
public class PacketHandleAnimationRequest implements IMessage
{

    private String animationName;

    public PacketHandleAnimationRequest()
    {
    }

    public PacketHandleAnimationRequest(String animationName)
    {
        this.animationName = animationName;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.animationName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, animationName);
    }


    public static class Message implements IMessageHandler<PacketHandleAnimationRequest, IMessage>
    {
        @Override
        public IMessage onMessage(PacketHandleAnimationRequest message, MessageContext ctx)
        {
            if (!AnimationUtils.animations.containsKey(message.animationName)) return null;
            Animation animation = AnimationUtils.animations.get(message.animationName);

            EntityPlayerMP player = ctx.getServerHandler().player;

            float size = (float) PlayerSizes.get(player);
            AxisAlignedBB animationBounds = Animation.createHitboxByDimensions(player, animation.getWidth(size), animation.getHeight(size));
            if (player.isSneaking())
            {
                animationBounds = Animation.createHitboxByDimensions(player, animation.getWidth(size), animation.getSneakingHeight(size));
            }

            if (player.world.collidesWithAnyBlock(animationBounds))
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSendPopMessage("§cAnimation impossible! Risque de glicth dans un block.", 2000), player);
                return null;
            }

            for (EntityPlayerMP p : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketHandleAnimationResponse(ctx.getServerHandler().player.getName(), message.animationName), p);
            }

            AnimationUtils.setPlayerHandledAnimation(ctx.getServerHandler().player.getName(), AnimationUtils.createAnimationFromStaticInstance(AnimationUtils.animations.get(message.animationName)));
            //System.out.println("Receiving packet animation from " + player.getName() + " sending update to all players");
            return null;
        }
    }
}