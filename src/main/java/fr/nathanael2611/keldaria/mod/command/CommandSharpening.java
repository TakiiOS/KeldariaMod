package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.SharpeningHelpers;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CommandSharpening extends KeldariaCommand
{
    public CommandSharpening()
    {
        super("sharpening", "/sharpening <sharpness>", createAliases("sharp"));
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP player = user.asPlayer();
        if(args.length > 0)
        {
            if(SharpeningHelpers.canBeSharped(player.getHeldItemMainhand()))
            {
                int sharp = parseInt(args[0]);
                SharpeningHelpers.setSharpness(player.getHeldItemMainhand(), sharp);
                user.sendMessage(GREEN + "Epée affûtée.");
            }
            else
            {
                user.sendMessage(RED + "Cet item n'est pas affûtable.");
            }
        }
        else
        {
            user.sendMessage(RED + "Utilisation correcte: " + getUsage(user.getSender()));
        }
    }
}
