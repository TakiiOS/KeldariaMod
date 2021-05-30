/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityHRPCarpet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityHRPCarpetRender extends TileEntitySpecialRenderer<TileEntityHRPCarpet>
{

    @Override
    public void render(TileEntityHRPCarpet te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y, z + 0.5);




        for (int d = 0; d < te.getSizeInventory(); d++)
        {
            ItemStack stack = te.getStackInSlot(d);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0, d * 0.001, 0);
            if(d == 0)
            {
                GlStateManager.translate(-0.3, 0, 0);
            }
            else if(d == 1)
            {
                GlStateManager.translate(0.1, 0, 0.3);
            }
            else if(d == 2)
            {
                GlStateManager.translate(-0.1, 0, -0.3);
            }
            else if(d == 3)
            {
                GlStateManager.translate(0.3, 0, -0.3);
            }


            GlStateManager.rotate(90, 1, 0, 0);
            GlStateManager.rotate(d * 30, 0, 0, 1);

            int fifou = 0;
            for(int i = 0; i < stack.getCount(); i ++)
            {
                Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.translate(0, 0, -0.02);
                GlStateManager.rotate(d * 30, 0, 0, 1);

                GlStateManager.rotate(fifou == 0 ? 20 : fifou == 1 ?  -20 : 0, 0, 0, 1);
                fifou ++;
                if(fifou > 2)
                {
                    fifou = 0;
                }
            }
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();

        /*GlStateManager.enableAlpha();
        int lines = RenderHelpers.drawCenteredStringMultiLine(te.getText(), 100, -2000, -2000 ,new Color(0, 0, 0, 0).getRGB());
        GlStateManager.disableAlpha();
        if(Minecraft.getMinecraft().gameSettings.hideGUI) return;
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
        GlStateManager.popMatrix();*/
    }

}
