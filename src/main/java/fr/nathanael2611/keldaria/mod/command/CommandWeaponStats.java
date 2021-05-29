package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandWeaponStats extends KeldariaCommand
{
    public CommandWeaponStats()
    {
        super("weaponstats", "", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException usage = new WrongUsageException("/weaponstats <player> set <stat> <value> OR /weaponstats <player> infos");
        if(args.length > 1)
        {
            EntityPlayerMP player = user.getPlayer(args[0]);
            if(args[1].equalsIgnoreCase("infos"))
            {
                user.sendMessage(RED + "   Infos dde compétences de combat sur " + GOLD + player.getName());
                for (WeaponStat value : WeaponStat.values())
                {
                    user.sendMessage(YELLOW + " ● " + value.getName() + ": " + GOLD + value.getLevel(player) + YELLOW + "/20");
                }
            }
            else if(args.length == 4 && args[1].equalsIgnoreCase("set"))
            {
                try
                {
                    try
                    {
                        WeaponStat ability = WeaponStat.valueOf(args[2].toUpperCase());
                        double val = Double.parseDouble(args[3]);
                        ability.set(player, val);
                        user.sendMessage(YELLOW + "Set " + GOLD + ability.getName() + YELLOW + " of " + GOLD + player.getName() + YELLOW + " to " + GOLD + val);
                    }
                    catch (Exception ex)
                    {
                        throw usage;
                    }
                }
                catch (NullPointerException e)
                {
                    user.sendMessage(RED + "La compétence fournie est invalide.");
                }
            }
            else if(args.length == 4 && args[1].equalsIgnoreCase("add"))
            {
                try
                {
                    try
                    {
                        WeaponStat ability = WeaponStat.valueOf(args[2].toUpperCase());
                        double val = Double.parseDouble(args[3]);
                        ability.increment(player, val);
                        user.sendMessage(YELLOW + "Incremented " + GOLD + ability.getName() + YELLOW + " of " + GOLD + player.getName() + YELLOW + " by " + GOLD + val);
                    }
                    catch (Exception ex)
                    {
                        throw usage;
                    }
                }
                catch (NullPointerException e)
                {
                    user.sendMessage(RED + "La compétence fournie est invalide.");
                }
            }
            else
            {
                throw usage;
            }
        }
        else
        {
            throw usage;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1 ) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        else if(args.length == 2) return getListOfStringsMatchingLastWord(args, "add", "set", "infos");
        else if(args.length == 3)
        {
            List<String > strs = new ArrayList<>();
            for(WeaponStat skill : WeaponStat.values())
            {
                strs.add(skill.toString());
            }
            return getListOfStringsMatchingLastWord(args, strs);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
