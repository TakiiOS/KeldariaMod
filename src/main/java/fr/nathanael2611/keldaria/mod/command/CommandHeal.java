/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.features.thirst.Thirst;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class CommandHeal extends KeldariaCommand
{

    public CommandHeal()
    {
        super("heal", "/heal <player>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        EntityPlayerMP playerMP = args.length > 0 ? user.getPlayer(args[0]) : user.asPlayer();
        playerMP.setHealth(playerMP.getMaxHealth());
        FoodStats foodStats = playerMP.getFoodStats();
        foodStats.setFoodLevel(20);
        Field saturationField = FoodStats.class.getDeclaredFields()[1];
        saturationField.setAccessible(true);
        try
        {
            saturationField.setFloat(foodStats, 5f);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        //ThirstHelper.getThirstData(playerMP).setHydration(5);
        //ThirstHelper.getThirstData(playerMP).setThirst(20);
        Thirst thirst = Thirst.getThirst(playerMP);
        thirst.setThirstLevel(20);
        thirst.setHydration(5);
        user.sendMessage(GREEN + playerMP.getName() + " a bien été heal.");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}
