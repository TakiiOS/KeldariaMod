package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import net.minecraft.command.CommandException;

import javax.security.auth.login.LoginException;
import java.util.List;

public class CommandStartBot extends KeldariaCommand
{
    public CommandStartBot()
    {
        super("startbot", "/startBot", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        try
        {
            DiscordImpl.getInstance().initBot();
        } catch (LoginException e)
        {
            e.printStackTrace();
        }
    }
}
