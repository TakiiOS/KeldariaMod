package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandDebugMode extends KeldariaCommand
{
    public CommandDebugMode()
    {
        super("debug", "/debug", createAliases("mode-sans-échec"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP playerMP = user.asPlayer();
        switchDebugMode(playerMP.getName());
    }

    public static void switchDebugMode(String name)
    {
        Databases.getPlayerData(name).setInteger("DebugMode", isInDebugMode(name) ? 0 : 1);
    }

    public static boolean isInDebugMode(String name)
    {
        return Databases.getPlayerData(name).getInteger("DebugMode") == 1;
    }

}
