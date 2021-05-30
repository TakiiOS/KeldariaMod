/**
 * Copyright 2019-2021 Keldaria. Tous droits r√©serv√©s.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.OfflinePlayerAccessor;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandSeen extends KeldariaCommand
{
    public CommandSeen()
    {
        super("seen", "/seen <player>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length != 1) throw new WrongUsageException("/seen <player>");
        EntityPlayerMP playerMP = user.getServer().getPlayerList().getPlayerByUsername(args[0]);
        boolean online = playerMP != null;
        if(!online && !OfflinePlayerAccessor.hasOfflineGameProfile(user.getServer(), args[0])) throw new WrongUsageException("No player-data found for offline-player " + args[0]);
        OfflinePlayerAccessor accessor = online ? new OfflinePlayerAccessor(user.getServer(), playerMP.getGameProfile()) : new OfflinePlayerAccessor(user.getServer(), args[0]);
        Database playerData = Databases.getPlayerData(online ? playerMP.getName() : accessor.getName());
        String IP = playerData.getString("Ip");
        Date lastConnection = Helpers.getLastPlayerConnection(args[0]);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY √† HH:mm");
        user.getSender().sendMessage(new TextComponentString(TextFormatting.GOLD + " ‚ó? Informations sur " + TextFormatting.RED + args[0] + "\n" + TextFormatting.GOLD + "   ‚ó? Status: " + (online ? TextFormatting.GREEN : TextFormatting.DARK_RED) + (online ? "En Ligne" : "Hors Ligne")));
        user.getSender().sendMessage(new TextComponentString(TextFormatting.GOLD + "   ‚ó? Position: " + TextFormatting.RED + (online ? ((int) playerMP.posX + ", " + (int) playerMP.posY + ", " + (int) playerMP.posZ) : ((int) accessor.posX + ", " + (int) accessor.posY + ", " + (int) accessor.posZ))).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.RED + "‚ó? Se t√©l√©porter √† la position"))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %s %s %s", online ? playerMP.posX : accessor.posX, online ? playerMP.posY : accessor.posY, online ? playerMP.posZ : accessor.posZ)))));
        user.getSender().sendMessage(new TextComponentString(TextFormatting.GOLD + "   ‚ó? Inventaire: " + TextFormatting.RED + TextFormatting.UNDERLINE + "Voir l'invetaire").setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invsee " + args[0])).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.RED + "‚ó? Ouvrir l'inventaire de " + args[0])))));
        user.getSender().sendMessage(new TextComponentString(TextFormatting.GOLD + "   ‚ó? IP: ").appendSibling(new TextComponentString(TextFormatting.RED + "[HIDE]").setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.RED + "‚ó? " + IP))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copytoclipboard " + IP)))));
        user.getSender().sendMessage(new TextComponentString(TextFormatting.GOLD + "   ‚ó? Derni√®re Connexion: " + TextFormatting.RED + " " + (lastConnection != null ? format.format(lastConnection).toString() : "Inconnue.")));

    }
}
