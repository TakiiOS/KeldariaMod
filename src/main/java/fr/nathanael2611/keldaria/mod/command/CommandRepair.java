/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.item.ItemStack;

public class CommandRepair extends KeldariaCommand
{
    public CommandRepair()
    {
        super("repair", "/repair [pourcentage]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        int percent = 100;
        if(args.length == 1)
        {
            percent = parseInt(args[0]);
        }
        ItemStack toRepair = user.asPlayer().getHeldItemMainhand();
        toRepair.setItemDamage(toRepair.getMaxItemUseDuration() - Helpers.crossMult(
                percent,100, toRepair.getMaxItemUseDuration()
        ));
    }
}
