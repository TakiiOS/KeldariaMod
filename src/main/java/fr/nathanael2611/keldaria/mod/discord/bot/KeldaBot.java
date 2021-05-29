package fr.nathanael2611.keldaria.mod.discord.bot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.nathanael2611.keldaria.mod.discord.bot.events.EventsListener;
import fr.nathanael2611.keldaria.mod.discord.impl.InfoChannel;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.security.auth.login.LoginException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeldaBot
{

    private JDA jda;
    //private CommandManager commandManager;

    public boolean isReady = false;

    private Comparator<MessageContent> messageContentComparator = Comparator.comparingInt(o -> o.message.length());
    private Queue<MessageContent> messageQueue = new PriorityQueue<>(messageContentComparator);

    private long lastChannelUpdate = 0;

    public final ScheduledExecutorService MESSAGE_SERVICE = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("discord-bot").build());

    {
        this.MESSAGE_SERVICE.scheduleAtFixedRate(() ->
        {
            if(this.isReady && !this.messageQueue.isEmpty())
            {
                MessageContent content = this.messageQueue.remove();
                sendMessage(content.guildId, content.channelId, content.message);
            }
        }, 2000, 900, TimeUnit.MILLISECONDS);

        this.MESSAGE_SERVICE.scheduleAtFixedRate(() ->
        {
            if(System.currentTimeMillis() - this.lastChannelUpdate > TimeUnit.MINUTES.toMillis(5))
            {
                updateChannels();
                this.lastChannelUpdate = System.currentTimeMillis();
            }
        }, 3000, 2000, TimeUnit.MILLISECONDS);
    }

    public void updateChannels()
    {
        if(this.isReady)
        {
            int players = FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames().length;
            InfoChannel.PLAYER_COUNT.actualize(players > 1 ? ("\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66 " + players + " joueurs connectés.") : players == 1 ? "\uD83D\uDE4D 1 joueur connecté." : "Pas de joueurs connectés.");
            String season;
            InfoChannel.RP_DATE.actualize(KeldariaDate.lastDate.toFormattedString());
            switch (KeldariaDate.lastDate.getSeason())
            {
                case WINTER:
                    season = "\uD83C\uDF84 Hiver";
                    break;
                case SPRING:
                    season = "\uD83C\uDF37 Printemps";
                    break;
                case AUTUMN:
                    season = "\uD83C\uDF42 Automne";
                    break;
                case SUMMER:
                    season = "\uD83C\uDF1E Été";
                    break;
                default:
                    season = "Invalid.";
            }
            InfoChannel.RP_SEASON.actualize(season);
            InfoChannel.LAST_REFRESH.actualize("\uD83D\uDD56 Actualisé à " + new SimpleDateFormat("HH:mm").format(new Date()));
        }
    }

    public KeldaBot(String token) throws LoginException
    {
        jda = JDABuilder.createDefault(token).build();
        registerListeners();
    }

    public void registerListeners()
    {
        getJda().addEventListener(new EventsListener());
    }

    public JDA getJda()
    {
        return jda;
    }

    public void sendMessageOnDiscordLinkChannel(String message)
    {
        if(!isReady) return;
        addToMessageQueue(getKeldariaGuild().getId(), getDiscordLink().getId(), message);
    }

    public MessageChannel getDiscordLink()
    {
        return getKeldariaGuild().getTextChannelById("647990736763093031");
    }

    public Guild getKeldariaGuild()
    {
        return jda.getGuildById("639070811730477066");
    }

    private void sendMessage(String guildId, String channelId, String message)
    {
        if(!isReady) return;
        this.jda.getGuildById(guildId).getTextChannelById(channelId).sendMessage(message).complete();
    }

    public void addToMessageQueue(String guildId, String channelId, String message)
    {
        messageQueue.add(new MessageContent(guildId, channelId, message));
    }

    private class MessageContent
    {
        final String guildId;
        final String channelId;
        final String message;

        final int position;

        public MessageContent(String guildId, String channelId, String message) {
            this.guildId = guildId;
            this.channelId = channelId;
            this.message = message;
            this.position = messageQueue.size() + 1;
        }
    }

}
