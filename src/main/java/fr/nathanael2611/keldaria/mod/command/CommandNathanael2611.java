package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandNathanael2611 extends KeldariaCommand
{

    public CommandNathanael2611()
    {
        super("nathanael2611", "/nathanael2611 <args>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        Database db = Databases.getPlayerData("Nathanael2611");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
