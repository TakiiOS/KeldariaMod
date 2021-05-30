/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.AmputationManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandAmputation extends KeldariaCommand
{

    public CommandAmputation()
    {
        super("amputation", "/amputation <player> <amputate/graft> <member>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 3)
        {
            EntityPlayerMP player = user.getPlayer(args[0]);
            AmputationManager.Member member;
            try
            {
                member = AmputationManager.Member.valueOf(args[2].toUpperCase());
            } catch (Exception ex)
            {
                ex.printStackTrace();
                user.sendMessage(RED + "Veuillez fournir un membre amputable valide.");
                return;
            }
            if(args[1].equalsIgnoreCase("amputate"))
            {
                AmputationManager.amputate(player, member);
                user.sendMessage(GREEN + "Amputation réussie!");
            }
            else if(args[1].equalsIgnoreCase("graft"))
            {
                AmputationManager.graft(player, member);
                user.sendMessage(GREEN + "Greffe réussie!");
            }
            else
            {
                user.sendMessage(RED + "Action invalide! amputate OU graft");
            }
        }
        else
        {
            throw new WrongUsageException(getUsage(user.getSender()));
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
            return getListOfStringsMatchingLastWord(args, "amputate", "graft");
        }
        else
        {
            String[] amputableMembers = new String[AmputationManager.Member.values().length];
            for (int i = 0; i < AmputationManager.Member.values().length; i++)
            {
                amputableMembers[i] = AmputationManager.Member.values()[i].getName();
            }
            return getListOfStringsMatchingLastWord(args, amputableMembers);
        }
    }
}
