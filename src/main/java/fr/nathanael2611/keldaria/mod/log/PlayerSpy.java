/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.log;

import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSendLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum PlayerSpy
{
    MESSAGE_LOG("635516391742636032"), DOORS_LOG("635516946867027984"),
    COMMANDS_LOG("635518074937802800"), TE_LOG("637163185027416077"),
    HORSE_LOG("637170368506757131"), ROLL_LOG("709941545213755422"),
    DEATH_LOG("775701927370817556"), PUNCH_LOG("776268413635002419"),
    ITEMS_LOG("801135652224761886"), GUI_LOGS("807286282530062366"),
    SCREENSHOTS_LOG("807286072748670997"), SPY_LOG("808724757041250344");

    private final String channelId;

    PlayerSpy(String channelId)
    {
        this.channelId = channelId;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public void log(String message)
    {
        if(DiscordImpl.getInstance().getBot() == null) return;
        DiscordImpl.getInstance().getBot().addToMessageQueue("635516356615340065", channelId, String.format("[%s] %s", new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss").format(new Date()), message));
    }

    public static void fromClient(PacketSendLog logPacket)
    {
        for (PlayerSpy value : values())
        {
            if(value.channelId.equalsIgnoreCase(logPacket.getChannelId()))
            {
                value.log(logPacket.getLog());
            }
        }
    }

}
