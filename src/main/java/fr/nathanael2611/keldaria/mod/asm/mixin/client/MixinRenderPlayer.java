/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.ISwimingEntity;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RenderLivingBase<AbstractClientPlayer>
{

    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    /**
     * @author
     */
    @Overwrite
    @Override
    protected void applyRotations(AbstractClientPlayer entityLiving, float ageInTicks, float rotationYaw, float partialTicks)
    {
        boolean swimFlag = ((ISwimingEntity)entityLiving).isSwiming();
        if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping())
        {
            GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else if (entityLiving.isElytraFlying() || swimFlag)
        {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
            int ticks = (swimFlag ? ((ISwimingEntity)entityLiving).getSwimTime() : entityLiving.getTicksElytraFlying());
            float f = (float) ticks + partialTicks;
            float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
            GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
            Vec3d vec3d = entityLiving.getLook(partialTicks);
            double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
            if(((ISwimingEntity) entityLiving).isSwiming()) GlStateManager.translate(0.0F, -1.0F + f1, 0.3F);
            if (d0 > 0.0D && d1 > 0.0D)
            {
                double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
                GlStateManager.rotate((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        }
    }

}
