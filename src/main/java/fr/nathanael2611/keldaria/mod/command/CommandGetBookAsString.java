/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketCopyStringToClipboard;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class CommandGetBookAsString extends KeldariaCommand
{

    public CommandGetBookAsString()
    {
        super("getBookAsString", "/getBookAsString", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        ItemStack stack = user.asPlayer().getHeldItemMainhand();
        if(stack.getItem() instanceof ItemWrittenBook)
        {
            if(!stack.hasTagCompound()) return;
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null) return;
            NBTTagList bookPages = compound.getTagList("pages", 8).copy();
            List<String> strings = new ArrayList<>();
            for(int i = 0; i < bookPages.tagCount(); i++) strings.add(bookPages.getStringTagAt(i));
            JsonArray pages = new JsonParser().parse(strings.toString().replace("\n", "")).getAsJsonArray();
            StringBuilder book = new StringBuilder();
            for(int i = 0; i < pages.size(); i++)
            {
                JsonObject obj = pages.get(i).getAsJsonObject();
                book.append(obj.get("text").getAsString()).append("\n");
            }
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketCopyStringToClipboard(book.toString()), user.asPlayer());
            user.sendMessage(GREEN + "Le texte a été copié dans votre presse papier...");
        }
        else user.sendMessage(RED + "Désolé, mais cette commande fonctionne seulement sur les livres signés.");
    }
}
