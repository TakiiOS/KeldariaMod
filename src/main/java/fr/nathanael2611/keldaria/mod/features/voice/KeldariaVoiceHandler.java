package fr.nathanael2611.keldaria.mod.features.voice;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketToggleAThing;
import fr.nathanael2611.modularvoicechat.api.StartVoiceRecordEvent;
import fr.nathanael2611.modularvoicechat.api.StopVoiceRecordEvent;
import fr.nathanael2611.modularvoicechat.api.VoiceServerStartEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeldariaVoiceHandler
{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onStartTalking(StartVoiceRecordEvent event)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_TALK, true));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onStopTalking(StopVoiceRecordEvent event)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_TALK, false));
    }

    @SubscribeEvent
    public void onVoiceServerStart(VoiceServerStartEvent event)
    {
        event.changeVoiceDispatcher(new KeldariaVoiceDispatcher());
    }

}
