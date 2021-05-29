package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFeeder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityFeederRenderer extends TileEntitySpecialRenderer<TileEntityFeeder>
{

    @Override
    public void render(TileEntityFeeder te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te.getContentType() != TileEntityFeeder.ContentType.NONE)
        {
            double percent = te.getPercent();
            float factor = 0.8f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.1, y + 0.3 + ((percent / 100) * 0.55), z + 0.1);
            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.glNormal3f(1.0f, 1.0f, 1.0f);
            GlStateManager.scale(factor, factor, factor);
            GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableLighting();
            ResourceLocation location = te.getContentType() == TileEntityFeeder.ContentType.WATER ? new ResourceLocation("textures/blocks/water_flow.png") : new ResourceLocation("textures/blocks/hay_block_top.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(location);
            RenderHelpers.drawImage(0, 0, location, 1, 1);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

    }
}