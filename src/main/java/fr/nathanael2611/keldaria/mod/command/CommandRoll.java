/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class CommandRoll extends KeldariaCommand
{
    public CommandRoll()
    {
        super("roll", "/roll <distance> <min-value> <max-value> <description>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length < 4)
        {
            throw new WrongUsageException(getUsage(user.getSender()));
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < args.length; i++)
        {
            builder.append(" ").append(args[i]);
        }
        String desc = builder.toString().substring(1);
        int maxValue = parseInt(args[2]);
        int minValue = parseInt(args[1]);
        int rand = (int) Helpers.randomDouble(minValue, maxValue);

        int range = parseInt(args[0]);

        List<EntityPlayerMP> seeners = Lists.newArrayList();
        if(range == 0) seeners = user.getServer().getPlayerList().getPlayers();
        else
        {
            for (EntityPlayerMP player : user.getServer().getPlayerList().getPlayers())
            {
                if(player.getDistance(user.asPlayer()) <= range)
                {
                    seeners.add(player);
                }
            }
        }


        for (EntityPlayerMP player : seeners)
        {
            player.sendMessage(new TextComponentString(YELLOW + " �? " + GOLD + RolePlayNames.getName(user.asPlayer()) + YELLOW + " a fait un roll de " + GOLD + rand + YELLOW + "(max: " + GOLD + maxValue + ")\n" + YELLOW + "   Desc: " + GOLD + desc));
        }
        PlayerSpy.ROLL_LOG.log(
                "\n> **" + user.asPlayer().getName() + "**\n" +
                        "> **" + rand + "** (" + maxValue + ")\n" +
                        String.format("> `%s`", desc));
    }
}
