package fr.nathanael2611.keldaria.mod.discord.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Util
{

    public static void sendErrorMessage(MessageReceivedEvent e, ErrorType errorType)
    {
        e.getTextChannel().sendMessage(":x: **" + errorType.getMessage() + "** :x:").queue();
    }

    public enum ErrorType
    {
        HASNT_PERMISSION("Vous n'avez pas la permission !");

        private String message;

        ErrorType(String message)
        {
            this.message = message;
        }

        public String getMessage()
        {
            return message;
        }
    }
}
