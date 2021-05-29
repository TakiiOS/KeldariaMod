package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketActionHomingPigeon;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.textarea.GuiTextField;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiHomingPigeon extends GuiFrame
{

    private static final GuiTextureSprite TEXTURE = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/homing_pigeon.png"), 200, 166, 0, 0, 200, 166);



    public GuiHomingPigeon(int pigeonId, String loc1, String loc2)
    {
        super(0, 0, 200, 166);
        this.setTexture(TEXTURE);
        this.setBackgroundColor(0);
        this.setPauseGame(false);

        GuiTextField loc1Field = new GuiTextField(9, 35, 87, 20);
        loc1Field.setText(loc1);
        this.add(loc1Field);

        GuiButton setLoc1Button = new GuiButton(9, 35 + 20, 87, 20, "Définir");
        setLoc1Button.addClickListener((mouseX, mouseY, mouseButton) -> KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionHomingPigeon(pigeonId, 1, loc1Field.getText())));
        this.add(setLoc1Button);

        GuiButton sendLoc1 = new GuiButton(9, 35 + 20 + 30, 87, 20, "Envoyer");
        sendLoc1.addClickListener((mouseX, mouseY, mouseButton) -> KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionHomingPigeon(pigeonId, 1)));
        this.add(sendLoc1);

        GuiTextField loc2Field = new GuiTextField(104, 35, 87, 20);
        loc2Field.setText(loc2);
        this.add(loc2Field);

        GuiButton setLoc2Button = new GuiButton(104, 35 + 20, 87, 20, "Définir");
        setLoc2Button.addClickListener((mouseX, mouseY, mouseButton) -> KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionHomingPigeon(pigeonId, 2, loc2Field.getText())));
        this.add(setLoc2Button);

        GuiButton sendLoc2 = new GuiButton(104, 35 + 20 + 30, 87, 20, "Envoyer");
        sendLoc2.addClickListener((mouseX, mouseY, mouseButton) -> KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionHomingPigeon(pigeonId, 2)));
        this.add(sendLoc2);



    }


    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);
        //GuiScreen.drawRect(getScreenX() + 96, getScreenY() +20, getScreenX() + 96 + 96, getScreenY() + 20 + 138, Color.decode("#130f40").getRGB());
        mc.fontRenderer.drawString("Pigeon-Voyageur:", getScreenX() + 10, getScreenY() + 6, Color.BLACK.getRGB());
        mc.fontRenderer.drawString("Destination 1:", getScreenX() + 10, getScreenY() + 26, Color.DARK_GRAY.getRGB());
        mc.fontRenderer.drawString("Destination 2:", getScreenX() + 105, getScreenY() + 26, Color.DARK_GRAY.getRGB());

    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }

}
