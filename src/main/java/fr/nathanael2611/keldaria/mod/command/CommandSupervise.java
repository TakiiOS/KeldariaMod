/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.containment.Containment;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandSupervise extends KeldariaCommand
{
    public CommandSupervise()
    {
        super("supervise", "/supervise <player>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 1)
        {
            EntityPlayerMP player = user.getPlayer(args[0]);
            Containment.supervise(player, Containment.isSupervised(player));
            user.sendMessage("§c[Keldaria] §6Supervision §e" + player.getName() + "§6: " + Containment.isSupervised(player));
        }
    }
}
