/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.features.MorphingManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class CommandMorph extends KeldariaCommand
{
    public CommandMorph()
    {
        super("morph", "/morph <player> remove OU /morph <player> <morphJson>", createAliases("transofmatioonnnn"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 1)
        {
            EntityPlayer player = user.getPlayer(args[0]);
            if(args[1].equalsIgnoreCase("remove"))
            {
                MorphingManager.unMorph(player);
            }
            else if(args.length >= 3)
            {
                if(args[1].equalsIgnoreCase("set"))
                {
                    String entityRegistryName = args[2];
                    String total = "{}";
                    if(args.length > 3)
                    {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < args.length; i++)
                        {
                            sb.append(" ").append(args[i]);
                        }
                        total = sb.toString().substring(1);
                    }
                    JsonObject obj = new JsonParser().parse(total).getAsJsonObject();
                    obj.addProperty("id", entityRegistryName);
                    total = obj.toString();
                    MorphingManager.morph(player, total);
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, "set", "remove");
        }
        else if(args.length == 3)
        {
            List<String> string = Lists.newArrayList();
            ForgeRegistries.ENTITIES.forEach(registry -> {
                if(EntityLivingBase.class.isAssignableFrom((registry.getEntityClass())))
                {
                    string.add(registry.getRegistryName().toString());
                }
            });
            return getListOfStringsMatchingLastWord(args, string);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
