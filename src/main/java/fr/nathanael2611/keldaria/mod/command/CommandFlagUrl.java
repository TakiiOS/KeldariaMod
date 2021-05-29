package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.item.ItemShipFlag;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CommandFlagUrl extends KeldariaCommand
{

    public CommandFlagUrl()
    {
        super("shipflag", "/shipflag <url>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length < 1)
        {
            user.sendMessage("Pas foufou");
            return;
        }
        EntityPlayerMP player = user.asPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        ItemShipFlag.setFlagUrl(stack, args[0]);
    }
}
