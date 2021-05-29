package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import net.dv8tion.jda.api.entities.Member;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;

public class CommandDiscordOP extends KeldariaCommand
{

    public CommandDiscordOP()
    {
        super("discordops", "discordops <op/deop/list> [discordId]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 0) throw new WrongUsageException(getUsage(user.getSender()));
        if(args[0].equalsIgnoreCase("list"))
        {
            DiscordImpl.getInstance().getConfigManager().getOpIds().forEach(s ->
            {
                Member member = DiscordImpl.getInstance().getBot().getKeldariaGuild().getMemberById(s);
                if(member != null) user.sendMessage(member.getAsMention());
            });
        }
        else if(args[0].equalsIgnoreCase("op") || args[0].equalsIgnoreCase("deop"))
        {
            if(args.length == 2)
            {
                if(args[0].equalsIgnoreCase("op"))
                {
                    DiscordImpl.getInstance().getConfigManager().deOp(args[1]);
                    user.sendMessage("Opped " + args[1]);
                }
                else if(args[0].equalsIgnoreCase("deop"))
                {
                    DiscordImpl.getInstance().getConfigManager().deOp(args[1]);
                    user.sendMessage("De-Opped " + args[1]);
                }
            }
            else
            {
                throw new WrongUsageException(getUsage(user.getSender()));
            }
        }
    }
}
