/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.voice;

import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import fr.nathanael2611.modularvoicechat.api.VoiceDispatchEvent;
import fr.nathanael2611.modularvoicechat.api.dispatcher.IVoiceDispatcher;
import fr.nathanael2611.modularvoicechat.server.dispatcher.DistanceBasedVoiceDispatcher;
import net.minecraft.entity.player.EntityPlayerMP;

public class KeldariaVoiceDispatcher implements IVoiceDispatcher
{

    private DistanceBasedVoiceDispatcher distanceBasedVoiceDispatcher = new DistanceBasedVoiceDispatcher(15, true);

    @Override
    public void dispatch(VoiceDispatchEvent event)
    {
        if(KeldariaVoices.getSpeakStrength(event.getSpeaker()) == 0)
        {
            event.dispatchToAllExceptSpeaker();
            return;
        }
        if(KeldariaVoices.hearTroughWalls(event.getSpeaker().world))
        {
            this.distanceBasedVoiceDispatcher.dispatch(event);
        }
        else
        {
            for (EntityPlayerMP connectedPlayer : event.getVoiceServer().getConnectedPlayers())
            {
                if (connectedPlayer != event.getSpeaker())
                {
                    int volume = (int) KeldariaVoices.getHearVolume(event.getSpeaker(), connectedPlayer);
                    event.dispatchTo(connectedPlayer, volume, event.getProperties());
                }
            }
        }
    }

}
