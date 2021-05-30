/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiTextureSprite {

    private final ResourceLocation atlasTexture;
    private final int atlasWidth, atlasHeight;

    private final int textureU, textureV;
    private final int textureWidth, textureHeight;

    public GuiTextureSprite(ResourceLocation texture, int textureU, int textureV, int textureWidth, int textureHeight) {
        this(texture, 256, 256, textureU, textureV, textureWidth, textureHeight);
    }

    public GuiTextureSprite(ResourceLocation atlasTexture, int atlasWidth, int atlasHeight, int textureU, int textureV, int textureWidth, int textureHeight) {
        this.atlasTexture = atlasTexture;
        this.atlasWidth = atlasWidth;
        this.atlasHeight = atlasHeight;
        this.textureU = textureU;
        this.textureV = textureV;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public void drawSprite(int x, int y, int spriteWidth, int spriteHeight) {
        drawSprite(x, y, spriteWidth, spriteHeight, spriteWidth, spriteHeight);
    }

    public void drawSprite(int x, int y, int spriteWidth, int spriteHeight, int width, int height) {
        drawSprite(x, y, spriteWidth, spriteHeight, 0, 0, textureWidth, textureHeight, width, height);
    }

    public void drawSprite(int x, int y, int spriteWidth, int spriteHeight, int uOffset, int vOffset, int textureWidth, int textureHeight, int width, int height)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(atlasTexture);
        double nRepeatY = height / (double) spriteHeight;
        int j = 0;

        do {
            int i = 0;
            double nRepeatX = width / (double) spriteWidth;
            double ySize = MathHelper.clamp(nRepeatY, 0, 1);

            do {
                double xSize = MathHelper.clamp(nRepeatX, 0, 1);
                drawTexturedModalRect(x + i * spriteWidth, y + j * spriteHeight, (int) Math.ceil(spriteWidth * xSize), (int) Math.ceil(spriteHeight * ySize), uOffset, vOffset, (int) Math.ceil(textureWidth * xSize), (int) Math.ceil(textureHeight * ySize));
                i++;
                nRepeatX -= 1;

            } while(nRepeatX > 0);

            j++;
            nRepeatY -= 1;

        } while(nRepeatY > 0);
    }

    private void drawTexturedModalRect(int x, int y, int spriteWidth, int spriteHeight, int uOffset, int vOffset, int textureWidth, int textureHeight)
    {
        /*Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        double u1 = (textureU + uOffset) / (double) atlasWidth;
        double u2 = (textureU + uOffset + textureWidth) / (double) atlasWidth;
        double v1 = (textureV + vOffset) / (double) atlasHeight;
        double v2 = (textureV + vOffset + textureHeight) / (double) atlasHeight;
        tessellator.addVertexWithUV(x, y + spriteHeight, 0, u1, v2);
        tessellator.addVertexWithUV(x + spriteWidth, y + spriteHeight, 0, u2, v2);
        tessellator.addVertexWithUV(x + spriteWidth, y, 0, u2, v1);
        tessellator.addVertexWithUV(x, y, 0, u1, v1);
        tessellator.draw();*/
    	Gui.drawScaledCustomSizeModalRect(x, y, textureU+uOffset, textureV+vOffset, textureWidth, textureHeight, spriteWidth, spriteHeight, atlasWidth, atlasHeight);
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

}
