package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.nathanael2611.keldaria.mod.item.ItemLockpick;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenGui;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.math.BigDecimal;
import java.util.Random;

public class PacketLockpickDoor implements IMessage
{

    private String pos;

    public PacketLockpickDoor(String pos)
    {
        this.pos = pos;
    }
    public PacketLockpickDoor() {}

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.pos);
    }

    public static class Message implements IMessageHandler<PacketLockpickDoor, IMessage>
    {
        @Override
        public IMessage onMessage(PacketLockpickDoor message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;

                if(!EnumComplement.PICKING.has(player)) return;

                BlockPos pos = Helpers.parseBlockPosFromString(message.pos);
                World world  = ctx.getServerHandler().player.world;
                if(!(player.getHeldItemMainhand().getItem() instanceof ItemLockpick)) return;
                if(world.getBlockState(pos).getBlock() instanceof BlockDoor)
                {
                    if(pos.getDistance(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()) < 2)
                    {
                        BlockDoor door = (BlockDoor) world.getBlockState(pos).getBlock();
                        Random rand = Helpers.RANDOM;
                        int random = rand.nextInt(100);
                        player.getHeldItemMainhand().setItemDamage(player.getHeldItemMainhand().getItemDamage() + 1);
                        if(player.getHeldItemMainhand().getItemDamage()>=player.getHeldItemMainhand().getMaxDamage()) player.getHeldItemMainhand().shrink(1);

                        if(random <= (1 + EnumComplement.getPickingChancesPercent(player)))
                        {
                            door.toggleDoor(world, pos, true);
                            //EnumSkills.PICKING.increment(player, new BigDecimal(0.01f).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue());
                            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenGui(PacketOpenGui.NULL), player);
                        }
                    }
                }
            });
            return null;
        }
    }
}