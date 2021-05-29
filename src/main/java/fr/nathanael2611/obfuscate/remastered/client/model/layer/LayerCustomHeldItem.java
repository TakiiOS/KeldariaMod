package fr.nathanael2611.obfuscate.remastered.client.model.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCustomHeldItem implements LayerRenderer<EntityLivingBase>
{
    private final RenderLivingBase<?> livingEntityRenderer;
    
    public LayerCustomHeldItem(final RenderLivingBase<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(final EntityLivingBase entity, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final boolean isRightHanded = entity.getPrimaryHand() == EnumHandSide.RIGHT;
        final ItemStack leftStack = isRightHanded ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        final ItemStack rightStack = isRightHanded ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();
        if (!leftStack.isEmpty() || !rightStack.isEmpty()) {
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                GlStateManager.translate(0.0f, 0.75f, 0.0f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
            this.renderHeldItem(entity, rightStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, partialTicks);
            this.renderHeldItem(entity, leftStack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, partialTicks);
            GlStateManager.popMatrix();
        }
    }
    
    private void renderHeldItem(final EntityLivingBase entity, final ItemStack stack, final ItemCameraTransforms.TransformType transformType, final EnumHandSide handSide, final float partialTicks) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            if (entity.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            this.translateToHand(handSide);
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            final boolean isLeftHanded = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((isLeftHanded ? -1 : 1) / 16.0f, 0.125f, -0.625f);
            if (this.isArmVisible(handSide)) {
                Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, transformType, isLeftHanded);
                //MinecraftForge.EVENT_BUS.post((Event)new RenderItemEvent.Held.Post(entity, stack, transformType, handSide, partialTicks));
            }
            GlStateManager.popMatrix();
        }
    }
    
    private boolean isArmVisible(final EnumHandSide handSide) {
        final RenderPlayer render = (RenderPlayer)this.livingEntityRenderer;
        switch (handSide) {
            case LEFT: {
                return !render.getMainModel().bipedLeftArm.isHidden && render.getMainModel().bipedLeftArm.showModel;
            }
            case RIGHT: {
                return !render.getMainModel().bipedRightArm.isHidden && render.getMainModel().bipedRightArm.showModel;
            }
            default: {
                return false;
            }
        }
    }
    
    protected void translateToHand(final EnumHandSide p_191361_1_) {
        ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625f, p_191361_1_);
    }
    
    public boolean shouldCombineTextures() {
        return false;
    }
}