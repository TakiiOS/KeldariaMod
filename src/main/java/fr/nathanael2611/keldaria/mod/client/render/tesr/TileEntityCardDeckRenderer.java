package fr.nathanael2611.keldaria.mod.client.render.tesr;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCardDeck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TileEntityCardDeckRenderer extends TileEntitySpecialRenderer<TileEntityCardDeck>
{

    @Override
    public void render(TileEntityCardDeck te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        int size = 0;
        for (int d = 0; d < te.getSizeInventory(); d++)
        {
            ItemStack stack = te.getStackInSlot(d);
            if(!stack.isEmpty()) size ++;
        }

        if(size > 0)
        {

            GlStateManager.pushMatrix();

            GlStateManager.translate(x + 0.5, y + 0.08, z + 0.5);
            GlStateManager.scale(0.6, 0.6, 0.6);
            GlStateManager.scale(1, size, 1);
            GlStateManager.rotate(90, 1, 0, 0);


            ItemStack stack = new ItemStack(KeldariaItems.CARD);

            Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player,
                    stack, ItemCameraTransforms.TransformType.GROUND);

            GlStateManager.popMatrix();

        }

    }
}
