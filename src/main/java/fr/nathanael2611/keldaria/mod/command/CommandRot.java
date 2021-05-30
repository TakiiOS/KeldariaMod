/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.rot.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.rot.capability.Rot;
import net.minecraft.command.CommandException;

public class CommandRot extends KeldariaCommand
{

    public CommandRot()
    {
        super("rot", "/rot <days>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 1)
        {
            Rot rot = ExpiredFoods.getRot(user.asPlayer().getHeldItemMainhand());
            if(args[0].equalsIgnoreCase("created"))
            {
                rot.setCreatedDay(KeldariaDate.lastDate.getTotalDaysInRP() - parseInt(args[1]));
            }
            else if(args[0].equalsIgnoreCase("salt"))
            {
                rot.setSaltBagDay(KeldariaDate.lastDate.getTotalDaysInRP() - parseInt(args[1]));
            }
        }
    }
}
