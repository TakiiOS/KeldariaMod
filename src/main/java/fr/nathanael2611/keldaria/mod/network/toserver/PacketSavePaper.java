/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.writable.WritablePaper;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSavePaper implements IMessage
{

    private boolean sign = false;
    private String title = "Canvas Signé";
    private String content = "";

    public PacketSavePaper()
    {
    }

    public PacketSavePaper(String content, boolean sign)
    {
        this.content = content;
        this.sign = sign;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.sign = buf.readBoolean();
        this.title = ByteBufUtils.readUTF8String(buf);
        this.content = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.sign);
        ByteBufUtils.writeUTF8String(buf, this.title);
        ByteBufUtils.writeUTF8String(buf, this.content);
    }

    public void setTitle(String title)
    {
        if(this.sign) this.title = title;
    }

    public static class Message implements IMessageHandler<PacketSavePaper, IMessage>
    {
        @Override
        public IMessage onMessage(PacketSavePaper message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            ItemStack mainHand = player.getHeldItemMainhand();
            if(mainHand.getItem() == Items.PAPER)
            {
                WritablePaper.write(mainHand, message.content);
                if(message.sign)
                {
                    WritablePaper.sign(mainHand, message.title, RolePlayNames.getName(player) + " (" + player.getName() + ")");
                    ItemStack writed = new ItemStack(KeldariaItems.WRITED_PAPER, 1);
                    writed.setTagCompound(mainHand.getTagCompound());
                    player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, writed);
                    writed.setStackDisplayName(message.title);
                }
            }
            return null;
        }
    }

}