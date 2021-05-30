/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.layer;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.accessories.InventoryAccessories;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemSpur;
import fr.nathanael2611.keldaria.mod.util.ModelHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class LayerSpur implements LayerRenderer<EntityPlayer>
{
    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        //IKeldariaPlayer keldaPlayer = (IKeldariaPlayer) player;
        InventoryAccessories accessories = Keldaria.getInstance().getSyncedAccessories().getAccessories(player).getInventory();
        ItemStack stack = accessories.getAccessory(EntityEquipmentSlot.FEET);
        ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();

        if(stack.getItem() instanceof ItemSpur)
        {
            ItemSpur spur = (ItemSpur) stack.getItem();

            if (player.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.0F, 0.3F);
                //GlStateManager.rotate(90.0F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
            }

            GlStateManager.pushMatrix();
            {
                ModelPlayer model = ModelHelpers.getRenderPlayer((AbstractClientPlayer) player).getMainModel();
                GlStateManager.translate(0, 0.65, 0);
                GlStateManager.rotate(model.bipedLeftLeg.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(model.bipedLeftLeg.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(model.bipedLeftLeg.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0, player.isRiding() ? 1.05 : 0.9, 0);
                if (!player.isRiding()) GlStateManager.translate(0, 0, 0.3);
                GlStateManager.translate(0.26, 0, 0);
                GlStateManager.translate(0, 0, -0.1);
                GlStateManager.rotate(90, 0, 1, 0);
                if (player.isRiding())
                {
                    GlStateManager.rotate(130, 0, 0, 1);
                } else
                {
                    GlStateManager.rotate(200, 0, 0, 1);
                }
                renderer.renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.GROUND);
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                ModelPlayer model = ModelHelpers.getRenderPlayer((AbstractClientPlayer) player).getMainModel();
                GlStateManager.translate(0, 0.65, 0);
                GlStateManager.rotate(model.bipedRightLeg.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(model.bipedRightLeg.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(model.bipedRightLeg.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0, player.isRiding() ? 1.05 : 0.9, 0);
                if (!player.isRiding()) GlStateManager.translate(0, 0, 0.3);
                GlStateManager.translate(-0.26, 0, 0);
                GlStateManager.translate(0, 0, -0.1);
                GlStateManager.rotate(90, 0, 1, 0);
                if (player.isRiding())
                {
                    GlStateManager.rotate(130, 0, 0, 1);
                } else
                {
                    GlStateManager.rotate(200, 0, 0, 1);
                }
                renderer.renderItem(Minecraft.getMinecraft().player, stack, ItemCameraTransforms.TransformType.GROUND);
            }
            GlStateManager.popMatrix();


        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}
