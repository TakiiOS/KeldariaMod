package fr.nathanael2611.keldaria.mod.client.gui.button;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class GuiButtonKeyCategory extends GuiButtonFullgood
{
    private String categoryName;
    public GuiButtonKeyCategory(String categoryName, int x, int y, int widthIn, int heightIn, String buttonText, Color bgColor, Color color)
    {
        super(0, x, y, widthIn, heightIn, buttonText, bgColor, color);
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
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

        GlStateManager.pushMatrix();
        //drawString(mc.fontRenderer, displayString, x + 5, y + ((height / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)), color);
        RenderHelpers.drawScaledString(mc.fontRenderer, displayString, x + 5, y + ((height / 2 ) - (mc.fontRenderer.FONT_HEIGHT / 2)), 0.7f, color, true);

        GlStateManager.popMatrix();

    }

}
