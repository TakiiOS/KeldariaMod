/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.server.group.Group;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CommandGroup extends KeldariaCommand
{
    public CommandGroup()
    {
        super("group", "/group", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        GroupManager manager = GroupManager.getInstance();

        String usage = "/group <reload/list> OR /group checkup <group> OR /group set <player> <group> OR /group get <player>";
        String setUsage = "/group set <player> <group>";

        String goodPrefix = TextFormatting.DARK_GREEN + "[Kyrgon Permission] " + TextFormatting.GREEN;
        String badPrefix = TextFormatting.DARK_RED + "[Kyrgon Permission] " + TextFormatting.RED;

        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                GroupManager.getInstance().init();
                user.sendMessage(goodPrefix + "Les permissions ont bien été rechargées");
            } else if(args[0].equalsIgnoreCase("list"))
            {
                user.sendMessage(goodPrefix + "Liste des groupes: " + Arrays.toString(manager.getGroupNames()));
            } else
            {
                if(args.length > 1)
                {
                    if(args[0].equalsIgnoreCase("set"))
                    {
                        if(args.length > 2)
                        {
                            if(user.getServer().getPlayerList().getPlayerByUsername(args[1]) != null || Databases.hasCustomPlayerdata(args[1])) {
                                if (manager.has(args[2])) {
                                    Databases.getPlayerData(args[1]).setString("group", args[2]);
                                    user.sendMessage(goodPrefix + "Le groupe de " + args[1] + " a été définit à: '" + args[2] + "'");
                                } else {
                                    user.sendMessage(badPrefix + "Le groupe '" + args[2] + "' n'existe pas !");
                                }
                            } else
                            {
                                user.sendMessage(badPrefix + "Le joueur '" + args[1] + "' n'existe pas, ou ne s'est jamais connecté !");
                            }
                        } else {
                            throw new WrongUsageException(setUsage);
                        }
                    } else if(args[0].equalsIgnoreCase("get"))
                    {
                        EntityPlayerMP playerMP = getPlayer(user.getServer(), user.getSender(), args[1]);
                        user.sendMessage(goodPrefix + "Le groupe de " + playerMP.getName() + " est: '" + manager.get(playerMP).getName() + "'");
                    } else if(args[0].equalsIgnoreCase("checkup"))
                    {
                        if(manager.has(args[1]))
                        {
                            Group group = manager.get(args[1]);
                            user.sendMessage(
                                    goodPrefix + "Checkup du groupe '" + group.getName() + "':"
                                            + (!group.getFormattedName().equalsIgnoreCase(group.getName()) ? "\n" + TextFormatting.GREEN + " - Nom formatté: " + group.getFormattedName() : "")
                                            + "\n" + TextFormatting.GREEN + " - Permissions: " + Arrays.toString(group.getPermissions().getPermissionsAsString())
                                            + (group.getPermissions().inherit() ? "\n" + TextFormatting.GREEN + " - Hérite de : " + group.getPermissions().getInheritGroupName() : "")
                            );
                        } else {
                            user.sendMessage(badPrefix + "Le groupe '" + args[1] + "' n'existe pas !");
                        }
                    }
                } else {
                    throw new WrongUsageException(usage);
                }
            }
        } else {
            throw new WrongUsageException(usage);
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "list", "reload", "checkup", "get", "set");
        } else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("checkup")) return getListOfStringsMatchingLastWord(args, GroupManager.getInstance().getGroupNames());
            else if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("set")) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if(args.length == 3)
        {
            if(args[0].equalsIgnoreCase("set")) return getListOfStringsMatchingLastWord(args, GroupManager.getInstance().getGroupNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

}
