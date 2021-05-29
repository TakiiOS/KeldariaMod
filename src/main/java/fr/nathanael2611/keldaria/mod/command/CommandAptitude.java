package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandAptitude extends KeldariaCommand
{
    public CommandAptitude()
    {
        super("aptitude", "", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException usage = new WrongUsageException("/aptitude <player> set <aptitude> <value> OR /aptitude <player> infos");
        if(args.length > 1)
        {
            EntityPlayerMP player = user.getPlayer(args[0]);
            if(args[1].equalsIgnoreCase("infos"))
            {
                user.sendMessage(RED + "   Infos d'aptitude sur " + GOLD + player.getName());
                for (EnumAptitudes value : EnumAptitudes.values())
                {
                    user.sendMessage(YELLOW + " ● " + value.getDisplayName() + ": " + GOLD + value.getPoints(player) + YELLOW + "/" + value.getMaxPoints());
                }
            }
            else if(args.length == 4 && args[1].equalsIgnoreCase("set"))
            {
                try
                {
                    EnumAptitudes ability = EnumAptitudes.valueOf(args[2].toUpperCase());
                    try
                    {
                        int val = Integer.parseInt(args[3]);
                        ability.set(player, val);
                        user.sendMessage(YELLOW + "Set " + GOLD + ability.getDisplayName() + YELLOW + " of " + GOLD + player.getName() + YELLOW + " to " + GOLD + val);
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
        else if(args.length == 2) return getListOfStringsMatchingLastWord(args, "set", "infos");
        else if(args.length == 3)
        {
            List<String > strs = new ArrayList<>();
            for(EnumAptitudes skill : EnumAptitudes.values())
            {
                strs.add(skill.getName());
            }
            return getListOfStringsMatchingLastWord(args, strs);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
