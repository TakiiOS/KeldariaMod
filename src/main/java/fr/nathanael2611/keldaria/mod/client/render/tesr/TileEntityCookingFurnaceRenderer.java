/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.block.BlockCookingFurnace;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCookingFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityCookingFurnaceRenderer extends TileEntitySpecialRenderer<TileEntityCookingFurnace>
{

    @Override
    public void render(TileEntityCookingFurnace te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.translate(x, y+0.062+(0.0625), z);
        try {
            this.getWorld().getBlockState(te.getPos()).getValue(BlockCookingFurnace.FACING);
        } catch(Exception ex)
        {
            return;
        }
        switch (this.getWorld().getBlockState(te.getPos()).getValue(BlockCookingFurnace.FACING))
        {
            case WEST:
                GlStateManager.translate(0.35, 0.43, 0.5);
                break;
            case EAST:
                GlStateManager.translate(0.65, 0.43, 0.5);
                break;
            case NORTH:
                GlStateManager.translate(0.5, 0.43, 0.3);
                break;
            case SOUTH:
                GlStateManager.translate(0.5, 0.43, 0.7);
        }


        GlStateManager.rotate(1, 0, 1.0f, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        GlStateManager.rotate(90, 1, 0, 0);
        GlStateManager.translate(0.5, -0.1875, 0.0);
        GlStateManager.translate(0.0, 0.6875, 0.0);
        GlStateManager.scale(0.5, 0.5, 0.5);


        ItemStack stack = te.getEntryCopy().copy();
        if(!te.getStackInSlot(2).copy().isEmpty())
        {
            stack = te.getStackInSlot(2).copy();
        }
        Minecraft.getMinecraft().getRenderItem().renderItem(stack.copy(), ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }
}
