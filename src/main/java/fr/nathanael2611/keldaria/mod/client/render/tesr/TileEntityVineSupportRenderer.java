/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.client.model.ModelSimpleBlockModel;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityVineSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityVineSupportRenderer extends TileEntitySpecialRenderer<TileEntityVineSupport>
{

    private ModelSimpleBlockModel model = new ModelSimpleBlockModel();

    @Override
    public void render(TileEntityVineSupport te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();
        this.setLightmapDisabled(false);
        if(te.isActive())
        {
            GlStateManager.pushMatrix();
            double growTime = te.LEAVES_GROW_TIME;
            long diff = (System.currentTimeMillis() - te.getPlantedTime());
            double percent = Math.min((diff / growTime) * 100, 100);
            float scale = (float) ((percent / 1.2) * 0.01);
            GlStateManager.translate(x , y, z);

            EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
            if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH)
            {
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(90, 0, 1, 0);
            }

            {
                GlStateManager.pushMatrix();

                GlStateManager.translate(0.5, 0, 0);
                GlStateManager.translate(0, 0, 0.5);
                GlStateManager.translate(0, -0.3, 0);
                GlStateManager.scale(0.5, 4, 0.5);
                Minecraft.getMinecraft().getItemRenderer().renderItem(
                        Minecraft.getMinecraft().player, new ItemStack(Blocks.LOG, 1, 0), ItemCameraTransforms.TransformType.GROUND);

                GlStateManager.popMatrix();
            }

            GlStateManager.translate(0.5 - (scale / 4), 0.8 - scale /2, 0.5 - scale / 2);
            GlStateManager.scale(scale / 2, scale, scale );

            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0);
                GlStateManager.translate(0, 0, 0.5);
                GlStateManager.scale(3.3, 3.3, 3.3);
                Minecraft.getMinecraft().getItemRenderer().renderItem(
                        Minecraft.getMinecraft().player, new ItemStack(Blocks.LEAVES), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.popMatrix();
            }

            GlStateManager.scale(2, 1, 1 );
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(0, 0.5, 0);
            GlStateManager.translate(-0.5, 0, 0);
            int rest =(int) ((int) te.getPos().getX() - te.getPos().getZ()) % 2;
            if(te.isGrapesGrowed())
            {
                for(int i = 0; i < 2; i ++)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(rest == 0 ? 0.2 : -0.2, rest == 0 ? -0.3 : -0.4, 0);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, new ItemStack(KeldariaItems.GRAPES), ItemCameraTransforms.TransformType.GROUND);
                    GlStateManager.translate(rest == 0 ? -0.4 : 0.2, rest == 0 ? 0.4 : 0.5, 0);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, new ItemStack(KeldariaItems.GRAPES), ItemCameraTransforms.TransformType.GROUND);
                    GlStateManager.popMatrix();
                    GlStateManager.translate(0, 0, 0.5);
                }
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();

    }
}
