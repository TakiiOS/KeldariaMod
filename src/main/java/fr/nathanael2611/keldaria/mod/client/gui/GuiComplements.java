package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import fr.reden.guiapi.component.textarea.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiComplements extends GuiFrame
{

    public GuiComplements()
    {
        super(0, 0, 200, 166);
        setTexture(new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/complements.png"), 200, 166, 0, 0, 200, 166));
        setBackgroundColor(0);
        GuiScrollPane pane = new GuiScrollPane(9, 21, 200, 136);
        pane.setBackgroundColor(0);
        int y = 10;
        for (EnumComplement value : EnumComplement.values())
        {
            if(value.has(mc.player))
            {
                pane.add(new GuiLabel(0, y, 200, 25, "  - " + value.getFormattedName()));
                y += 15;
            }
        }
        add(pane);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);
        //GuiScreen.drawRect(getScreenX() + 96, getScreenY() +20, getScreenX() + 96 + 96, getScreenY() + 20 + 138, Color.decode("#130f40").getRGB());
        mc.fontRenderer.drawStringWithShadow("Compléments: " + EnumComplement.getTheoricMaxComplements(mc.player) + " points au total", getScreenX() + 10, getScreenY() + 6, Color.GRAY.getRGB());


    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }


}
