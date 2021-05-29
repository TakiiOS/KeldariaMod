package fr.nathanael2611.keldaria.mod.command;

import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import net.minecraft.command.CommandException;

public class CommandVoiceDistance extends KeldariaCommand
{


    public CommandVoiceDistance()
    {
        super("voicedistance", "/voicedistance <distance>", createAliases());
    }

    @Override
    public void run(CommandUser user, String[] args) throws CommandException
    {
        if(args.length == 1)
        {
            int distance = parseInt(args[0]);
            KeldariaVoices.setSpeakStrength(user.asPlayer(), distance);
        }
    }
}
