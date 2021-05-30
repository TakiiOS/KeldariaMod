/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;

public class CommandMillicie extends KeldariaCommand
{

    public CommandMillicie()
    {
        super("millicie", "/millicie", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        Database db = Databases.getPlayerData(user.asPlayer());
        db.setInteger("IsMillicieMode", db.getInteger("IsMillicieMode") == 1 ? 0 : 1);
    }
}
