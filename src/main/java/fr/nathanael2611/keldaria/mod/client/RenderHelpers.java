/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

public class RenderHelpers
{


    @SideOnly(Side.CLIENT)
    public static void drawSeamlessCuboid(final float minU, final float maxU, final float minV, final float maxV, final double z_size, final double y_size, final double x_size, final double scale)
    {
        drawSeamlessCuboidAt(0, 0, 0, minU, maxU, minV, maxV, x_size, y_size, z_size, scale);
    }

    public static void drawSeamlessCuboidAt(final double x, final double y, final double z, final float minU, final float maxU, final float minV, final float maxV, final double x_size, final double y_size, final double z_size, final double scale)
    {

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        final double hlfU = minU + ((maxU - minU) / 2);
        final double hlfV = minV + ((maxV - minV) / 2);

        final double centre = 0d;

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // UP
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();

        // DOWN
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();

        // LEFT
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();

        // RIGHT
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();

        // BACK BOTTOM
        bufferbuilder.pos(-x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, centre, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, centre, -z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, -y_size + y, -z_size + z).color(1, 0, 0, 1).endVertex();

        // BACK TOP
        bufferbuilder.pos(-x_size + x, centre, -z_size + z).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, -z_size + z).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, -z_size + z).endVertex();
        bufferbuilder.pos(x_size + x, centre, -z_size + z).endVertex();

        // FRONT BOTTOM
        bufferbuilder.pos(x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, centre, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, centre, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, -y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();

        // FRONT TOP
        bufferbuilder.pos(x_size + x, centre, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, y_size + y, z_size + z).color(1, 0, 0, 1).endVertex();
        bufferbuilder.pos(-x_size + x, centre, z_size + z).color(1, 0, 0, 1).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();
    }

    public static void renderCenteredText(String text, int posX, int posY, int par4)
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, par4);
    }

    public static void renderInventorySlot(int slot, int x, int y, float partialTicks, boolean isJustOne)
    {


        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        ItemStack itemstack = Minecraft.getMinecraft().player.inventory.mainInventory.get(slot);
        if (itemstack != null)
        {
            float f1 = (float) itemstack.getAnimationsToGo() - partialTicks;
            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float) (x + 8), (float) (y + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }
            ItemStack fits = new ItemStack(itemstack.getItem(), 1);
            if (isJustOne)
            {
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, fits, x, y, null);
            } else
            {
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, itemstack, x, y, null);
            }

        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);


    }

    public static void renderItemStack(ItemStack stack, int x, int y, float partialTicks, boolean isJustOne)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        if (stack != null)
        {
            float f1 = (float) stack.getAnimationsToGo() - partialTicks;
            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float) (x + 8), (float) (y + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            if (f1 > 0.0F) GL11.glPopMatrix();
            ItemStack fits = new ItemStack(stack.getItem(), 1);
            if (isJustOne)
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, fits, x, y, null);
            else
                Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, stack, x, y, null);
        }
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }


    public static void drawImage(double x, double y, ResourceLocation image, double width, double height)
    {
        drawImage(x, y, image, width, height, 1.0f);
    }

    public static void drawImage(double x, double y, ResourceLocation image, double width, double height, float alpha)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.enableAlpha();
        GL11.glEnable(3042);
        GL11.glEnable(2832);
        GL11.glHint(3153, 4353);
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex(1, 1).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex(1, 0).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(0, 0).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2832);
    }


    public static void drawStringMultiLine(String text, int maxLineWidth, int x, int y, int color)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int lineWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);

        if (lineWidth < maxLineWidth)
        {
            Minecraft.getMinecraft().fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
        } else
        {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++)
            {
                if (fontRenderer.getStringWidth(currentLine + words[i]) < maxLineWidth)
                {
                    currentLine += " " + words[i];
                } else
                {
                    fontRenderer.drawString(currentLine, x, y, color);
                    y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0)
            {
                Minecraft.getMinecraft().fontRenderer.drawString(currentLine, x, y, color);
            }
        }
    }

    public static int drawCenteredStringMultiLine(String text, int maxLineWidth, int x, int y, int color)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int lineWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        int totalLines = 1;

        if (lineWidth < maxLineWidth)
        {
            drawCenteredString(fontRenderer, text, x, y, color);
        } else
        {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++)
            {
                if (fontRenderer.getStringWidth(currentLine + words[i]) < maxLineWidth)
                {
                    currentLine += " " + words[i];
                } else
                {
                    drawCenteredString(fontRenderer, currentLine, x, y, color);
                    y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                    currentLine = words[i];
                    totalLines++;
                }
            }
            if (currentLine.trim().length() > 0)
            {
                drawCenteredString(fontRenderer, currentLine, x, y, color);
            }
        }
        return totalLines;
    }


    public static int coutLinesCenteredStringMultiLine(String text, int maxLineWidth)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int lines = 0;
        int lineWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);

        if (lineWidth < maxLineWidth)
        {
            //drawCenteredString(fontRenderer, text, x, y, color);
            return 0;
        } else
        {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++)
            {
                if (fontRenderer.getStringWidth(currentLine + words[i]) < maxLineWidth)
                {
                    currentLine += " " + words[i];
                } else
                {
                    //drawCenteredString(fontRenderer, currentLine, x , y, color);
                    lines += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0)
            {
                //drawCenteredString(fontRenderer, currentLine, x , y, color);
            }
        }
        return lines;
    }


    public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y, color);
    }


    public static void drawScaledString(FontRenderer fontRenderer, String string, int x, int y, float scale, int color, boolean dropShadow)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1.0f);
        if (fontRenderer != null) fontRenderer.drawString(string, 0, 0, color, dropShadow);
        GlStateManager.popMatrix();
    }

    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int maxWidth, int color)
    {
        drawScaledCenteredString(fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color);
    }

    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color)
    {
        float originalWidth = fontRenderer.getStringWidth(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(fontRenderer, string, x, y, width, scale, color);
    }

    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int width, float scale, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.getStringWidth(string);
        int titleHeight = fontRenderer.FONT_HEIGHT;
        fontRenderer.drawString(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        GlStateManager.popMatrix();
    }

    public static void drawLinearGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel)
    {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }


    public static BufferedImage superpose(Collection<BufferedImage> images)
    {
        int width = 0;
        int height = 0;
        for (BufferedImage img : images)
        {
            if (img == null) return null;
            width = Math.max(img.getWidth(), width);
            height = Math.max(img.getHeight(), height);
        }
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        images.forEach(img -> g2.drawImage(img, 0, 0, buffer.getWidth(), buffer.getHeight(), null));
        g2.dispose();
        return buffer;
    }

    public static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2)
    {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight())
        {
            for (int x = 0; x < img1.getWidth(); x++)
            {
                for (int y = 0; y < img1.getHeight(); y++)
                {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) return false;
                }
            }
        } else
        {
            return false;
        }
        return true;
    }


