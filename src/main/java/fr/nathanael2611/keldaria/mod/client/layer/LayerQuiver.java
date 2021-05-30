/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.layer;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.model.ModelQuiver;
import fr.nathanael2611.keldaria.mod.features.accessories.Accessories;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerQuiver implements LayerRenderer<EntityPlayer>
{

    private RenderTippedArrow arrow = new RenderTippedArrow(Minecraft.getMinecraft().getRenderManager());

    private ModelQuiver quiver = new ModelQuiver();

    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {

        GlStateManager.pushMatrix();

        Accessories accessories = Keldaria.getInstance().getSyncedAccessories().getAccessories(player);
        ItemStack quiver = accessories.getInventory().getAccessory(EntityEquipmentSlot.CHEST);
        GlStateManager.translate(0, 0, 0.15);
        GlStateManager.rotate(-90, 1, 0, 0);
        //System.out.println(quiver.getTagCompound());
        if(quiver.getItem() instanceof ItemQuiver)
        {
            InventoryQuiver inventoryQuiver = new InventoryQuiver(player, quiver);
            int arrows = 0;
            for (int i = 0; i < inventoryQuiver.getSizeInventory(); i++)
            {
                ItemStack stack = inventoryQuiver.getStackInSlot(i);
                if(!stack.isEmpty())
                {
                    int number = stack.getCount();
                    arrows += number;
                }
            }

            renderQuiverWithArrows(player, arrows, partialTicks);
        }

        GlStateManager.popMatrix();
    }

    public void renderQuiverWithArrows(EntityPlayer player, int arrowCount, float partialTicks)
    {
        EntityTippedArrow entityArrow = new EntityTippedArrow(player.world);
        GlStateManager.pushMatrix();

        //GlStateManager.translate(0, 0.05 * i, 0);
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("keldaria", "textures/models/quiver.png"));
        GlStateManager.translate(0, 0, 0.6);
        GlStateManager.translate(0, 0.15, 0);
        GlStateManager.scale(0.06, 0.06, 0.06);
        GlStateManager.rotate(90, 1, 0, 0);
        quiver.render(1);
        GlStateManager.popMatrix();

        arrowCount = Math.min(arrowCount, 20);

        GlStateManager.rotate(30, 0, 1, 0);
        GlStateManager.translate(0, 0, 0.3);
        GlStateManager.translate(0, -0.1, 0);
        GlStateManager.translate(-0.28, 0, 0);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.05 * 2.5, 0);
        int ii = 0;
        for (int i = 0; i < arrowCount; i++)
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.3, 0.3, 1);
            arrow.doRender(entityArrow, 0, 0, 0, 0, partialTicks);
            GlStateManager.popMatrix();
            GlStateManager.translate(0.05, 0, 0);
            ii ++;
            if(ii >= 5)
            {
                GlStateManager.translate(-0.05 * 5, 0, 0);
                GlStateManager.translate(0, -0.05, 0);
                ii = 0;
            }
        }
        GlStateManager.popMatrix();


        GlStateManager.popMatrix();
        GlStateManager.enableCull();



    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
