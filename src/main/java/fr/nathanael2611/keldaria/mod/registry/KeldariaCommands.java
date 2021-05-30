/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.registry;

import fr.nathanael2611.keldaria.mod.command.KeldariaCommand;
import net.minecraft.command.CommandHandler;

import java.util.ArrayList;
import java.util.List;

public class KeldariaCommands
{

    private List<KeldariaCommand> commands = new ArrayList<>();

    public void clear()
    {
        this.commands.clear();
    }

    public void add(KeldariaCommand command)
    {
        this.commands.add(command);
    }

    public void addAll(KeldariaCommand... commands)
    {
        for (KeldariaCommand command : commands)
        {
            this.add(command);
        }
    }

    public void register(CommandHandler handler)
    {
        this.commands.forEach(handler::registerCommand);
    }

}
