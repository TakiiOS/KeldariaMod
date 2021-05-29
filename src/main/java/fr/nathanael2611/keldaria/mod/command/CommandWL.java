package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.server.WhitelistManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CommandWL extends KeldariaCommand
{
    public CommandWL()
    {
        super("wl", "/wl <add/remove> <player> | /wl staffMode | /wl <list/listFoufou>", createAliases("keldaWhitelist"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException wrongUsage = new WrongUsageException(getUsage(user.getSender()));
        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                user.sendMessage(RED + "[KeldariaWhitelist] " + GOLD + WhitelistManager.getWhitelistedNames());
            }
            else if(args[0].equalsIgnoreCase("listFoufou"))
            {
                user.sendMessage(RED + "[KeldariaWhitelist] " + GOLD + "List détaillée:");
                List<String> inOrder = WhitelistManager.getWhitelistedNames();
                inOrder.sort(Comparator.comparingLong(p -> {
                    Date date = Helpers.getLastPlayerConnection(p);
                    if(date == null) return 0;
                    return date.getTime();
                }));
                Collections.reverse(inOrder);
                for (String s : inOrder)
                {
                    String da = "Inconnue";
                    Date d = Helpers.getLastPlayerConnection(s);
                    if(d != null)
                    {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY à HH:mm");
                        da = format.format(d);
                    }
                    user.sendMessage("   " + YELLOW + " - " + da + " | " + s);
                }

            }
            else if(args[0].equalsIgnoreCase("staffMode"))
            {
                WhitelistManager.switchStaffMode();
                user.sendMessage(RED + "[KeldariaWhitelist] " + GOLD + "StaffMode: " +YELLOW + WhitelistManager.isOnlyStaffMode());
            }
            else if(args.length > 1 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")))
            {
                if(args[0].equalsIgnoreCase("add"))WhitelistManager.whitelist(args[1]);
                else WhitelistManager.unWhitelist(args[1]);
                user.sendMessage(RED + "[KeldariaWhitelist] " + GOLD + args[1] + YELLOW + " a bien été " + (WhitelistManager.isWhitelisted(args[1]) ? "ajouté à la whitelist!" : "retiré de la whitelist..."));
            }
            else throw wrongUsage;
        }
        else
        {
            throw wrongUsage;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "add", "remove", "list", "listFoufou", "staffMode");
        }
        else if(args.length > 1 && args[0].equalsIgnoreCase("remove"))
        {
            return getListOfStringsMatchingLastWord(args, WhitelistManager.getWhitelistedNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
