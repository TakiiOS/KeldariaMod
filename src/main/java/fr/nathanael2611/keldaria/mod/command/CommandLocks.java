/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.lockpick.Lock;
import fr.nathanael2611.keldaria.mod.features.lockpick.LocksHelper;
import fr.nathanael2611.keldaria.mod.item.ItemKey;
import fr.nathanael2611.keldaria.mod.item.ItemLock;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.BlockDoor;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;

public class CommandLocks extends KeldariaCommand
{

    public CommandLocks()
    {
        super("locks", "/locks <lock/unlock/get> <x> <y> <z> [<keyId>] OR /locks create <lock/key/fromHand> [<keyId>]", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        CommandException wrongUsage = new WrongUsageException(getUsage(user.getSender()));
        if(args.length > 1)
        {
            if(args[0].equalsIgnoreCase("create"))
            {
                if(args[1].equalsIgnoreCase("fromHand"))
                {
                    EntityPlayerMP player = getCommandSenderAsPlayer(user.getSender());
                    ItemStack hand = player.getHeldItemMainhand();
                    if(hand.getItem() instanceof ItemKey || hand.getItem() instanceof ItemLock)
                    {
                        ItemStack stack = new ItemStack(hand.getItem() instanceof ItemKey ? KeldariaItems.LOCK : KeldariaItems.KEY);
                        LocksHelper.setKeyCode(stack, LocksHelper.getKeyId(hand));
                        player.addItemStackToInventory(stack);
                    }
                    else
                    {
                        user.sendMessage(RED + "Vous devez avoir un cadenas ou une clef dans votre main");
                    }
                }
                else if(args[1].equalsIgnoreCase("key") || args[1].equalsIgnoreCase("lock"))
                {
                    if(args.length > 2)
                    {
                        ItemStack stack = new ItemStack(args[1].equalsIgnoreCase("key") ? KeldariaItems.KEY : KeldariaItems.LOCK);
                        LocksHelper.setKeyCode(stack, parseInt(args[2]));
                        getCommandSenderAsPlayer(user.getSender()).addItemStackToInventory(stack);
                    }
                    else
                    {
                        throw wrongUsage;
                    }
                }
                else
                {
                    throw wrongUsage;
                }
            }
            else
            {
                if(args.length >= 4 && (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock")))
                {
                    BlockPos pos = new BlockPos(parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
                    WorldServer world = user.getServer().getWorld(0);
                    if(!(world.getBlockState(pos).getBlock() instanceof BlockDoor))
                    {
                        user.sendMessage(RED + "Il n'y a pas de porte à la position " + DARK_RED + Helpers.blockPosToString(pos));
                        return;
                    }
                    if(!(world.getBlockState(pos.up()).getBlock() instanceof BlockDoor)) pos = pos.down();
                    if(args[0].equalsIgnoreCase("get"))
                    {
                        Lock lock = LocksHelper.getLockFromPos(pos);
                        if(lock.isLocked)
                        {
                            user.sendMessage(GOLD + "Le cadenas sur la porte située à " + RED + Helpers.blockPosToString(pos) + GOLD + " a pour ID " + RED + lock.getKeyid());
                        }
                        else
                        {
                            user.sendMessage(GOLD + "La porte située à " + RED + Helpers.blockPosToString(pos) + GOLD + " n'est pas vérouillée");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("lock"))
                    {
                        if(args.length == 5)
                        {
                            LocksHelper.lockDoor(pos, parseInt(args[4]));
                            user.sendMessage(GOLD + "La porte à " + RED + Helpers.blockPosToString(pos) + GOLD + " a été vérouillée avec l'ID " + RED + args[4]);
                        }
                        else
                        {
                            throw wrongUsage;
                        }
                    }
                    else
                    {
                        LocksHelper.unLockDoor(pos);
                        user.sendMessage(GOLD + "La porte à " + RED + Helpers.blockPosToString(pos) + GOLD + " a été dé-vérouillée");
                    }
                }
                else
                {
                    throw wrongUsage;
                }
            }
        }
        else
        {
            throw wrongUsage;
        }

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "get", "lock", "unlock");
        }
        else if(args.length >= 2 && (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock")))
        {
            if (args.length <= 4)
            {
                return getTabCompletionCoordinate(args, 1, targetPos);
            }
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
