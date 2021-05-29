package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandComplement extends KeldariaCommand
{
    public CommandComplement()
    {
        super("complement", "", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException usage = new WrongUsageException("/complement <player> <add/remove> <complement> | /complement <player> infos");
        if(args.length > 1)
        {
            EntityPlayerMP player = user.getPlayer(args[0]);
            if(args[1].equalsIgnoreCase("infos"))
            {
                user.sendMessage(RED + "   Infos de compléments sur " + GOLD + player.getName());
                for (EnumComplement value : EnumComplement.values())
                {
                    if(value.has(player))
                    user.sendMessage(YELLOW + " ● " + value.getFormattedName() + ": " + GOLD + value.getCost());
                }
            }
            else if(args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")))
            {
                try
                {
                    EnumComplement ability = EnumComplement.byName(args[2]);
                    try
                    {
                        boolean val = args[1].equalsIgnoreCase("add");
                        ability.set(player, val);
                        user.sendMessage(YELLOW + "Set " + GOLD + ability.getFormattedName() + YELLOW + " of " + GOLD + player.getName() + YELLOW + " to " + GOLD + val);
                    }
                    catch (Exception ex)
                    {
                        throw usage;
                    }
                }
                catch (NullPointerException e)
                {
                    user.sendMessage(RED + "Le complément fournie est invalide.");
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
        else if(args.length == 2) return getListOfStringsMatchingLastWord(args, "add", "remove", "infos");
        else if(args.length == 3)
        {
            List<String > strs = new ArrayList<>();
            for(EnumComplement skill : EnumComplement.values())
            {
                strs.add(skill.getName());
            }
            return getListOfStringsMatchingLastWord(args, strs);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
