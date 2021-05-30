/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.bridj.cpp.std.list;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandJob extends KeldariaCommand
{
    public CommandJob()
    {
        super("job", "/job <player> infos OU /job <player> <job> <level>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (args.length > 1)
        {
            EntityPlayerMP dest = user.getPlayer(args[0]);
            if (args[1].equalsIgnoreCase("infos"))
            {
                user.sendMessage("§cMétiers de " + args[0]);
                for (EnumJob value : EnumJob.values())
                {
                    EnumJob.Level level = value.getLevel(dest);
                    if (level.atLeast(EnumJob.Level.NOVICE))
                    {
                        user.sendMessage(" §e- §6" + value.getFormattedName() + "§e " + level.getFormattedName());
                    }
                }
            } else if (args.length > 2)
            {
                EnumJob job = EnumJob.byName(args[1]);
                if (job == null)
                    throw new WrongUsageException(getUsage(user.getSender()));
                EnumJob.Level level = EnumJob.Level.byName(args[2]);
                if (level == null)
                    throw new WrongUsageException(getUsage(user.getSender()));
                job.set(dest, level);
                user.sendMessage(String.format("§cLe métier §6%s§c de §6%s§c a été défini au niveau §e%s", job.getFormattedName(), args[0], level.getFormattedName()));
            } else
            {
                throw new WrongUsageException(getUsage(user.getSender()));
            }
        } else
        {
            throw new WrongUsageException(getUsage(user.getSender()));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if(args.length == 2)
        {
            List<String> list = Arrays.stream(EnumJob.values()).map(EnumJob::getName).collect(Collectors.toList());
            list.add("infos");
            return getListOfStringsMatchingLastWord(args, list);
        }
        else if(args.length == 3)
        {
            List<String> list = Lists.newArrayList();
            for (EnumJob.Level value : EnumJob.Level.values())
            {
                list.add(value.getName());
            }
            return getListOfStringsMatchingLastWord(args, list);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
