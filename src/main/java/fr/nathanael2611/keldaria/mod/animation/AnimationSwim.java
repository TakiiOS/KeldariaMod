package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class AnimationSwim extends Animation
{
    public AnimationSwim()
    {
        super("swim");
    }

    @Override
    public void render(EntityPlayer player, ModelPlayer issou, float partialTicks)
    {
        super.render(player, issou, partialTicks);

        if(!player.isInsideOfMaterial(Material.WATER)) return;

        float fIssou = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
        float fIssou1 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, partialTicks);
        float netHeadYaw = fIssou1 - fIssou;

        issou.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

        issou.bipedHead.rotateAngleX = -((float)Math.PI / 4F);

        issou.bipedLeftArm.rotateAngleZ += 180;
        issou.bipedLeftArm.rotateAngleY += 30;

        issou.bipedRightArm.rotateAngleZ = -issou.bipedLeftArm.rotateAngleZ;
        issou.bipedRightArm.rotateAngleY += issou.bipedLeftArm.rotateAngleY;
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
    {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) { }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    @Override
    public boolean isStopOnMove()
    {
        return false;
    }

    @Override
    public boolean isInAnimationMenu() {
        return false;
    }

    @Override
    public float getHeight(float scale)
    {
        return (float) (0.6 * scale);
    }

    @Override
    public float getWidth(float scale)
    {
        return (float) (0.6 * scale);
    }
}
