/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.discord.bot.events;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.discord.impl.DiscordCommandSender;
import fr.nathanael2611.keldaria.mod.server.chat.RolePlayChatHooks;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EventsListener extends ListenerAdapter
{

    @Override
    public void onReady(ReadyEvent e)
    {
        super.onReady(e);
        DiscordImpl.getInstance().getBot().isReady = true;
        DiscordImpl.getInstance().getBot().sendMessageOnDiscordLinkChannel("**__Keldaria-Bot__, à votre service ! :wink:** (ah et au passage du coup, le serveur est ON)");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        super.onMessageReceived(e);
        String msg = e.getMessage().getContentDisplay();
        if (e.getTextChannel() != null)
        {
            if (e.getTextChannel().getId().equalsIgnoreCase(DiscordImpl.getInstance().getBot().getDiscordLink().getId()))
            {
                if (!e.getAuthor().isBot())
                {
                    if (msg.startsWith("/"))
                    {
                        DiscordCommandSender discordSender = new DiscordCommandSender(e.getAuthor().getId(), false);
                        int prefixLength = 1;
                        if (msg.startsWith("/mp:"))
                        {
                            discordSender.sendMP = true;
                            prefixLength = "/mp:".length();
                        }
                        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(discordSender, msg.substring(prefixLength));
                    } else
                    {
                        if (DiscordImpl.getInstance().getConfigManager().isOP(e.getAuthor().getId()) || RolePlayChatHooks.canSpeakHRP(FMLCommonHandler.instance().getMinecraftServerInstance(), FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0)))
                        {
                            final TextComponentString comp = new TextComponentString("§3[§9Discord§3] §b" + e.getAuthor().getName() + ": " + e.getMessage().getContentRaw());
                            FMLCommonHandler.instance().getMinecraftServerInstance().sendMessage(comp);
                            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(p ->
                            {
                                if (Databases.getPlayerData(p).getInteger(Keldaria.MOD_ID + ":hideGlobalChat") != 1)
                                {
                                    p.sendMessage(comp);
                                }
                            });
                        }
                        else
                        {
                            DiscordImpl.getInstance().getBot().sendMessageOnDiscordLinkChannel("Désolé! Dû au grand nombre de joueurs, le chat HRP est désactivé pour éviter le méta-gaming. (/list pour voir les connectés)");
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e)
    {
        super.onGuildMemberJoin(e);
        e.getGuild().getTextChannelById("414865689015353366").sendMessage("Bienvenue à toi " + e.getMember().getAsMention() + " si tu as des questions, je t'invite à lire le " + e.getGuild().getTextChannelById("414882519922638849").getAsMention() + ". Tu y trouveras tout ce dont tu as besoin. Si toutefois tu as d'autres questions ---> " + e.getGuild().getTextChannelById("426340418502787072").getAsMention() + ".").complete();
    }
}
