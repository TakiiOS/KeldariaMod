/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;

public class CommandZone extends KeldariaCommand
{

    public CommandZone()
    {
        super("zone", "/zone <setZone/getZone> [<zone>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        Chunk chunk = player.world.getChunk(player.getPosition());
        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("getZone"))
            {
                RPZone zone = Zones.getZone(chunk);
                user.sendMessage("La zone de votre chunk est: " + zone.getName());
            }
            else
            {
                if(args.length >= 2 && args[0].equalsIgnoreCase("setZone"))
                {
                    RPZone zone = Zones.byName(args[1]);
                    if(args.length == 3)
                    {
                        Zones.setZoneInRadius(chunk, zone, parseInt(args[2]));
                        user.sendMessage("La zone de votre chunk a été définie à: " + zone.getName() + " dans un rayon de " + args[2] + " chunks");
                    }
                    else
                    {
                        Zones.setZone(chunk, zone);
                        user.sendMessage("La zone de votre chunk a été définie à: " + zone.getName());
                    }
                }
                else if(args[0].equalsIgnoreCase("map"))
                {
                    int radius = args.length == 2 ? parseInt(args[1]) : 8;

                    ChunkPos pos = chunk.getPos();
                    user.sendMessage("§c§lMap des zones alentours:");
                    user.sendMessage("\n§aH§f = VOUS\nLe chunk actuel est en zone: " + Zones.getZone(chunk).getName() + " \n");
                    for (int x = pos.x - radius; x <= pos.x + radius; x++)
                    {
                        ITextComponent line = new TextComponentString("");
                        for (int z = pos.z - radius; z <= pos.z + radius; z++)
                        {
                            Chunk c = chunk.getWorld().getChunk(x, z);
                            RPZone zone = Zones.getZone(c);
                            BlockPos p = c.getPos().getBlock(0, 0, 0);
                            if(c == chunk)
                            {
                                line.appendSibling(new TextComponentString("§aH"));
                            }else
                            line.appendSibling(new TextComponentString(zone.getMapSymbol()).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(zone.getName()))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + p.getX() + " ~ " + p.getZ()))));
                        }
                        user.getSender().sendMessage(line);
                    }
                }

            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "setZone", "getZone");
        }
        else if(args.length == 2)
        {
          return getListOfStringsMatchingLastWord(args, Zones.getZonesNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
