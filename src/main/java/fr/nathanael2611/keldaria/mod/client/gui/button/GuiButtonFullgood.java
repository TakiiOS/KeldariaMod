/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui.button;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class GuiButtonFullgood extends GuiButton
{

    public Color bgColor;
    public Color color;

    public GuiButtonFullgood(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Color bgColor, Color color)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.bgColor = bgColor;
        this.color = color;
    }

    int alpha = 0;

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        //super.drawButton(mc, mouseX, mouseY, partialTicks);
        if(!visible) return;

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int pluser = 4;
        if (hovered)
        {

            if (alpha + pluser <= 200)
            {
                alpha += pluser;
            } else
            {
                alpha = 200;
            }
        } else
        {
            if (alpha - pluser * 2 >= 0)
            {
                alpha -= pluser * 2;
            } else
            {
                alpha = 0;
            }
        }
        GlStateManager.pushMatrix();
        {
            RenderHelpers.drawLinearGradientRect(x, y, x + width, y + height, new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB(), bgColor.getRGB(), zLevel);
        }
        GlStateManager.popMatrix();

        int color = getTextColor();

        drawString(mc.fontRenderer, displayString, x + 5, y + ((height / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)), color);

    }

    public int getTextColor()
    {
        if (hovered)
        {
            return Color.YELLOW.getRGB();
        } else
        {
            return Color.WHITE.getRGB();
        }
    }
}
