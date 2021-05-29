package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSpyRequest;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSpyReply;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandKeldaSpy extends KeldariaCommand
{
    public CommandKeldaSpy()
    {
        super("spy", "/spy <player> <thing>", createAliases("anticheat"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 1)
        {
            EntityPlayerMP mp = user.getPlayer(args[0]);
            user.sendMessage("§2Requête envoyée! Sachez que toutes les réponses sont postées sur discord");
            if(args[1].equalsIgnoreCase("screenshot"))
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSpyRequest(PacketSpyRequest.ID_SCREENSHOT, user.asPlayer().getName()), mp);
            }
            else if(args[1].equalsIgnoreCase("mods"))
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSpyRequest(PacketSpyRequest.ID_MODS, user.asPlayer().getName()), mp);
            }
            else if(args[1].equalsIgnoreCase("packs"))
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSpyRequest(PacketSpyRequest.ID_PACKS, user.asPlayer().getName()), mp);
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
