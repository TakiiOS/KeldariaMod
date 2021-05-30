/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

public class AnimationWave extends Animation {
    public AnimationWave() {
        super("wave");
    }

    float timer = 0;
    @Override
    public void render(EntityPlayer player, ModelPlayer model, float partialTicks) {

        ModelRenderer arm;
        ModelRenderer armWear;
        ModelRenderer armorArm = null;
        if(player.getPrimaryHand() == EnumHandSide.RIGHT){
            arm = model.bipedRightArm;
            armWear = model.bipedRightArmwear;
            if(chestplate != null) armorArm = chestplate.bipedRightArm;
        }else{
            arm = model.bipedLeftArm;
            armWear = model.bipedLeftArmwear;
            if(chestplate != null) armorArm = chestplate.bipedRightArm;
        }

        if(arm == model.bipedLeftArm){
            arm.rotateAngleY -= (float)Math.toRadians(30);
            arm.rotateAngleX = (float)Math.toRadians(-160);
        }else{
            arm.rotateAngleY -= (float)Math.toRadians(-30);
            arm.rotateAngleX = (float)Math.toRadians(-160);
        }

        double bound = 1.2;

        timer+=0.01;
        if(timer > bound)timer = 0;
        if(timer<bound/2){
            if(player.getPrimaryHand() == EnumHandSide.LEFT)arm.rotateAngleZ +=  bound - timer - bound/2;
            if(player.getPrimaryHand() == EnumHandSide.RIGHT)arm.rotateAngleZ -=  bound - timer - bound/2;
        }else{
            if(player.getPrimaryHand() == EnumHandSide.LEFT)arm.rotateAngleZ += timer - bound/2;
            if(player.getPrimaryHand() == EnumHandSide.RIGHT)arm.rotateAngleZ -= timer - bound/2;
        }
        arm.offsetY-=0.1;
        armWear.offsetY-=0.1;

        if(chestplate != null)armorArm.offsetY-=0.1;

    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet de saluer quelqu'un."
        };
    }
}
