package fr.nathanael2611.obfuscate.remastered.client.model;

import fr.nathanael2611.obfuscate.remastered.client.ModelPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CustomModelPlayer extends ModelPlayer
{
    private boolean smallArms;
    private float modelSize;

    public CustomModelPlayer(final float modelSize, final boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        this.smallArms = smallArmsIn;
        this.modelSize = modelSize;
    }

    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        this.resetRotationAngles();
        if (!MinecraftForge.EVENT_BUS.post((Event)new ModelPlayerEvent.SetupAngles.Pre((EntityPlayer)entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks()))) {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
            MinecraftForge.EVENT_BUS.post((Event)new ModelPlayerEvent.SetupAngles.Post((EntityPlayer)entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks()));
        }
        this.setupRotationAngles();
    }

    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        this.resetVisibilities();

        // ENCORE MOINS FOUFOU System.out.println(textureWidth);

/*
        int headScale = 10;

        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-(headScale/2), -(4 + headScale), -(headScale/2), headScale, headScale, headScale, modelSize);
        int texWidth = (int) (64 * (headScale / 8));
        this.textureWidth  = texWidth;
        this.textureHeight = texWidth;
        this.bipedHead.setRotationPoint(0.0F, 0.0F + modelSize, 0.0F);
*/

        if (!MinecraftForge.EVENT_BUS.post((Event)new ModelPlayerEvent.Render.Pre((EntityPlayer)entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks()))) {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            MinecraftForge.EVENT_BUS.post((Event)new ModelPlayerEvent.Render.Post((EntityPlayer)entityIn, this, Minecraft.getMinecraft().getRenderPartialTicks()));
        }
    }

    private void setupRotationAngles() {
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
        copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }

    private void resetRotationAngles() {
        this.resetAll(this.bipedHead);
        this.resetAll(this.bipedHeadwear);
        this.resetAll(this.bipedBody);
        this.resetAll(this.bipedBodyWear);
        this.resetAll(this.bipedRightArm);
        this.bipedRightArm.rotationPointX = -5.0f;
        this.bipedRightArm.rotationPointY = (this.smallArms ? 2.5f : 2.0f);
        this.bipedRightArm.rotationPointZ = 0.0f;
        this.resetAll(this.bipedRightArmwear);
        this.bipedRightArmwear.rotationPointX = -5.0f;
        this.bipedRightArmwear.rotationPointY = (this.smallArms ? 2.5f : 2.0f);
        this.bipedRightArmwear.rotationPointZ = 10.0f;
        this.resetAll(this.bipedLeftArm);
        this.bipedLeftArm.rotationPointX = 5.0f;
        this.bipedLeftArm.rotationPointY = (this.smallArms ? 2.5f : 2.0f);
        this.bipedLeftArm.rotationPointZ = 0.0f;
        this.resetAll(this.bipedLeftArmwear);
        this.bipedLeftArmwear.rotationPointX = 5.0f;
        this.bipedLeftArmwear.rotationPointY = (this.smallArms ? 2.5f : 2.0f);
        this.bipedLeftArmwear.rotationPointZ = 0.0f;
        this.resetAll(this.bipedLeftLeg);
        this.bipedLeftLeg.rotationPointX = 1.9f;
        this.bipedLeftLeg.rotationPointY = 12.0f;
        this.bipedLeftLeg.rotationPointZ = 0.0f;
        this.resetAll(this.bipedLeftLegwear);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        this.resetAll(this.bipedRightLeg);
        this.bipedRightLeg.rotationPointX = -1.9f;
        this.bipedRightLeg.rotationPointY = 12.0f;
        this.bipedRightLeg.rotationPointZ = 0.0f;
        this.resetAll(this.bipedRightLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
    }

    private void resetAll(final ModelRenderer renderer) {
        renderer.offsetX = 0.0f;
        renderer.offsetY = 0.0f;
        renderer.offsetZ = 0.0f;
        renderer.rotateAngleX = 0.0f;
        renderer.rotateAngleY = 0.0f;
        renderer.rotateAngleZ = 0.0f;
        renderer.rotationPointX = 0.0f;
        renderer.rotationPointY = 0.0f;
        renderer.rotationPointZ = 0.0f;
    }

    private void resetVisibilities() {
        this.bipedHead.isHidden = false;
        this.bipedBody.isHidden = false;
        this.bipedRightArm.isHidden = false;
        this.bipedLeftArm.isHidden = false;
        this.bipedRightLeg.isHidden = false;
        this.bipedLeftLeg.isHidden = false;
    }
}
