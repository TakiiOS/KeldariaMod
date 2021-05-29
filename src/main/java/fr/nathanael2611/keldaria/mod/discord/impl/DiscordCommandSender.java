package fr.nathanael2611.keldaria.mod.discord.impl;

import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class DiscordCommandSender implements ICommandSender
{

    private final String ID;
    public boolean sendMP;

    /**
     * Constructor
     */
    public DiscordCommandSender(String ID, boolean sendMP)
    {
        this.ID = ID;
        this.sendMP = sendMP;
    }

    /**
     * Returns the discord account name
     */
    @Override
    public String getName()
    {
        return DiscordImpl.getInstance().getBot().getJda().getUserById(ID).getName();
    }

    /**
     * Returns true if the discord account can execute a specified command
     */
    @Override
    public boolean canUseCommand(int permLevel, String commandName)
    {
        if (DiscordImpl.getInstance().getConfigManager().isOP(ID)) return true;
        ICommand command = getServer().getCommandManager().getCommands().get(commandName);
        if (command != null)
        {
            if (command instanceof CommandBase)
            {
                return 1 >= ((CommandBase) command).getRequiredPermissionLevel();
            }
        }
        return false;
    }

    /**
     * returns the world on the server that the bot runTask on
     */
    @Override
    public World getEntityWorld()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0];
    }

    @Nullable
    @Override
    public MinecraftServer getServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    /**
     * Just send a message in the provided channel
     */
    @Override
    public void sendMessage(ITextComponent component)
    {
        String text = component.getUnformattedText();
        if (text.isEmpty()) return;
        for (TextFormatting value : TextFormatting.values())
        {
            text = text.replace(String.valueOf(value), "");
        }
        if (!this.sendMP)
        {
            DiscordImpl.getInstance().getBot().sendMessageOnDiscordLinkChannel(text);
            return;
        }
        Member member = DiscordImpl.getInstance().getBot().getKeldariaGuild().getMemberById(this.ID);
        if (!member.getUser().hasPrivateChannel()) member.getUser().openPrivateChannel().complete();
        ((UserImpl) member.getUser()).getPrivateChannel().sendMessage(text).complete();
    }

    public String getID()
    {
        return ID;
    }
}
