package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.Keldaria;
import net.minecraft.command.CommandException;

public class CommandPortcullisReload extends KeldariaCommand
{

    public CommandPortcullisReload()
    {
        super("portcullisreload", "/portcullisreload", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        //Keldaria.getInstance().getPortcullisConfig().reloadConfig();
        user.sendMessage(GREEN + "La configuration des portes coulissantes a été rechargée.");
    }
}
