package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.block.furniture.TileEntityChessPlate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityChessPlateRenderer extends TileEntitySpecialRenderer<TileEntityChessPlate>
{

    @Override
    public void render(TileEntityChessPlate te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.pushMatrix();
        try{
            GlStateManager.translate(x, y+0.115, z);

            EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
            if(facing == EnumFacing.EAST)
            {
                GlStateManager.rotate(0, 0, 1, 0);
                GlStateManager.translate(0, 0, 1);
            }
            else if(facing == EnumFacing.WEST)
            {
                GlStateManager.rotate(180 + 90, 0, 1, 0);
                GlStateManager.translate(0, 0, -1);
            }
            else if(facing == EnumFacing.NORTH)
            {
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.translate(-1, 0, 0);
                GlStateManager.translate(0, 0, 1);
            }
            else if (facing == EnumFacing.SOUTH)
            {
            }

            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableLight(1);
            this.setLightmapDisabled(false);
            GlStateManager.scale(0.1, 0.1, 0.1);
            GlStateManager.scale(1.25, 1.25, 1.25);
            GlStateManager.scale(1,1, -1);

            for (int xx = 7; xx >= 0; xx--)
            {
                for (int yy = 0; yy < 8; yy++)
                {
                    ItemStack stack = te.getStackInSlot((xx * 8) + yy);
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(xx, 0, yy);
                    GlStateManager.translate(1.25/2, 0, 1.25/2);
                    GlStateManager.rotate(90, 0, 1, 0);

                    GlStateManager.scale(2, 2, 2);
                    if(!stack.isEmpty())
                    {
                        Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.GROUND);
                    }
                    GlStateManager.popMatrix();
                }
            }
            this.setLightmapDisabled(true);
        } catch(Exception ignored)
        {
        }
        GlStateManager.popMatrix();

    }
}
