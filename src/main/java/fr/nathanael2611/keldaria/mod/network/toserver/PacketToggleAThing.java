/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.features.WalkMode;
import fr.nathanael2611.keldaria.mod.fight.FightControl;
import fr.nathanael2611.keldaria.mod.fight.FightHandler;
import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketToggleAThing implements IMessage
{

    public static int ID_HRP_CHAT = 0;
    public static int ID_WALKMODE = 1;
    public static int ID_ACCESSORY_HEAD = 2;
    public static int ID_ACCESSORY_CHEST = 3;
    public static int ID_ACCESSORY_LEGS = 4;
    public static int ID_ACCESSORY_FEETS = 5;
    public static int ID_CHANGE_COMBAT_TYPE = 6;
    public static int ID_TALK = 7;

    private int id;
    private boolean enabled;

    public PacketToggleAThing()
    {
    }

    public PacketToggleAThing(int id, boolean enabled)
    {
        this.id = id;
        this.enabled = enabled;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.enabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        buf.writeBoolean(this.enabled);
    }

    public static class Message implements IMessageHandler<PacketToggleAThing, IMessage>
    {
        @Override
        public IMessage onMessage(PacketToggleAThing message, MessageContext ctx)
        {
            IKeldariaPlayer keldaPlayer = (IKeldariaPlayer) ctx.getServerHandler().player;
            if(message.id == ID_HRP_CHAT)
            {
                Databases.getPlayerData(ctx.getServerHandler().player).setInteger(Keldaria.MOD_ID + ":hideGlobalChat", message.enabled ? 0 : 1);
            }
            else if(message.id == ID_WALKMODE)
            {
                WalkMode.toggle(ctx.getServerHandler().player);
            }
            else if(message.id == ID_ACCESSORY_HEAD)
            {
                keldaPlayer.getInventoryAccessories().makeUse(ctx.getServerHandler().player, EntityEquipmentSlot.HEAD);
            }
            else if(message.id == ID_ACCESSORY_CHEST)
            {
                keldaPlayer.getInventoryAccessories().makeUse(ctx.getServerHandler().player, EntityEquipmentSlot.CHEST);
            }
            else if(message.id == ID_ACCESSORY_LEGS)
            {
                keldaPlayer.getInventoryAccessories().makeUse(ctx.getServerHandler().player, EntityEquipmentSlot.LEGS);
            }
            else if(message.id == ID_ACCESSORY_FEETS)
            {
                keldaPlayer.getInventoryAccessories().makeUse(ctx.getServerHandler().player, EntityEquipmentSlot.FEET);
            }
            else if(message.id == ID_CHANGE_COMBAT_TYPE)
            {
                /*long millis = System.currentTimeMillis();
                Database db = Databases.getPlayerData(ctx.getServerHandler().player);
                long lastTimeMillis = db.isString("canSwitchAttack") ? Long.parseLong(db.getString("canSwitchAttack")) : millis - 4000;
                if(millis - lastTimeMillis > 300)
                {
                    EnumAttackType.switchAttackType(ctx.getServerHandler().player);
                    db.setString("canSwitchAttack", String.valueOf(millis));
                }*/
                FightControl<EntityPlayer> fight = FightHandler.getFightControl(ctx.getServerHandler().player);
                if (fight != null)
                {
                    fight.setFightMode(!fight.isFightMode());
                }
            }
            else if(message.id == ID_TALK)
            {
                if (!KeldariaVoices.hearTroughWalls(ctx.getServerHandler().player.world))
                {
                    KeldariaVoices.reloadHearList(ctx.getServerHandler().player);
                }
                KeldariaVoices.updateTalking(ctx.getServerHandler().player, message.enabled);
            }
            return null;
        }
    }

}
