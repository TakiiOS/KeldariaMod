/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class AnimationSleep extends Animation {
    public AnimationSleep() {
        super("sleep", false);
    }

    @Override
    public void render(EntityPlayer player, ModelPlayer model, float partialTicks) {

        model.isSneak = false;
        double translateYValue = 0.7;
        if(player.isSneaking())translateYValue-=0.17;
        GlStateManager.translate(0, translateYValue, 0);



        model.bipedHead.rotateAngleX-=0.45;

        model.bipedBody.rotateAngleX=(float)Math.toRadians(-90);

        model.bipedRightLeg.offsetY=-0.75f;
        model.bipedLeftLeg.offsetY=-0.75f;
        model.bipedRightLegwear.offsetY=-0.75f;
        model.bipedLeftLegwear.offsetY=-0.75f;

        if(leggings != null)
        {
            leggings.bipedRightLeg.offsetY=-0.75f;
            leggings.bipedLeftLeg.offsetY=-0.75f;
        }



        model.bipedRightLeg.offsetZ-=0.75f;
        model.bipedLeftLeg.offsetZ-=0.75f;
        model.bipedRightLegwear.offsetZ-=0.75f;
        model.bipedLeftLegwear.offsetZ-=0.75f;

        if(leggings != null){
            leggings.bipedRightLeg.offsetZ-=0.75f;
            leggings.bipedLeftLeg.offsetZ-=0.75f;
        }

        model.bipedRightLeg.rotateAngleX=(float)Math.toRadians(-90);
        model.bipedLeftLeg.rotateAngleX=(float)Math.toRadians(-90);

        model.bipedRightArm.rotateAngleX=(float)Math.toRadians(-90);
        model.bipedLeftArm.rotateAngleX=(float)Math.toRadians(-90);

        model.bipedRightArm.offsetY-=0.115;
        model.bipedLeftArm.offsetY-=0.115;
        model.bipedRightArmwear.offsetY-=0.115;
        model.bipedLeftArmwear.offsetY-=0.115;

        model.bipedRightArm.offsetZ-=0.115;
        model.bipedLeftArm.offsetZ-=0.115;
        model.bipedRightArmwear.offsetZ-=0.115;
        model.bipedLeftArmwear.offsetZ-=0.115;

        if(chestplate != null){
            chestplate.bipedRightArm.offsetY-=0.115;
            chestplate.bipedLeftArm.offsetY-=0.115;

            chestplate.bipedRightArm.offsetZ-=0.115;
            chestplate.bipedLeftArm.offsetZ-=0.115;

            chestplate.bipedRightLeg.offsetZ-=0.75f;
            chestplate.bipedLeftLeg.offsetZ-=0.75f;

            chestplate.bipedRightLeg.offsetY=-0.75f;
            chestplate.bipedLeftLeg.offsetY=-0.75f;
        }



    }

    @Override
    public float getHeight(float scale) {
        return 0.55f*scale;
    }

    @Override
    public float getWidth(float scale) {
        return 0.98f*scale;
    }
    @Override
    public boolean isPlayerCanMoveIfSneaked() {
        return false;
    }

    @Override
    public String[] getDescription(){
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet de s'allonger."
        };
    }

    @Override
    public boolean isElongated() {
        return true;
    }
}
