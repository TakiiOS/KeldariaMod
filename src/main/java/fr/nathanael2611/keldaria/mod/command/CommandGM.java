/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandGM extends KeldariaCommand
{
    public CommandGM()
    {
        super("gamemode", "/gamemode <0/1/2/3>", createAliases("gm"));
    }

    /**
     * Callback for when the command is executed
     */
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
        else
        {
            GameType gametype = this.getGameModeFromCommand(user.getSender(), args[0]);
            EntityPlayer entityplayer = args.length >= 2 ? getPlayer(user.getServer(), user.getSender(), args[1]) : getCommandSenderAsPlayer(user.getSender());
            if(GroupManager.executePermissionCheck("command.gamemode.change." + gametype.getID(), user.getSender()))
            {
                entityplayer.setGameType(gametype);
                ITextComponent itextcomponent = new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]);

                if (user.getSender().getEntityWorld().getGameRules().getBoolean("sendCommandFeedback"))
                {
                    entityplayer.sendMessage(new TextComponentTranslation("gameMode.changed", new Object[] {itextcomponent}));
                }

                if (entityplayer == user.getSender())
                {
                    notifyCommandListener(user.getSender(), this, 1, "commands.gamemode.success.self", new Object[] {itextcomponent});
                }
                else
                {
                    notifyCommandListener(user.getSender(), this, 1, "commands.gamemode.success.other", new Object[] {entityplayer.getName(), itextcomponent});
                }

            }
            else
            {
                user.sendMessage(RED + "Vous n'avez pas la permission, souillasse!");
            }
        }
    }

    /**
     * Gets the Game Mode specified in the command.
     */
    protected GameType getGameModeFromCommand(ICommandSender sender, String gameModeString) throws CommandException, NumberInvalidException
    {
        GameType gametype = GameType.parseGameTypeWithDefault(gameModeString, GameType.NOT_SET);
        return gametype == GameType.NOT_SET ? WorldSettings.getGameTypeById(parseInt(gameModeString, 0, GameType.values().length - 2)) : gametype;
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"survival", "creative", "adventure", "spectator"});
        }
        else
        {
            return args.length == 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}
