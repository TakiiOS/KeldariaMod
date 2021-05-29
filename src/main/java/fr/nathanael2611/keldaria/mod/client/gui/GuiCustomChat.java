package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiSlider;
import fr.nathanael2611.keldaria.mod.features.ChatBubblesManager;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketChangeVoiceStrength;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketToggleAThing;
import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.io.IOException;

public class GuiCustomChat extends GuiChat
{

    private GuiButton globalChatButton;

    public GuiCustomChat(String defaultStr)
    {
        super(defaultStr);
    }

    public GuiSlider distanceSlider;
    @Override
    public void initGui()
    {
        this.globalChatButton = new GuiButton(0, 10, 10, 200, 20, "");
        this.buttonList.add(this.globalChatButton);
        this.buttonList.add(new GuiButton(1, 10, 30, 200, 20, "Aide Chat RP"));

        this.distanceSlider = new GuiSlider(
                ()->{
                    KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketChangeVoiceStrength((int) this.distanceSlider.getSliderValue()));
                },
                22,
                "Distance", 60, 60, KeldariaVoices.getSpeakStrength(mc.player),
                2f, 30f);

        this.buttonList.add(distanceSlider);

        super.initGui();


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if(this.globalChatButton != null)
        {
            this.globalChatButton.displayString = "Chat HRP: " + (ClientDatabases.getPersonalPlayerData().getInteger(Keldaria.MOD_ID + ":hideGlobalChat") != 1 ? "Activé" : "Désactivé");

        drawRect(8, 55, 220, 55 + 30, new Color(0, 0, 0, 150).getRGB());}

        if(this.distanceSlider != null)
        {
            RenderHelpers.drawScaledString(mc.fontRenderer, "Vocal: ", 10, 65, 1.5f, Color.WHITE.getRGB(), true);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {

        if(button == this.globalChatButton)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_HRP_CHAT, ClientDatabases.getPersonalPlayerData().getInteger(Keldaria.MOD_ID + ":hideGlobalChat") == 1));
        }
        if(button.id == 1)
        {
            mc.player.sendMessage(new TextComponentString("§3§lListe des préfix de chat RP:"));
            mc.player.sendMessage(new TextComponentString("§9 - HRP Global: §7("));
            mc.player.sendMessage(new TextComponentString("§9 - HRP Local: §7["));
            mc.player.sendMessage(new TextComponentString("§9 - Crier: §7!"));
            mc.player.sendMessage(new TextComponentString("§9 - Chuchoter: §7$"));
            mc.player.sendMessage(new TextComponentString("§9 - Parler bas: §7-"));
            mc.player.sendMessage(new TextComponentString("§9 - Parler fort: §7+"));
            mc.player.sendMessage(new TextComponentString("§9 - Action: §7*"));
            mc.player.sendMessage(new TextComponentString("§9 - Action faible-portée: §7*$"));
            mc.player.sendMessage(new TextComponentString("§9 - Narration Locale: §7#"));
            mc.player.sendMessage(new TextComponentString("§9 - Narration Globale: §7% §c[Staff Only]"));
            mc.player.sendMessage(new TextComponentString("§9 - Chat Staff: §7{ §c[Staff Only]"));
            mc.player.sendMessage(new TextComponentString("§9 - Envoyer une demande au saff: §7£"));
        }
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        String playerName = this.mc.player.getName();
        if(this.inputField.getText().length() > 0)
        {
            if(!ChatBubblesManager.getInstance().isWriting(playerName))
            {
                ChatBubblesManager.getInstance().handleWritePacketToServer(true);
            }
        } else {
            if(ChatBubblesManager.getInstance().isWriting(playerName))
            {
                ChatBubblesManager.getInstance().handleWritePacketToServer(false);
            }
        }
        super.keyTyped(typedChar, keyCode);

    }

    @Override
    public void onGuiClosed()
    {
        ChatBubblesManager.getInstance().handleWritePacketToServer(false);

        super.onGuiClosed();
    }
}
