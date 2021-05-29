package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCardCarpet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntityCardCarpetRenderer extends TileEntitySpecialRenderer<TileEntityCardCarpet>
{
    @Override
    public void render(TileEntityCardCarpet te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.pushMatrix();
        try{
            GlStateManager.translate(x, y+0.03, z);

            EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
            if(facing == EnumFacing.EAST)
            {
                GlStateManager.rotate(90, 0, 1, 0);
                //GlStateManager.translate(0, 0, -0.05);
                GlStateManager.translate(-1, 0, 0);
            }
            else if(facing == EnumFacing.WEST)
            {
                GlStateManager.rotate(180 + 90, 0, 1, 0);
                GlStateManager.translate(0, 0, -1);
            }
            else if(facing == EnumFacing.NORTH)
            {
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.translate(-1, 0, -1);
                //GlStateManager.translate(0, 0, 0.95);
                //GlStateManager.translate(1, 0, 0);
            }
            else if (facing == EnumFacing.SOUTH)
            {
            }


            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableLight(1);

            this.setLightmapDisabled(false);

            GlStateManager.rotate(90,1,0, 0);
            GlStateManager.scale(0.3, 0.3,0.3);

            int i = 0;
            for (String card : te.getCards())
            {
                i++;
                GlStateManager.pushMatrix();

                GlStateManager.translate(0.2, 0.2, 0);
                GlStateManager.scale(0.8, 0.8, 1);
                if(!card.equalsIgnoreCase(""))
                {
                    RenderHelpers.drawImage(0, 0, ImageCache.getAsResourceLocation(card, ImageCache.getBlank()), 1, 1);
                }


                GlStateManager.popMatrix();
                GlStateManager.translate(1, 0, 0);
                if(i == 3)
                {
                    GlStateManager.translate(-3,0, 0);
                    GlStateManager.translate(0,1, 0);
                    i =0;
                }
            }
            this.setLightmapDisabled(true);
        } catch(Exception ignored)
        {
        }
        GlStateManager.popMatrix();

    }




}
