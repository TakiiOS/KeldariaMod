package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCleaniless extends KeldariaCommand
{
    public CommandCleaniless()
    {

        super("cleaniless", "/cleaniless ", createAliases("proprete"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 2)
        {
            List<Entity> list = getEntityList(user.getServer(), user.getSender(), args[0]);
            for (Entity entity : list)
            {
                if(entity instanceof EntityPlayer)
                {
                    CleanilessManager.setCleaniless((EntityPlayer) entity, parseInt(args[1]));
                    user.sendMessage(GREEN + entity.getName() + " a été lavé.");
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 0)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
