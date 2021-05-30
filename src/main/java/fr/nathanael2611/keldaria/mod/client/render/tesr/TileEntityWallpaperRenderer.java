/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.model.custom.ModelPainting;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityWallpaper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityWallpaperRenderer extends TileEntitySpecialRenderer<TileEntityWallpaper>
{

    private ModelPainting model = new ModelPainting();

    @Override
    public void render(TileEntityWallpaper te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        for (BlockPos blockPos : te.getPaintedFaces().keySet())
        {
            GlStateManager.pushMatrix();

            List<EnumFacing> faces = te.getPaintedFaces().get(blockPos);

            GlStateManager.translate(x - (te.getPos().getX() - blockPos.getX()), y - (te.getPos().getY() - blockPos.getY()) + 1, z - (te.getPos().getZ() - blockPos.getZ()));

            for (int i = 0; i < faces.size(); i++)
            {
                EnumFacing facing = faces.get(i);
                facing = facing.getOpposite();

                GlStateManager.pushMatrix();
                //EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
                if(facing == EnumFacing.EAST)
                {
                    GlStateManager.rotate(180 + 90, 0, 1, 0);
                    GlStateManager.translate(0, 0, 0.01);
                    GlStateManager.translate(1, 0, 0);
                }
                else if(facing == EnumFacing.WEST)
                {
                    GlStateManager.rotate(90, 0, 1, 0);
                    GlStateManager.translate(0, 0, 1.01);
                }
                else if(facing == EnumFacing.NORTH)
                {
                    GlStateManager.translate(0, 0, 1.01);
                    GlStateManager.translate(1, 0, 0);
                }
                else if (facing == EnumFacing.SOUTH)
                {
                    GlStateManager.rotate(180, 0, 1, 0);
                    GlStateManager.translate(0, 0, 0.01);
                }
                else if(facing == EnumFacing.DOWN)
                {
                    GlStateManager.translate(0, 0.01, 0);
                    GlStateManager.translate(0, 0, 1);
                    GlStateManager.translate(1, 0, 0);
                    GlStateManager.rotate(90, 1, 0, 0);
                }
                else if(facing == EnumFacing.UP)
                {
                    GlStateManager.translate(0, -1.01, 0);
                    GlStateManager.translate(0, 0, 1);
                    GlStateManager.translate(1, 0, 0);
                    GlStateManager.rotate(90, 1, 0, 0);
                }

                GlStateManager.color(1, 1, 1, 1);

                if(te.getWallPaper() != null)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1, 1, 1);
                    ResourceLocation toDraw = ImageCache.getAsResourceLocation(te.getWallPaper(), ImageCache.getBlank());
                    if(toDraw != null)
                    {
                        this.model.render(toDraw);
                    }
                    GlStateManager.popMatrix();

                }
                GlStateManager.popMatrix();

            }
            GlStateManager.popMatrix();
        }

    }

    @Override
    public boolean isGlobalRenderer(TileEntityWallpaper te)
    {
        return super.isGlobalRenderer(te);
    }
}
