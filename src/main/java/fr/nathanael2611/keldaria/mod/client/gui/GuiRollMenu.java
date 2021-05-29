package fr.nathanael2611.keldaria.mod.client.gui;

import com.google.common.collect.Lists;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.GuiSlider;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.textarea.GuiLabel;
import fr.reden.guiapi.component.textarea.GuiTextArea;
import fr.reden.guiapi.component.textarea.GuiTextField;
import fr.reden.guiapi.event.listeners.IKeyboardListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;

public class GuiRollMenu extends GuiFrame
{

    public GuiRollMenu()
    {
        super(0, 0, 176, 166);
        setBackgroundColor(0);
        ResourceLocation loc = new ResourceLocation("keldaria", "textures/gui/rollgui.png");
        setTexture(new GuiTextureSprite(loc, 0, 0, 176, 166));


        GuiTextArea rollDesc = new GuiTextArea(5, 30, 160, 45);
        add(rollDesc);

        List<String> numbers = Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        GuiTextField minValue = new GuiTextField(90, 80 - 1, 60, 12);
        minValue.setText("0");
        add(minValue);


        GuiTextField maxValue = new GuiTextField(90, 90 + 2 - 1, 60, 12);
        maxValue.setText("100");
        add(maxValue);



        GuiSlider radiusSlider = new GuiSlider(5, 115, 160, 10, true);
        radiusSlider.setMax(100);
        add(radiusSlider);
        GuiLabel label = new GuiLabel(5, 128, 160, 10);
        label.setBackgroundColor(0);
        label.setEnabledTextColor(Color.BLACK.getRGB());
        label.setShadowed(false);
        label.addTickListener(() -> {
            label.setText(radiusSlider.getValue() > 0 ? (int) radiusSlider.getValue()+ " blocks" : "Tout le serveur");
        });
        add(label);


        GuiButton button = new GuiButton( 5, 140, 150, 20, "Lancer le dé");
        button.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            mc.player.sendChatMessage(String.format("/roll %s %s %s %s", ((int) radiusSlider.getValue()), minValue.getText(), maxValue.getText(), rollDesc.getText()));
            guiClose();
        });
        add(button);
    }


    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);
        mc.fontRenderer.drawString("§8§nFaire un roll", x + 5, y + 5, Color.BLACK.getRGB());
        mc.fontRenderer.drawString("Description & Conditions:", x + 5, y + 20, Color.BLACK.getRGB(), false);
        mc.fontRenderer.drawString("Valeur Minimum:", x + 5, y + 80, Color.BLACK.getRGB(), false);
        mc.fontRenderer.drawString("Valeur Maximum:", x + 5, y + 90 + 2, Color.BLACK.getRGB(), false);
        mc.fontRenderer.drawString("Portée: " , x + 5, y + 105, Color.BLACK.getRGB(), false);
    }

    @Override
    public void resize(int width, int height)
    {
        this.setX((width - this.getWidth()) / 2);
        this.setY((height - this.getHeight()) / 2);
        super.resize(width, height);
    }

}
