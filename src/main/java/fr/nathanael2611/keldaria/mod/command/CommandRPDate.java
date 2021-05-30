/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
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
