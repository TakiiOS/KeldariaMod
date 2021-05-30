/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSpeed extends KeldariaCommand
{

    public CommandSpeed()
    {
        super("speed", "/speed <fly/walk> ", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length > 1)
        {
            EntityPlayer player = args.length > 2 ? user.getPlayer(args[2]) : user.asPlayer();
            if (args[1].equalsIgnoreCase("reset"))
            {
                resetSpeed(player);
                user.sendMessage(GREEN + "Resetting " + player.getName() + " speed.");
                return;
            }
            if(args[0].equalsIgnoreCase("fly")) setFlySpeed(player, parseDouble(args[1]));
            else if (args[0].equalsIgnoreCase("walk")) setWalkSpeed(player, parseDouble(args[1]));
            else throw new WrongUsageException("Invalid fly type. ['fly' OR 'walk']");
            user.sendMessage(GREEN + "Set " + player.getName() + " " + args[0] + "'s speed to " + args[1] + ".");
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public static void setWalkSpeed(EntityPlayer player, double speedDouble)
    {
        float speed = 0.05F;
        double multiplier = speedDouble;
        if (multiplier >= 20) multiplier = 20;
        speed = (float) (speed * multiplier);
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities").setTag("walkSpeed", new NBTTagFloat(speed));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
    }

    public static void setFlySpeed(EntityPlayer player, double speedDouble)
    {
        float speed = 0.05F;
        double multiplier = speedDouble;
        if (multiplier >= 20) multiplier = 20;
        speed = (float) (speed * multiplier);
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities").setTag("flySpeed", new NBTTagFloat(speed));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
    }

    public static float getRawWalkSpeed(EntityPlayer player)
    {
        return player.capabilities.getWalkSpeed();
    }

    public static float getRawFlySpeed(EntityPlayer player)
    {
        return player.capabilities.getFlySpeed();
    }

    public static void setRawFlySpeed(EntityPlayer player, float speed)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities").setTag("flySpeed", new NBTTagFloat(speed));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
    }

    public static void setRawWalkSpeed(EntityPlayer player, float speed)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities").setTag("walkSpeed", new NBTTagFloat(speed));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
    }

    public static void resetSpeed(EntityPlayer player)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        player.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.getCompoundTag("abilities").setTag("flySpeed", new NBTTagFloat(0.05F));
        tagCompound.getCompoundTag("abilities").setTag("walkSpeed", new NBTTagFloat(0.1F));
        player.capabilities.readCapabilitiesFromNBT(tagCompound);
        player.sendPlayerAbilities();
    }
}