/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.handler;

import fr.nathanael2611.keldaria.mod.server.chat.RolePlayChatHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatHandler
{

    @SubscribeEvent
    public void onChat(ServerChatEvent event)
    {
        event.setCanceled(true);
        RolePlayChatHooks.send(event);
    }

}
