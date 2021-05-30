/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui.button;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.GuiCraftingManager;
import fr.nathanael2611.keldaria.mod.crafting.CraftEntry;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class GuiButtonCraft extends GuiButton
{

    public GuiCraftingManager gui;
    public CraftEntry entry;

    public GuiButtonCraft(int x, int y, int widthIn, int heightIn, CraftEntry entry, GuiCraftingManager gui)
    {
        super(x, y, widthIn, heightIn, entry.getResult().getDisplayName());
        this.entry = entry;
        this.gui = gui;
        addTickListener(() ->
        {
            setForegroundColor(gui.selectedCraft == entry ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
        });
        addClickListener((mouseX, mouseY, click) -> gui.selectedCraft = entry);

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks)
    {
        // super.drawBackground(mouseX, mouseY, partialTicks);

    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        int beforeColor = foregroundColor;
        if (isHovered() && entry != gui.selectedCraft)
        {
            foregroundColor = Color.YELLOW.getRGB();
        }
        if (entry == gui.selectedCraft)
        {
            foregroundColor = Color.GREEN.getRGB();
        }
        //super.drawForeground(mouseX, mouseY, partialTicks);

        if (text != null && !text.isEmpty())
        {

            int textX = (getWidth() - mc.fontRenderer.getStringWidth(getText())) / 2;
            int textY = (getHeight() - mc.fontRenderer.FONT_HEIGHT) / 2;

            if (getRenderIcon() != null)
            {
                GuiTextureSprite renderIconTexture = getRenderIcon();
                textX = getTextX(renderIconTexture);
                textY = getTextY(renderIconTexture);
            }

            //RenderHelpers.drawScaledCenteredString(mc.fontRenderer, text, (int) (getScreenX() ), getScreenY() + height / 2, width, 0.5f, getRenderForegroundColor());

            GlStateManager.pushMatrix();
            GlStateManager.translate((int) (getScreenX() + relX + 2), (int) (getScreenY() + relY + 2), 0);
            GlStateManager.scale(0.7f, 0.7, 0.7);
            if (entry == gui.selectedCraft || isHovered())
            {
                double u = gui.selectedCraft == entry ? 1.3 : 1.1;
                int uu = gui.selectedCraft == entry ? -3 : -1;
                GlStateManager.scale(u, u, u);
                GlStateManager.translate(uu, uu, 0);
            }
            RenderHelpers.renderItemStack(entry.getResult(), 0, 0, partialTicks, false);
            GlStateManager.popMatrix();
        }

        foregroundColor = beforeColor;

    }

}
