/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.model.custom.ModelPainting;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCustomPainting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityCustomPaintingRenderer extends TileEntitySpecialRenderer<TileEntityCustomPainting>
{

    private ModelPainting model = new ModelPainting();

    @Override
    public void render(TileEntityCustomPainting te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + te.getHeight(), z);
        EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
        if(facing == EnumFacing.EAST)
        {
            GlStateManager.rotate(180 + 90, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.05);
            GlStateManager.translate(1, 0, 0);
        }
        else if(facing == EnumFacing.WEST)
        {
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(0, 0, 0.95);
        }
        else if(facing == EnumFacing.NORTH)
        {
            GlStateManager.translate(0, 0, 0.95);
            GlStateManager.translate(1, 0, 0);
        }
        else if (facing == EnumFacing.SOUTH)
        {
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.translate(0, 0, -0.05);
        }
        //GlStateManager.glNormal3f(1.0f, 1.0f, 1.0f);
        //GlStateManager.glNormal3f(0.0F, 0.0F, -0.010416667F);
        GlStateManager.color(1, 1, 1, 1);
        //GlStateManager.disableLighting();
        //GlStateManager.enableBlend();
        //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //GlStateManager.enableLight(1);

        //this.setLightmapDisabled(false);
        if(te.getLink() != null)
        {



            GlStateManager.pushMatrix();
            GlStateManager.scale(te.getWidth(), te.getHeight(), 1);
            ResourceLocation toDraw = ImageCache.getAsResourceLocation(te.getLink(), ImageCache.getBlank());
            if(toDraw != null)
            {
                this.model.render(toDraw);
            }
            GlStateManager.popMatrix();

        }
        //this.setLightmapDisabled(false);
        //GlStateManager.enableLighting();
        //GlStateManager.disableBlend();
        //GlStateManager.disableLight(1);
        GlStateManager.popMatrix();
    }

}
