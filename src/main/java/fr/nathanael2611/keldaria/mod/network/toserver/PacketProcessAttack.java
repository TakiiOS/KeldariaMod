package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.fight.FightControl;
import fr.nathanael2611.keldaria.mod.fight.FightHandler;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.SwingTypes;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.Sword;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketProcessAttack implements IMessage
{

    private SwingTypes type;
    private float renderYawOffset;

    public PacketProcessAttack()
    {
    }

    public PacketProcessAttack(SwingTypes type, float renderYawOffset)
    {
        this.type = type;
        this.renderYawOffset = renderYawOffset;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.type = SwingTypes.byId(buf.readInt());
        this.renderYawOffset = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.type.getId());
        buf.writeFloat(this.renderYawOffset);
    }

    public static class Handler implements IMessageHandler<PacketProcessAttack, IMessage>
    {
        @Override
        public IMessage onMessage(PacketProcessAttack message, MessageContext ctx)
        {
            EntityPlayer player = ctx.getServerHandler().player;
            FightControl<EntityPlayer> control = FightHandler.getFightControl(player);
            if(control != null && control.isFightMode())
            {
                control.attack(new Sword(message.type).createAttack(player, message.renderYawOffset, message.type));
                player.setRenderYawOffset(message.renderYawOffset);
            }
            return null;
        }
    }

}
