/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * The player will clap hand when this animation is running.
 *
 * @author Nathanael2611
 */
public class AnimationClap extends Animation {
    public AnimationClap() {
        super("clap");
    }

    float timer = 0;
    @Override
    public void render(EntityPlayer player, ModelPlayer model, float partialTicks) {

        model.bipedRightArm.rotateAngleX = (float)Math.toRadians(-40);
        model.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-40);

        model.bipedRightArm.rotateAngleY = (float)Math.toRadians(10);
        model.bipedLeftArm.rotateAngleY = (float)Math.toRadians(-10);

        timer+=2;
        if(timer>80){
            timer = 0;
        }
        double bound = 80;
        if(timer<(bound/2)){
            model.bipedLeftArm.rotateAngleZ +=  Math.toRadians(bound - timer - bound/2);
            model.bipedRightArm.rotateAngleZ -=  Math.toRadians(bound - timer - bound/2);
        }else{
            model.bipedLeftArm.rotateAngleZ += Math.toRadians(timer - bound/2);
            model.bipedRightArm.rotateAngleZ -= Math.toRadians(timer - bound/2);
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet d'applaudir."
        };
    }
}
