/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command.inventory;

import fr.nathanael2611.keldaria.mod.command.CommandUser;
import fr.nathanael2611.keldaria.mod.command.KeldariaCommand;
import fr.nathanael2611.keldaria.mod.util.OfflinePlayerAccessor;
import fr.nathanael2611.keldaria.mod.util.PlayerInvChest;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

public class CommandInvsee extends KeldariaCommand
{

    public CommandInvsee()
    {
        super("invsee", ":D", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (!FMLCommonHandler.instance().getEffectiveSide().isServer() || args.length != 1)
        {
            return;
        }
        EntityPlayerMP player = getCommandSenderAsPlayer(user.getSender());
        EntityPlayerMP victim = user.getServer().getPlayerList().getPlayerByUsername(args[0]);
        OfflinePlayerAccessor playerAccessor = null;
        if (victim == null)
        {
            if(OfflinePlayerAccessor.hasOfflineGameProfile(user.getServer(), args[0]))
            {
                playerAccessor = new OfflinePlayerAccessor(user.getServer(), args[0]);
            } else throw new PlayerNotFoundException("commands.generic.player.unspecified");
        }

        if (player.openContainer != player.inventoryContainer)
        {
            player.closeScreen();
        }
        player.getNextWindowId();

        PlayerInvChest chest = victim == null ? new PlayerInvChest(playerAccessor, player) : new PlayerInvChest(victim, player);
        player.displayGUIChest(chest);
    }



    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
