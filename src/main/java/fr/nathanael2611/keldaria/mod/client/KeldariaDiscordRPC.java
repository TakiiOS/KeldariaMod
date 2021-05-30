/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class KeldariaDiscordRPC
{

    public static void main(String[] args)
    {
        start();
    }


    public static void start()
    {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        // 639235594962206733 466556379545600001
        lib.Discord_Initialize("639235594962206733", handlers, true, "");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() /1000;
        presence.details = "RP Médiéval Fantastique";
        presence.largeImageKey = "logo";
        presence.state = "";
        lib.Discord_UpdatePresence(presence);
        new Thread(()->{
            while (!Thread.currentThread().isInterrupted()){
                lib.Discord_RunCallbacks();
                try {

                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "RPC-Keldaria").start();
    }

}