/*
    public static void renderRotatedBox(OrientedBB bb, float r, float g, float b)
    {

        Vec3d bottomSouthEast = bb.getCorner(BoxCorner.BottomSouthEast);
        Vec3d bottomSouthWest = bb.getCorner(BoxCorner.BottomSouthWest);
        Vec3d bottomNorthEast = bb.getCorner(BoxCorner.BottomNorthEast);
        Vec3d bottomNorthWest = bb.getCorner(BoxCorner.BottomNorthWest);
        Vec3d topSouthEast = bb.getCorner(BoxCorner.TopSouthEast);
        Vec3d topSouthWest = bb.getCorner(BoxCorner.TopSouthWest);
        Vec3d topNorthEast = bb.getCorner(BoxCorner.TopNorthEast);
        Vec3d topNorthWest = bb.getCorner(BoxCorner.TopNorthWest);


        GlStateManager.pushMatrix();

        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        renderLine(bottomSouthEast, bottomNorthEast, r, g, b);
        renderLine(bottomNorthWest, bottomNorthEast, r, g, b);
        renderLine(bottomNorthWest, bottomSouthWest, r, g, b);
        renderLine(bottomSouthWest, bottomSouthEast, r, g, b);

        renderLine(bottomSouthEast, topSouthEast, r, g, b);
        renderLine(bottomSouthWest, topSouthWest, r, g, b);
        renderLine(bottomNorthEast, topNorthEast, r, g, b);
        renderLine(bottomNorthWest, topNorthWest, r, g, b);

        renderLine(topSouthEast, topNorthEast, r, g, b);
        renderLine(topNorthWest, topNorthEast, r, g, b);
        renderLine(topNorthWest, topSouthWest, r, g, b);
        renderLine(topSouthWest, topSouthEast, r, g, b);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);


        GlStateManager.popMatrix();

    }*/

    public static void renderLine(Vec3d posA, Vec3d posB, float r, float g, float b)
    {
        GlStateManager.pushMatrix();
        //EntityPlayer player = Minecraft.getMinecraft().player;
        //GlStateManager.translate(-player.posX, -player.posY, -player.posZ);

        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        GlStateManager.glLineWidth(2);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(r, g, b, 1).endVertex();          //A
        bufferBuilder.pos(posB.x, posB.y, posB.z).color(r, g, b, 1).endVertex();       //B
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);

        GlStateManager.popMatrix();
    }


    public static void fillRect(int x, int y, int width, int height, int color)
    {
        Gui.drawRect(x, y, x + width, y + height, color);
    }


    public static void drawEntityWithYaw(int posX, int posY, int scale, float yaw, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        //GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = yaw;
        ent.rotationYaw = yaw;
        //ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

}
