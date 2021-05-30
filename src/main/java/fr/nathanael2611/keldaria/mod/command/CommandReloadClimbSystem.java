/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateClimbSystem;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandReloadClimbSystem extends KeldariaCommand
{

    public CommandReloadClimbSystem()
    {
        super("reloadclimbsystem", "/reloadclimbsystem", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        Keldaria.getInstance().getClimbSystem().init(Helpers.readFileToString(Keldaria.getInstance().getFiles().CLIMB_SYSTEM));
        for (EntityPlayerMP player : user.getServer().getPlayerList().getPlayers())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateClimbSystem(), player);
        }
        user.sendMessage("Successfully reloaded ClimbSystem UwU");
    }
}
