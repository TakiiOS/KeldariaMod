package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.features.Populator;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.List;

public class CommandPopulate extends KeldariaCommand
{


    public CommandPopulate()
    {
        super("populate", "/populate <chunk1> <chunk2> <surface/inside> <simple/ore> <maskBlocks> <block> <spawnChance>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        WrongUsageException usageException = new WrongUsageException(getUsage(user.getSender()));
        if(args.length == 9)
        {
            Vector2i c1 = Helpers.stringToChunkPos(args[0]);
            Vector2i c2 = Helpers.stringToChunkPos(args[1]);
            EntityPlayerMP player = user.asPlayer();
            int x1 = Math.min(c1.x, c2.x);
            int x2 = Math.max(c1.x, c2.x);
            int y1 = Math.min(c1.y, c2.y);
            int y2 = Math.max(c1.y, c2.y);
            for(int x = x1; x <= x2; x++)
            {
                for(int y = y1; y <= y2; y++)
                {
                    Chunk chunk = player.world.getChunk(x, y);
                    Populator.FillType fillType = Populator.FillType.byName(args[2]);
                    Populator.PopulateType populateType = Populator.PopulateType.byName(args[3]);
                    if(chunk != null)
                    {
                        Populator.populate(chunk,
                                fillType,
                                populateType,
                                getMask(args[4]),
                                Block.getBlockFromName(args[5]),
                                Helpers.parseDoubleOrZero(args[6]),

                                Helpers.parseOrZero(args[7]),
                                Helpers.parseOrZero(args[8]));
                    }
                }
            }
        }
        else
        {
            throw usageException;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1 || args.length == 2)
        {
            if(sender instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                Chunk chunk = player.world.getChunk(player.getPosition());
                return Lists.newArrayList(chunk.x + "," + chunk.z);
            }
        }
        else if(args.length == 3)
        {
            return Lists.newArrayList("surface", "inside");
        }
        else if(args.length == 4)
        {
            return Lists.newArrayList("simple", "ore");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    public static List<Block> getMask(String mask)
    {
        String[] parts = mask.split(",");
        List<Block> list = Lists.newArrayList();
        for (String part : parts)
        {
            list.add(Block.REGISTRY.getObject(new ResourceLocation(part)));
        }
        return list;
    }
}
