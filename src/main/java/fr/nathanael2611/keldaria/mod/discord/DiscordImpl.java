/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.discord;

import fr.nathanael2611.keldaria.mod.discord.bot.KeldaBot;
import fr.nathanael2611.keldaria.mod.discord.impl.DiscordConfigManager;

import javax.security.auth.login.LoginException;

public class DiscordImpl
{

    private static DiscordImpl instance;

    private KeldaBot bot;

    private DiscordConfigManager configManager;

    public static DiscordImpl getInstance()
    {
        if(instance == null) instance = new DiscordImpl();
        return instance;
    }

    public void initBot() throws LoginException
    {
        this.getConfigManager().readConfig();
        if(this.bot != null)
        {
            if(this.bot.getJda() != null)
            {
                this.bot.getJda().shutdown();
            }
        }
        bot = new KeldaBot(configManager.getToken());
        // -> MinecraftForge.EVENT_BUS.register(new DiscordHooks());
    }

    public KeldaBot getBot() {
        return bot;
    }

    public DiscordConfigManager getConfigManager()
    {
        if(configManager == null) configManager = new DiscordConfigManager();
        return configManager;
    }

    public boolean hasBot()
    {
        return bot != null && bot.isReady;
    }



}
