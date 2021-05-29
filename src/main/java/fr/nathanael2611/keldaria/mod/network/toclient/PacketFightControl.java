package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.fight.FightControl;
import fr.nathanael2611.keldaria.mod.fight.FightHandler;
import fr.nathanael2611.keldaria.mod.fight.FightStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketFightControl implements IMessage
{

    private int entityId;
    private NBTTagCompound controlTag;

    public PacketFightControl()
    {
    }

    public PacketFightControl(FightControl control)
    {
        this.entityId = control.getEntity().getEntityId();
        this.controlTag = control.serializeNBT();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.controlTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        ByteBufUtils.writeTag(buf, this.controlTag);
    }

    public static class Message implements IMessageHandler<PacketFightControl, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketFightControl message, MessageContext ctx)
        {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.player;
            World world = mc.world;
            if (world != null && player != null)
            {
                Entity entity = world.getEntityByID(message.entityId);
                if(entity != null)
                {
                    FightControl<Entity> entityFightControl = FightHandler.getFightControl(entity);
                    if(entityFightControl != null)
                    {
                        entityFightControl.deserializeNBT(message.controlTag);
                        //System.out.println(entityFightControl.serializeNBT());

                    }
                }
            }
            return null;
        }
    }
}
