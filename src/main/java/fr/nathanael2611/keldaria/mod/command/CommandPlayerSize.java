/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandPlayerSize extends KeldariaCommand
{

    public CommandPlayerSize()
    {
        super("playersize", "/playersize <set/get> <player> [<size>]", createAliases("rendersize"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 1)
        {
            EntityPlayerMP player = getPlayer(user.getServer(), user.getSender(), args[1]);
            if(args.length > 2)
            {
                double size;
                if(args[2].endsWith("m"))
                {
                    double s = parseDouble(args[2].substring(0, args[2].length() - 1));
                    size = Math.min(s / 1.8, 30);
                } else size = Math.min(parseDouble(args[2]), 30);
                PlayerSizes.set(player.getName(), size);
                user.sendMessage(GOLD + " �? La taille de " + RED + player.getName() + GOLD + " a été définie à " + RED + size + GOLD + " (" + size * 1.8 + "m)");
            } else {
                user.sendMessage(GOLD + " �? La taille de " + RED + player.getName() + GOLD + " est " + RED + PlayerSizes.get(player));
            }
        }
        else {
            throw new WrongUsageException(getUsage(user.getSender()));
        }

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        switch (args.length)
        {
            case 1: return getListOfStringsMatchingLastWord(args, "set", "get");
            case 2: return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            default: return super.getTabCompletions(server, sender, args, targetPos);
        }
    }
}
