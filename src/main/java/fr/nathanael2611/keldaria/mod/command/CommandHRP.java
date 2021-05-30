/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandHRP extends KeldariaCommand
{
    public CommandHRP()
    {
        super("hrp", "/hrp <sign/carpet/cardcarpet>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 0)
        {
            throw new WrongUsageException(getUsage(user.getSender()));
        }
        ItemStack stack = ItemStack.EMPTY;
        if(args[0].equalsIgnoreCase("sign"))
        {
            stack = new ItemStack(KeldariaBlocks.HRP_SIGN);
        }
        else if(args[0].equalsIgnoreCase("carpet"))
        {
            stack = new ItemStack(KeldariaBlocks.HRP_CARPET);
        }
        else if(args[0].equalsIgnoreCase("cardcarpet"))
        {
            stack = new ItemStack(KeldariaBlocks.CARD_CARPET);
        }
        else
        {
            throw new WrongUsageException(getUsage(user.getSender()));
        }
        user.asPlayer().addItemStackToInventory(stack);
        user.sendMessage(GREEN + "Item HRP donné!");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return getListOfStringsMatchingLastWord(args, "sign", "carpet", "cardcarpet");
    }
}
