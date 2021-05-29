package fr.nathanael2611.keldaria.mod.discord.impl;

import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class InfoChannel
{

    private String guildId;
    private String channelId;

    public static final InfoChannel PLAYER_COUNT = new InfoChannel("639070811730477066", "787187548266168330");
    public static final InfoChannel RP_DATE = new InfoChannel("639070811730477066", "787187601635409971");
    public static final InfoChannel RP_SEASON = new InfoChannel("639070811730477066", "787194988219072563");
    public static final InfoChannel LAST_REFRESH = new InfoChannel("639070811730477066", "787187696548315178");

    public InfoChannel(String guildId, String channelId)
    {
        this.guildId = guildId;
        this.channelId = channelId;
    }
    public void actualize(String name)
    {
        Guild guild = DiscordImpl.getInstance().getBot().getJda().getGuildById(this.guildId);
        if(guild != null)
        {
            VoiceChannel channel = guild.getVoiceChannelById(this.channelId);
            if(channel != null)
            {
                channel.getManager().setName(name).complete();
            }
        }
    }
}
