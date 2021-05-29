package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandException;

import java.util.List;

public class CommandRemedy extends KeldariaCommand
{

    public CommandRemedy()
    {
        super("remedy", "/remedy <create> <name> <", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {


    }
}
