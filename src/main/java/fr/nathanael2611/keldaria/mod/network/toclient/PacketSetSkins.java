/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.ClientSkinManager;
import fr.nathanael2611.keldaria.mod.features.skin.CustomSkinManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSetSkins implements IMessage
{

    public static final int TYPE_ALL = 0;
    public static final int TYPE_PLAYER = 1;

    private NBTTagCompound customSkinManager;

    private int setType;
    private String playerName;
    private String skin;

    public PacketSetSkins(String player, String skin)
    {
        this.setType = TYPE_PLAYER;
        this.playerName = player;
        this.skin = skin;
    }

    public PacketSetSkins(CustomSkinManager customSkinManager)
    {
        this.setType = TYPE_ALL;
        this.customSkinManager = customSkinManager.serializeNBT();
    }

    public PacketSetSkins()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.setType = buf.readInt();
        if (this.setType == TYPE_ALL)
        {
            this.customSkinManager = ByteBufUtils.readTag(buf);
        } else
        {
            this.playerName = ByteBufUtils.readUTF8String(buf);
            this.skin = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.setType);
        if (this.setType == TYPE_ALL)
        {
            ByteBufUtils.writeTag(buf, this.customSkinManager);
        } else
        {
            ByteBufUtils.writeUTF8String(buf, this.playerName);
            ByteBufUtils.writeUTF8String(buf, this.skin);
        }
    }

    public static class Handler implements IMessageHandler<PacketSetSkins, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSetSkins message, MessageContext ctx)
        {
            if (message.setType == TYPE_ALL && message.customSkinManager != null)
            {
                ClientSkinManager.deserialize(message.customSkinManager);
            } else
            {
                ClientSkinManager.update(message.playerName, message.skin);
                //System.out.println("cc");
            }
            return null;
        }
    }
}
