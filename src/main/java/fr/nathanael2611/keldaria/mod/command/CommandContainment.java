/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.containment.ChunkRegion;
import fr.nathanael2611.keldaria.mod.features.containment.Containment;
import fr.nathanael2611.keldaria.mod.features.containment.Points;
import fr.nathanael2611.keldaria.mod.features.containment.Regions;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;

public class CommandContainment extends KeldariaCommand
{
    public CommandContainment()
    {
        super("containment", "/containment <on/off> OU /containment addPoint <chunk-radius> OU /containment addRegion <ChunkX1>,<ChunkZ1> <ChunkX2>,<ChunkZ2> <name>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        Chunk playerChunk = player.world.getChunk(player.getPosition());
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("removePoint"))
            {
                Points.remove(playerChunk);
                user.sendMessage(RED + "[Keldaria] " + GOLD + " Chunk de contention supprimé.");
            }
            else
            {
                Containment.setActive(args[0].equalsIgnoreCase("on"));
                user.sendMessage(RED + "[Keldaria] " + GOLD + " Contention des joueurs: " + YELLOW + Containment.isActive());

            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("addPoint"))
        {
            int radius = parseInt(args[1]);
            Points.set(playerChunk, radius);
            user.sendMessage(RED + "[Keldaria] " + GOLD + " Point de contention ajouté à votre chunk. [F3 pour le voir]");
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("removeRegion"))
        {
            Regions.remove(args[1]);
            user.sendMessage(RED + "[Keldaria] " + GOLD + " Region de contention supprimée. [F3 pour le voir]");
        }
        else if(args.length == 4 && args[0].equalsIgnoreCase("addRegion"))
        {
            Vector2i pos1 = Helpers.stringToChunkPos(args[1]);
            Vector2i pos2 = Helpers.stringToChunkPos(args[2]);
            String name = args[3];
            Regions.set(name, new ChunkRegion(pos1.x, pos1.y, pos2.x, pos2.y));
            user.sendMessage(RED + "[Keldaria] " + GOLD + String.format(" Region %s ajoutée. [%s - %s]", name, pos1, pos2));
        }
        else throw new WrongUsageException(getUsage(user.getSender()));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "addPoint", "addRegion", "removePoint", "removeRegion");
        }
        else if( (args.length == 2 || args.length == 3) && args[0].equalsIgnoreCase("addRegion"))
        {
            if(sender instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                Chunk chunk = player.world.getChunk(player.getPosition());
                return Lists.newArrayList(chunk.x + "," + chunk.z);
            }
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("removeRegion"))
        {
            return getListOfStringsMatchingLastWord(args, Databases.getDatabase("Containment:" + Regions.KEY).getAllEntryNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
