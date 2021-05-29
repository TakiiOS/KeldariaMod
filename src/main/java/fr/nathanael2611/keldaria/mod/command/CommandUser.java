package fr.nathanael2611.keldaria.mod.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandUser
{

    private MinecraftServer server;
    private ICommandSender sender;

    CommandUser(MinecraftServer server, ICommandSender sender)
    {
        this.server = server;
        this.sender = sender;
    }

    public MinecraftServer getServer()
    {
        return server;
    }

    public ICommandSender getSender()
    {
        return sender;
    }

    public void sendMessage(Object text)
    {
        sender.sendMessage(new TextComponentString("" + text));
    }

    public EntityPlayerMP asPlayer() throws PlayerNotFoundException
    {
        return CommandBase.getCommandSenderAsPlayer(this.sender);
    }

    public EntityPlayerMP getPlayer(String playerName) throws CommandException
    {
        return CommandBase.getPlayer(this.server, this.sender, playerName);
    }

}
