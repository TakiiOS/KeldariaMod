package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandException;

public class CommandRPDate extends KeldariaCommand
{
    public CommandRPDate()
    {
        super("roleplaydate", "/roleplaydate [<toggle>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 0)
        {
            user.sendMessage(YELLOW + " >> " + GOLD + "");
        }
    }
}
