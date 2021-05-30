/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.layer;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.client.model.ModelRoundShield;
import fr.nathanael2611.keldaria.mod.features.ItemTextures;
import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import fr.nathanael2611.keldaria.mod.features.backweapons.Weapons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class LayerWeapons implements LayerRenderer<EntityPlayer>
{

    private final TileEntityBanner banner = new TileEntityBanner();
    private final ModelShield modelShield = new ModelShield();
    private final ModelRoundShield roundShield = new ModelRoundShield();

    @Override
    public void doRenderLayer(EntityPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        Weapons weapons = Keldaria.getInstance().getWeaponsManager().getWeapons(entity);
        if (weapons != null)
        {
            ItemStack backItem1 = weapons.getItem4();
            ItemStack backItem2 = weapons.getItem2();
            ItemStack backItem3 = weapons.getItem3();
            ItemStack backItem4 = weapons.getItem1();
            ItemStack shield = weapons.getShield();

            Animation animation = AnimationUtils.getPlayerHandledAnimation(entity);

            GlStateManager.pushMatrix();

            if (entity.isChild())
            {
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            }

            // Make backpack swing with body as players swing their arms.
            float swingProgress = entity.getSwingProgress(partialTicks);
            float swingAngle = MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2.0F)) * 0.2F;
            if ((entity.swingingHand == EnumHand.OFF_HAND) ^ (entity.getPrimaryHand() == EnumHandSide.LEFT))
                swingAngle *= -1;
            if (swingAngle != 0) GlStateManager.rotate((float) Math.toDegrees(swingAngle), 0.0F, 1.0F, 0.0F);

            // Rotate backpack if entity is sneaking.
            if (entity.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
                if (!animation.isElongated()) GlStateManager.rotate(90.0F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
            }

            if (!shield.isEmpty())
            { //Shield
                GlStateManager.pushMatrix();

                GlStateManager.translate(0, 0.5, 0);
                GlStateManager.translate(0, 0, 0.215);

                if (animation.isElongated())
                {
                    GlStateManager.translate(0, -0.65, 0);
                    GlStateManager.translate(0, 0, 0.3);
                    GlStateManager.rotate(180, 0, 1, 0);
                    GlStateManager.rotate(90, 1, 0, 0);
                }


                if (entity != null && shield != null)
                {
                    /*if (SkinNBTHelper.stackHasSkinData(shield))
                    {
                        SkinDescriptor descriptor = SkinNBTHelper.getSkinDescriptorFromStack(shield);
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



                        GlStateManager.scale(1.0F, -1.0F, -1.0F);
                        GlStateManager.rotate(180, 0, 0, 1);
                        if (shield.getItem() instanceof ItemShield)
                        {
                            if (ItemTextures.hasTexture(shield))
                            {
                                GlStateManager.rotate(173, 0, 0, 1);
                                GlStateManager.translate(0, 0.6 * (ItemTextures.getSize(shield) / 1.2), 0);
                                GlStateManager.translate(-0.28 * ItemTextures.getSize(shield), 0, 0);
                                GlStateManager.translate(0, 0, 0.08 + 0.1);
                                ItemTextures.renderItem(entity, shield, ItemCameraTransforms.TransformType.FIXED);
                            } else
                            {
                                if (shield.getSubCompound("BlockEntityTag") != null)
                                {
                                    this.banner.setItemValues(shield, true);
                                    Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
                                } else
                                {
                                    Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
                                }
                                this.modelShield.render();
                            }
                        }
                        GlStateManager.popMatrix();


                    }
                }
                GlStateManager.popMatrix();


            }



            GlStateManager.rotate(180, 1, 0, 0);

            GlStateManager.scale(0.8F, 0.8F, 0.8F);
            GlStateManager.translate(2.0F * scale, -7 * scale, -3 * scale);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);



            GlStateManager.translate(0.1, 0, 0);
            if (animation.isElongated())
            {
                GlStateManager.rotate(90, 1, 0, 0);
                GlStateManager.rotate(180, 1, 0, 0);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.translate(0, 0, 0.6);
            }



            doTheRender(entity, backItem1);

            GlStateManager.translate(0, 0, -0.08);
            GlStateManager.rotate(-90, 0, 0, 1);
            if (animation.isElongated()) GlStateManager.translate(0, 0, 0.1);

            doTheRender(entity, backItem2);

            GlStateManager.translate(0, 0, -0.1);

            GlStateManager.translate(-0.6, 0.33, 0.2);
            GlStateManager.rotate(90 * 3, 1, 0, 0);

            if (animation.isElongated())
            {
                GlStateManager.translate(-0.3, 0.3, -0.73);
                GlStateManager.rotate(45, 0, 0, 1);
                GlStateManager.rotate(-15, 1, 0, 0);
            }

            doTheRender(entity, backItem3);

            GlStateManager.translate(0, 0, -0.65);

            doTheRender(entity, backItem4);

            GlStateManager.popMatrix();

        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    public void doTheRender(EntityPlayer entity, ItemStack stack)
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
                            GlStateManager.rotate(35, 0, 0, 1);
                            GlStateManager.translate(0, 0, 0.05);
                            int useCount = entity.getItemInUseMaxCount();
                            ModelSkinBow model = SkinModelRenderHelper.INSTANCE.modelBow;
                            if(stack.getItem() instanceof ItemCrossbow)
                            {
                                GlStateManager.rotate(90, 1, 0, 0);
                                GlStateManager.translate(0, 0.25, 0);
                            }
                            model.frame = 2;
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

                GlStateManager.pushMatrix();
                double scale = PlayerSizes.get(entity);
                GlStateManager.scale(1/scale, 1/scale, 1/scale);

                Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.FIXED);

                GlStateManager.popMatrix();
            }
        }
    }
}
