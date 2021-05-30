/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toserver;

import fr.nathanael2611.keldaria.mod.crafting.CraftEntry;
import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import fr.nathanael2611.keldaria.mod.crafting.PlayerCrafts;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCraft implements IMessage
{

    private String managerName;
    private String key;

    public PacketCraft()
    {
    }

    public PacketCraft(String managerName, String key)
    {
        this.managerName = managerName;
        this.key = key;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.managerName = ByteBufUtils.readUTF8String(buf);
        this.key = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.managerName);
        ByteBufUtils.writeUTF8String(buf, this.key);
    }

    public static class Message implements IMessageHandler<PacketCraft, IMessage>
    {
        @Override
        public IMessage onMessage(PacketCraft message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            CraftManager manager = CraftManager.MANAGERS.get(message.managerName).copy();
            IKnownRecipes recipes = CraftManager.getRecipes(player);
            CraftEntry entry = manager.getCraft(message.key);
            if(entry != null)
            {
                PlayerCrafts crafts = PlayerCrafts.retrieve(manager, player);
                if (crafts.contains(message.key))
                {
                    if(entry.canCraft(player))
                    {
                        //System.out.println("hii");
                        entry.getIngredients().forEach((item, integer) ->
                        {
                            player.addItemStackToInventory(manager.getContainer(item.getItem()));
                            Helpers.removeQuantityOfItem(player, item.getItem(), item.getMeta(), integer);
                        });
                        player.addItemStackToInventory(entry.getResult().copy());
                        player.getHeldItemMainhand().damageItem(entry.getToolDamage(), player);
                        if (!entry.isNative(recipes))
                        {
                            if (Helpers.randomDouble(0, 100) < EnumAptitudes.INTELLIGENCE.getPoints(player) * 10)
                            {
                                recipes.discover(entry.getKey());
                                player.sendMessage(new TextComponentString("§aVotre personnage vient d'apprendre la recette! Il pourra désormais la reproduire sans avoir la recette sous les yeux!"));
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
