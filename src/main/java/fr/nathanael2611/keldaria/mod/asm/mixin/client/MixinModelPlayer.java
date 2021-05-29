package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.obfuscate.remastered.client.ModelPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(Side.CLIENT)
@Mixin(ModelPlayer.class)
public abstract class MixinModelPlayer extends ModelBiped
{

    @Shadow
    public ModelRenderer bipedRightLegwear;

    @Shadow
    public ModelRenderer bipedLeftLegwear;

    @Shadow
    @Final
    private boolean smallArms;

    @Shadow
    public ModelRenderer bipedLeftArmwear;

    @Shadow
    public ModelRenderer bipedBodyWear;

    @Shadow
    public ModelRenderer bipedRightArmwear;

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        resetRotationAngles();
        Animation animation = AnimationUtils.getPlayerHandledAnimation((EntityPlayer) entityIn);

        //entityIn.setSneaking(true);
        if (!MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Pre((EntityPlayer) entityIn, (ModelPlayer) (Object) this, Minecraft.getMinecraft().getRenderPartialTicks())))
        {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

            MinecraftForge.EVENT_BUS.post(new ModelPlayerEvent.SetupAngles.Post((EntityPlayer) entityIn, (ModelPlayer) (Object) this, Minecraft.getMinecraft().getRenderPartialTicks()));
        }

        //animation.renderAnimation((EntityPlayer) entityIn, (ModelPlayer) (Object) this);


        /*if(entityIn instanceof EntityPlayer)
        {
            System.out.println("iNeed");
            EntityPlayer player = (EntityPlayer) entityIn;
            AnimationUtils.getPlayerHandledAnimation(entityIn.getName()).renderAnimation(
                    new ModelPlayerEvent.SetupAngles.Post(
                            player, (ModelPlayer) (Object) this, Minecraft.getMinecraft().getRenderPartialTicks()
                    )
            );
        }*/


        setupRotationAngles();
    }

    private void setupRotationAngles()
    {
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
        copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }

    private void resetRotationAngles()
    {
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

    private void resetAll(final ModelRenderer renderer)
    {
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

}
