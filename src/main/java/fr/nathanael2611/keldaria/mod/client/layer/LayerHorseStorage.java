/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.layer;

import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import fr.nathanael2611.keldaria.mod.inventory.InventoryHorseStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;

public class LayerHorseStorage implements LayerRenderer<EntityHorse>
{

    private final TileEntityBanner banner = new TileEntityBanner();
    private final ModelShield modelShield = new ModelShield();

    @Override
    public void doRenderLayer(EntityHorse entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {


        //System.out.println(entitylivingbaseIn.getHorseArmorType());
        ILivingHorse horse = (ILivingHorse) entitylivingbaseIn;
        //horse.get();
        InventoryHorseStorage horseStorage = horse.getClientHorseStorage();
        for (int i = 0; i < horseStorage.getSizeInventory(); i++)
        {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90, 0, 1, 0);
            if (entitylivingbaseIn.isRearing())
            {
                GlStateManager.rotate(-30, 0, 0, 1);
                GlStateManager.translate(0, -0.5, 0);
            }
            GlStateManager.translate(0, 0.5, 0);
            GlStateManager.translate(-0.3, 0, 0);
            if (i == 0)
            {
                GlStateManager.translate(0, 0, 0.3);
            } else if (i == 1)
            {
                GlStateManager.translate(0, 0, -0.4);
                GlStateManager.rotate(180, 0, 1, 0);
            } else if (i == 2)
            {
                GlStateManager.translate(0, 0, 0.33);
                GlStateManager.translate(-0.45, 0, 0);
            } else if (i == 3)
            {
                GlStateManager.translate(0, 0, -0.43);
                GlStateManager.translate(-0.45, 0, 0);
                GlStateManager.rotate(180, 0, 1, 0);
            }
            ItemStack stack = horseStorage.getStackInSlot(i);
            if (stack.getItem() instanceof ItemShield)
            {

                   /* if (SkinNBTHelper.stackHasSkinData(stack))
                    {
                        SkinDescriptor descriptor = SkinNBTHelper.getSkinDescriptorFromStack(stack);
                        if (descriptor != null)
                        {
                            Skin skin = ClientSkinCache.INSTANCE.getSkin(descriptor);

                            if (skin != null)
                            {
                                GlStateManager.pushMatrix();
                                {
                                    //GlStateManager.rotate(35 + 180, 0, 0, 1);
                                    GlStateManager.rotate(90, 0, 1, 0);
                                    GlStateManager.translate(0.15, 0, 0);
                                    SkinItemRenderHelper.renderSkinWithoutHelper(descriptor, false);
                                }
                                GlStateManager.popMatrix();
                            }
                        }
                    } else*/
                { /* Vanilla Render */

                    GlStateManager.pushMatrix();
                    if (stack.getSubCompound("BlockEntityTag") != null)
                    {
                        this.banner.setItemValues(stack, true);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
                    } else
                    {
                        Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
                    }
                    GlStateManager.scale(1.0F, -1.0F, -1.0F);
                    GlStateManager.rotate(180, 0, 0, 1);
                    this.modelShield.render();
                    GlStateManager.popMatrix();
                }

            } else
            {
                doTheRender(entitylivingbaseIn, stack);
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    public void doTheRender(AbstractHorse entity, ItemStack stack)
    {

        if (entity != null && stack != null)
        {


            /*if (SkinNBTHelper.stackHasSkinData(stack))
            {
                SkinDescriptor descriptor = SkinNBTHelper.getSkinDescriptorFromStack(stack);
                if (descriptor != null)
                {
                    Skin skin = ClientSkinCache.INSTANCE.getSkin(descriptor);

                    if (skin != null)
                    {
                        GlStateManager.pushMatrix();


                        if (skin.getSkinType() == SkinTypeRegistry.skinBow)
                        {
                            GlStateManager.rotate(80, 0, 0, 1);
                            GlStateManager.rotate(180 + 90, 0, 1, 0);
                            GlStateManager.translate(0, 0, 0.05);
                            int useCount = entity.getItemInUseMaxCount();
                            ModelSkinBow model = SkinModelRenderHelper.INSTANCE.modelBow;
                            if(stack.getItem() instanceof ItemCrossbow)
                            {
                                GlStateManager.rotate(90, 1, 0, 0);
                                GlStateManager.translate(0, 0.25, 0);
                            }
                            model.frame = 0;
                            model.render(entity, skin, false, descriptor.getSkinDye(), null, false, 0, false);
                        } else
                        {
                            GlStateManager.rotate(35 + 180, 0, 0, 1);
                            GlStateManager.rotate(180 + 90, 0, 1, 0);
                            GlStateManager.translate(0, 0.5, 0);
                            SkinItemRenderHelper.renderSkinWithoutHelper(descriptor, false);
                        }
                        GlStateManager.popMatrix();
                    }
                }
            } else*/
            {
                GlStateManager.rotate(30, 0, 0, 1);

                ItemCameraTransforms.TransformType type = ItemCameraTransforms.TransformType.FIXED;
                Item item = stack.getItem();
                if (!(item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemBow))
                {
                    GlStateManager.scale(0.5, 0.5, 0.5);
                }
                Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, type);
            }
        }
    }

}
