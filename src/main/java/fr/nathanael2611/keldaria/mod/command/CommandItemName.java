/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandException;

public class CommandItemName extends KeldariaCommand
{

    public CommandItemName()
    {
        super("itemname", "/itemname <name>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 0)
        {
            StringBuilder builder = new StringBuilder();
            for (String arg : args)
            {
                builder.append(" ").append(arg);
            }
            String name = builder.toString().substring(1);
            name = name.replace("&", "§");

            user.asPlayer().getHeldItemMainhand().setStackDisplayName(name);
        }
    }
}
