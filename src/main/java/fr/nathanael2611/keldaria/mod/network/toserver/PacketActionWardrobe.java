/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.features.skin.CustomSkinManager;
import fr.nathanael2611.keldaria.mod.features.skin.Skin;
import fr.nathanael2611.keldaria.mod.features.skin.WardrobeManager;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketOpenGui;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketActionWardrobe implements IMessage
{

    private String action;
    private String name;
    private String url;

    public PacketActionWardrobe()
    {
    }

    public PacketActionWardrobe(String action, String name, String newUrl)
    {
        this.action = action;
        this.name = name;
        this.url = newUrl;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.action = ByteBufUtils.readUTF8String(buf);
        this.name = ByteBufUtils.readUTF8String(buf);
        this.url = ByteBufUtils.readUTF8String(buf);

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.action);
        ByteBufUtils.writeUTF8String(buf, this.name);
        ByteBufUtils.writeUTF8String(buf, this.url);
    }

    public static class Message implements IMessageHandler<PacketActionWardrobe, IMessage>
    {
        @Override
        public IMessage onMessage(PacketActionWardrobe message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
            {
                EntityPlayerMP player = ctx.getServerHandler().player;
                WardrobeManager.Wardrobe wardrobe = WardrobeManager.getInstance().getWardrobe(player);

                if (message.action.equalsIgnoreCase("setSkin"))
                {
                    CustomSkinManager.getInstance().setSkin(player, new Skin(message.name, message.url));
                } else if (message.action.equalsIgnoreCase("addSkin"))
                {
                    wardrobe.addSkin(new Skin(message.name, message.url), Databases.getPlayerData(player));
                } else if (message.action.equalsIgnoreCase("removeSkin"))
                {
                    wardrobe.removeSkin(new Skin(message.name, message.url), Databases.getPlayerData(player));
                    Skin actualSkin = CustomSkinManager.getInstance().getSkin(player.getName());
                    if (actualSkin.getName().equalsIgnoreCase(message.name) && actualSkin.getLink().equalsIgnoreCase(message.url))
                    {
                        CustomSkinManager.getInstance().setSkin(player, new Skin("Default", "Default"));
                    }
                    KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketOpenGui(PacketOpenGui.SKINCHOOSER), player);
                    return;
                }
            });

            return null;
        }
    }
}
