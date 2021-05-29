package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandWarp extends KeldariaCommand
{

    public CommandWarp()
    {
        super("warp", "/warp <set/remove/tp> <warp> [<player>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        Database warps = Databases.getDatabase("warps");

        if(args.length >= 2)
        {
            if(args[0].equalsIgnoreCase("set"))
            {
                warps.setString(args[1], Helpers.blockPosToString(user.getSender().getPosition()));
                user.sendMessage(GREEN + " ● Le warp " + DARK_GREEN + args[1] + GREEN + " a été définit à votre position.");
            }
            else if(args[0].equalsIgnoreCase("remove"))
            {
                if(warps.isString(args[1]))
                {
                    warps.remove(args[1]);
                    user.sendMessage(RED + " ● Le warp " + DARK_RED + args[1] + RED + " a été supprimé.");
                }
                else user.sendMessage(RED + " ● Le warp " + DARK_RED + args[1] + RED + " ne peut être supprimé car il n'existe pas.");
            }
            else if(args[0].equalsIgnoreCase("tp"))
            {
                if(!warps.isString(args[1]))
                {
                    user.sendMessage(RED + " ● Le warp " + DARK_RED + args[1] + RED + " n'existe pas.");
                    return;
                }
                BlockPos warpPos = Helpers.parseBlockPosFromString(warps.getString(args[1]));
                if(args.length == 3)
                {
                    EntityPlayer player = getPlayer(user.getServer(), user.getSender(), args[2]);
                    player.setPositionAndUpdate(warpPos.getX(), warpPos.getY(), warpPos.getZ());
                }
                else getCommandSenderAsPlayer(user.getSender()).setPositionAndUpdate(warpPos.getX(), warpPos.getY(), warpPos.getZ());
            }
            else throw new WrongUsageException(getUsage(user.getSender()));
        }
        else throw new WrongUsageException(getUsage(user.getSender()));
    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        switch (args.length)
        {
            case 1: return getListOfStringsMatchingLastWord(args, "set", "remove", "tp");
            case 2: return getListOfStringsMatchingLastWord(args, Databases.getDatabase("warps").getAllEntryNames());
            case 3: return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            default: return super.getTabCompletions(server, sender, args, targetPos);
        }
    }

}
