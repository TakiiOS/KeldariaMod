package fr.nathanael2611.keldaria.mod.command;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityPortcullis;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDefinePortcullis extends KeldariaCommand
{

    public CommandDefinePortcullis()
    {
        super("define-portullis", "/define-portcullis <pos> <pos1> <pos2>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 3)
        {
            BlockPos cullisPos = Helpers.parseBlockPosFromString(args[0]);
            if(user.asPlayer().world.getTileEntity(cullisPos) instanceof TileEntityPortcullis)
            {
                BlockPos pos1 = Helpers.parseBlockPosFromString(args[1]);
                BlockPos pos2 = Helpers.parseBlockPosFromString(args[2]);
                TileEntityPortcullis cullis = (TileEntityPortcullis) user.asPlayer().world.getTileEntity(cullisPos);
                if(cullis != null)
                {
                    cullis.x1 = pos1.getX();
                    cullis.x2 = pos2.getX();
                    cullis.y1 = pos1.getY();
                    cullis.y2 = pos2.getY();
                    cullis.z1 = pos1.getZ();
                    cullis.z2 = pos2.getZ();
                }
            }

        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(targetPos != null)
        {
            return Lists.newArrayList(Helpers.blockPosToString(targetPos));

        }
        return Lists.newArrayList();
    }
}
