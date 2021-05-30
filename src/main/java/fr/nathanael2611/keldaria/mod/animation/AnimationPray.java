/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

public class AnimationPray extends Animation {
    public AnimationPray() {
        super("pray", false);
    }

    @Override
    void render(EntityPlayer player, ModelPlayer model, float partialTicks) {

        double translateYValue = 0.35;
        if(player.isSneaking()){
            translateYValue-=0.165;
        }
        GlStateManager.translate(0, translateYValue, 0);

        model.isSneak = false;

        model.bipedRightLeg.rotateAngleX= (float) Math.toRadians(90);
        model.bipedLeftLeg.rotateAngleX= (float) Math.toRadians(90);

        model.bipedRightLeg.rotateAngleY = (float) Math.toRadians(-10F);
        model.bipedLeftLeg.rotateAngleY = (float) Math.toRadians(10F);


        model.bipedLeftArm.rotateAngleX-=1.85;
        model.bipedRightArm.rotateAngleX-=1.85;
        model.bipedLeftArm.rotateAngleY+=0.55;
        model.bipedRightArm.rotateAngleY-= 0.55;
        if(player.isSneaking()){
            model.bipedLeftArm.rotateAngleX+=0.7;
            model.bipedRightArm.rotateAngleX+=0.7;
            model.bipedBody.rotateAngleX+=0.5;

            model.bipedBody.offsetY+=0.1;
            model.bipedBodyWear.offsetY+=0.1;
            if(chestplate != null)chestplate.bipedBody.offsetY+=0.1;

            model.bipedHead.offsetY+=0.1;
            model.bipedHeadwear.offsetY+=0.1;
            if(chestplate != null)chestplate.bipedHead.offsetY+=0.1;


            model.bipedLeftArm.offsetY+=0.1;
            model.bipedRightArm.offsetY+=0.1;
            model.bipedLeftArmwear.offsetY+=0.1;
            model.bipedRightArmwear.offsetY+=0.1;
            if(chestplate != null)chestplate.bipedLeftArm.offsetY+=0.1;
            if(chestplate != null)chestplate.bipedRightArm.offsetY+=0.1;


            model.bipedLeftArm.offsetZ+=0.1;
            model.bipedRightArm.offsetZ+=0.1;
            model.bipedLeftArmwear.offsetZ+=0.1;
            model.bipedRightArmwear.offsetZ+=0.1;
            if(chestplate != null)chestplate.bipedLeftArm.offsetZ+=0.1;
            if(chestplate != null)chestplate.bipedRightArm.offsetZ+=0.1;

            model.bipedLeftLeg.offsetZ+=0.3;
            model.bipedRightLeg.offsetZ+=0.3;
            model.bipedLeftLegwear.offsetZ+=0.3;
            model.bipedRightLegwear.offsetZ+=0.3;
            if(leggings != null)leggings.bipedLeftLeg.offsetZ+=0.3;
            if(leggings != null)leggings.bipedRightLeg.offsetZ+=0.3;
            if(chestplate != null)chestplate.bipedLeftLeg.offsetZ+=0.3;
            if(chestplate != null)chestplate.bipedRightLeg.offsetZ+=0.3;


            model.bipedLeftLeg.offsetY+=0.03;
            model.bipedRightLeg.offsetY+=0.03;
            model.bipedLeftLegwear.offsetY+=0.03;
            model.bipedRightLegwear.offsetY+=0.03;

        }

    }

    @Override
    public float getHeight(float scale) {
        return 1.24f*scale;
    }

    @Override
    public String[] getDescription(){
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet de prier."
        };
    }
}
