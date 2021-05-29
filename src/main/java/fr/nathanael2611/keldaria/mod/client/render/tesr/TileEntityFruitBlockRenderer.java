package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFruitBlock;
import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityFruitBlockRenderer extends TileEntitySpecialRenderer<TileEntityFruitBlock>
{

    private Item heldItem;

    @Override
    public void render(TileEntityFruitBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        if(heldItem == null)
        {
            heldItem = new ItemStack(KeldariaBlocks.FRUIT_BLOCK).getItem();
        }

        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
        if(mc.player.getHeldItemMainhand().getItem() == heldItem)
        {
            GlStateManager.rotate(-mc.player.rotationYawHead, 0, 1, 0);
            GlStateManager.rotate(mc.player.rotationPitch, 1, 0, 0);
            mc.getItemRenderer().renderItem(mc.player, new ItemStack(heldItem), ItemCameraTransforms.TransformType.FIXED);
        }
        else
        {
            if(te.isGrowed())
            {
                GlStateManager.translate(0, 0.4, 0);
                ItemStack stack = te.isMatured() ? te.getMatureStack() : te.getMaturingStack();
                GlStateManager.rotate(-mc.player.rotationYawHead, 0, 1, 0);
                GlStateManager.scale(0.5, 0.5, 0.5);
                if(!te.isMatured())
                {
                    double factor = te.getMaturingPercent() * 0.01;
                    GlStateManager.scale(factor, factor, factor);
                }

                GlStateManager.pushMatrix();
                mc.getItemRenderer().renderItem(mc.player, stack, ItemCameraTransforms.TransformType.FIXED);

                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }
}
