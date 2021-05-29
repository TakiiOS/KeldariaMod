package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiAptitudes extends GuiFrame
{

    public EnumAptitudes selectedAptitude;

    public GuiAptitudes()
    {
        super(0, 0, 200, 166);
        setTexture(new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/aptitudes.png"), 200, 166, 0, 0, 200, 166));
        setBackgroundColor(0);
        GuiScrollPane pane = new GuiScrollPane(9, 21, 81, 136);
        pane.setBackgroundColor(0);
        int y = 0;
        for (EnumAptitudes value : EnumAptitudes.values())
        {
            pane.add(new GuiAptitudesBar(0, y, 81, 25, value));
            y += 26;
        }
        add(pane);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {

        super.drawForeground(mouseX, mouseY, partialTicks);
        //GuiScreen.drawRect(getScreenX() + 96, getScreenY() +20, getScreenX() + 96 + 96, getScreenY() + 20 + 138, Color.decode("#130f40").getRGB());
        mc.fontRenderer.drawStringWithShadow("Aptitudes: ", getScreenX() + 10, getScreenY() + 6, Color.GRAY.getRGB());
        if(mc.fontRenderer != null && selectedAptitude != null)
        {

            mc.fontRenderer.drawStringWithShadow(String.format("Infos %s:", selectedAptitude.getDisplayName()), getScreenX() + 100, getScreenY() + 6, Color.GRAY.getRGB());
            GlStateManager.pushMatrix();
            GlStateManager.translate(getScreenX() + 98, getScreenY() +23, 0);
            GlStateManager.scale(0.75, 0.75, 1.0f);
            mc.fontRenderer.drawSplitString(
                    this.selectedAptitude.getDescriptionFor(selectedAptitude.getPoints(mc.player)),
                    0, 0, (int) ((int) 70 * 1.75),
                    Color.WHITE.getRGB());
            GlStateManager.popMatrix();
        }

    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }

    public class GuiAptitudesBar extends GuiComponent
    {

        public final GuiTextureSprite STAR_EMPTY = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/stars.png"), 32, 16, 0, 0, 16, 16);
        public final GuiTextureSprite STAR_FILL = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/stars.png"), 32, 16, 16, 0, 16, 16);


        private EnumAptitudes abilities;
        private int points;

        public GuiAptitudesBar(int x, int y, int width, int height, EnumAptitudes ability)
        {
            super(x, y, width, height);
            this.abilities = ability;
            this.points = ability.getPoints(mc.player);
            setBackgroundColor(Color.DARK_GRAY.getRGB());
            addClickListener((mouseX, mouseY, mouseButton) ->
            {
               selectedAptitude = abilities;
            });
        }


        @Override
        public void drawForeground(int mouseX, int mouseY, float partialTicks)
        {
            super.drawForeground(mouseX, mouseY, partialTicks);
            mc.fontRenderer.drawStringWithShadow(abilities.getDisplayName(), getScreenX() + 1, getScreenY() + 1, (isHovered() ? Color.YELLOW : Color.WHITE).getRGB());
            GlStateManager.color(255, 255, 255);
            int x = 1;
            for (int i = 1; i <= abilities.getMaxPoints(); i++)
            {
                (points >= i ? STAR_FILL : STAR_EMPTY).drawSprite(getScreenX() + x, getScreenY() + 10, 12, 12);
                x += 12;
            }
            setBackgroundColor(selectedAptitude == abilities ? Color.decode("#d1ccc0").getRGB() : 0);
        }
    }

}
