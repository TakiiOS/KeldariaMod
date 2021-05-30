/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

public class CommandArmorSee extends KeldariaCommand
{
    public CommandArmorSee()
    {
        super("armorsee", ":D", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if (!FMLCommonHandler.instance().getEffectiveSide().isServer() || args.length != 1)
        {
            return;
        }
        EntityPlayerMP victim = user.getPlayer(args[0]);
        IKeldariaPlayer player = (IKeldariaPlayer) victim;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
        {
            for (ItemStack stack : player.getInventoryArmor().getArmor(slot))
            {
                if(!stack.isEmpty())
                user.getSender().sendMessage(new TextComponentString(
                        GOLD + slot.getName() + ">> " + YELLOW + " " + stack.getDisplayName()
                ).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(stack.serializeNBT().toString())))));
            }
        }

    }


    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
