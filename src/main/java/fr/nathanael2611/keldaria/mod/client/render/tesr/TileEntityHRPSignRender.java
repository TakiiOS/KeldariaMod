package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityHRPSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import java.awt.*;

public class TileEntityHRPSignRender extends TileEntitySpecialRenderer<TileEntityHRPSign>
{

    @Override
    public void render(TileEntityHRPSign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if(Minecraft.getMinecraft().gameSettings.hideGUI) return;

        GlStateManager.enableAlpha();
        int lines = RenderHelpers.drawCenteredStringMultiLine(te.getText(), 100, -2000, -2000 ,new Color(0, 0, 0, 0).getRGB());
        GlStateManager.disableAlpha();
        Minecraft mc = Minecraft.getMinecraft();
        final float factor = 0.01f;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.5 + (getFontRenderer().FONT_HEIGHT / 2) * factor, z + 0.5);
        GlStateManager.glNormal3f(1.0f, 1.0f, 1.0f);
        GlStateManager.scale(-factor * 1, -factor * 1, -factor * 1);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableLighting();
        GlStateManager.translate(0, - (lines / 2) * getFontRenderer().FONT_HEIGHT, 0);
        this.setLightmapDisabled(true);
        RenderHelpers.drawCenteredStringMultiLine(te.getText(), 100, 0, 0 ,te.getColor());
        GlStateManager.translate(0, -0.3, -0.1);
        Gui.drawRect(0 - 100 / 2, 0, 100 / 2, lines * getFontRenderer().FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
        this.setLightmapDisabled(false);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

}
