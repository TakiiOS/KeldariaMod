package fr.nathanael2611.keldaria.mod.client.render;

import fr.nathanael2611.keldaria.mod.client.model.ModelLammergeier;
import fr.nathanael2611.keldaria.mod.entity.EntityLucrain;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderLammergeier extends Render<EntityLucrain>
{

    private ModelLammergeier model = new ModelLammergeier();

    public RenderLammergeier(RenderManager rendermanagerIn) {
        super(rendermanagerIn);
    }

    @Override
    public void doRender(EntityLucrain entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
       // super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate(
                x ,
                y,
                z);

        //model.swingProgress = entity.getSwingProgress(partialTicks);
        float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);

        {

            GlStateManager.rotate(180.0F - f, 0.0F, 1.0F, 0.0F);

        }
        GlStateManager.scale(-1, -1, 1);

        //GlStateManager.translate(0, -((entity.height*2) / 2) - 1, 0);
        GlStateManager.translate(0, -(entity.height) * 2, 0);
        GlStateManager.scale(0.1, 0.1, 0.1);
        GlStateManager.scale(entity.width, entity.height, entity.width);


        this.bindTexture(getEntityTexture(entity));
        model.setRotationAngles(entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, entity.rotationYawHead,
                entity.rotationPitch, 1, entity);
        model.render(entity, (float)x, (float)y, (float)z, entityYaw, partialTicks, 1);
        GlStateManager.popMatrix();
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityLucrain entity) {
        int typeNumber = 1;
        ResourceLocation result = new ResourceLocation("keldaria", "textures/entity/lucrain/1.png");
        switch (typeNumber) {
        case 1:
            //result = ModTextures.lam_orange;
            break;
        case 2:
            //result = ModTextures.lam_red;
            break;
        case 3:
            //result = ModTextures.lam_white;
            break;
        case 4:
            //result = ModTextures.lam_yellow;
            break;
        }
        return result;

    }


    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
    {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

}