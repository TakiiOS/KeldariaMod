/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketCopyStringToClipboard;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandCopyToClipboard extends KeldariaCommand
{

    public CommandCopyToClipboard()
    {
        super("copytoclipboard", "/copytoclipboard <text>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 0) throw new WrongUsageException("Veuillez fournir des arguments.");
        EntityPlayerMP playerMP = user.asPlayer();
        StringBuilder builder = new StringBuilder();
        for (String arg : args) builder.append(" ").append(arg);
        KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketCopyStringToClipboard(builder.toString().substring(1)), playerMP);
    }
}
