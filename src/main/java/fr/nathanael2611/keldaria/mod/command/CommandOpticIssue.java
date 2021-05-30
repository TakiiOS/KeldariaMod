/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.OpticIssue;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandOpticIssue extends KeldariaCommand
{

    public CommandOpticIssue()
    {
        super("opticissue", "/opticissue <player> [<issue>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException wrongUsage = new WrongUsageException(getUsage(user.getSender()));
        if (args.length > 0)
        {
            EntityPlayer player = user.getPlayer(args[0]);
            if (args.length > 1)
            {
                OpticIssue issue = OpticIssue.byName(args[1]);
                OpticIssue.setIssue(player, issue);
                user.sendMessage("§6Le problème de vue de §c" + player.getName() + "§6 est désormais: §c" + issue.getFormattedName());
            } else
            {
                user.sendMessage("§6Le problème de vue de §c" + player.getName() + "§6 est: §c" + OpticIssue.getIssue(player).getFormattedName());
            }
        } else
        {
            throw wrongUsage;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2)
        {
            List<String> list = Arrays.stream(OpticIssue.values()).map(OpticIssue::getName).collect(Collectors.toList());
            return getListOfStringsMatchingLastWord(args, list);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
