package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class CommandStaffReply extends KeldariaCommand
{


    public CommandStaffReply()
    {
        super("staffreply", "/sreply <player> msg", createAliases("sreply", "sr"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(GroupManager.executePermissionCheck("staffchat.use", user.asPlayer()))
        {
            if(args.length > 1)
            {
                EntityPlayer p = user.getPlayer(args[0]);
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                {
                    builder.append(args[i]).append(" ");
                }
                p.sendMessage(new TextComponentString("§c[§4Réponse-Staff§c] §c" + user.asPlayer().getName() + "§6: " + builder.toString()));
            }
        }
        else
        {
            user.sendMessage("§cCette commande est réservée au staff de Kelda!");
        }

    }
}
