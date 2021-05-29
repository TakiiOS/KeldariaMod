package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

public class CommandGetJsonStack extends KeldariaCommand
{
    public CommandGetJsonStack()
    {
        super("getStackJson", "/getStackJson", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        String string = Helpers.getStringFromItemStack(user.asPlayer().getHeldItemMainhand());
        user.getSender().sendMessage(new TextComponentString(string).setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string))));
    }
}
