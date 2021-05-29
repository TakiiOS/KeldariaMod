package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.season.Season;
import net.minecraft.command.CommandException;

public class CommandSeason extends KeldariaCommand
{
    public CommandSeason()
    {
        super("season", "/season [<season>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            Season season = Season.byName(args[0]);
            if(season != null)
            {
                Season.setSeason(user.asPlayer().world, season);
                user.sendMessage(RED + "Keldaria > " + GOLD + "La saison a été définie à " + YELLOW + args[0]);
            }
            else
            {
                user.sendMessage(RED + "Keldaria > " + GOLD + "La saison donnée n'existe pas!");
            }
        }
        else
        {
            user.sendMessage(RED + "Keldaria > " + GOLD + "La saison actuelle est: " + YELLOW + Season.getSeason(user.asPlayer().world));
        }
    }
}
