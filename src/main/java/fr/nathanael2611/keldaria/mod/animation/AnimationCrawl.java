package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;


public class AnimationCrawl extends Animation{
    public AnimationCrawl() {
        super("crawl", false);
    }

    float timer = 0;

    @Override
    void render (EntityPlayer player, ModelPlayer model, float partialTicks) {

        model.isSneak = false;
        double translateYValue = 0.7;
        if(player.isSneaking())translateYValue-=0.17;
        GlStateManager.translate(0, translateYValue, 0);


        model.bipedBody.rotateAngleX=(float)Math.toRadians(90);
        model.bipedRightLeg.offsetY=-0.75f;
        model.bipedLeftLeg.offsetY=-0.75f;
        model.bipedRightLegwear.offsetY=-0.75f;
        model.bipedLeftLegwear.offsetY=-0.75f;


        if(
                player.posX != player.lastTickPosX ||
                player.posY != player.lastTickPosY ||
                player.posZ != player.lastTickPosZ
        ){
            timer +=0.003;

        }
        if(timer>0.3 ){
            timer = 0;
        }

        if(timer<0.15){
            model.bipedRightArm.offsetZ+=0.3-timer -0.15;
            model.bipedLeftArm.offsetZ+=timer;
            model.bipedRightArmwear.offsetZ+=0.3-timer -0.15;
            model.bipedLeftArmwear.offsetZ+=timer;

            model.bipedRightLeg.offsetZ+=0.55f+timer;
            model.bipedLeftLeg.offsetZ+=0.55f+0.3-timer -0.15;
            model.bipedRightLegwear.offsetZ+=0.55f+timer;
            model.bipedLeftLegwear.offsetZ+=0.55f+0.3-timer -0.15;

            if(leggings != null){
                leggings.bipedRightLeg.offsetZ+=0.55f+timer;
                leggings.bipedLeftLeg.offsetZ+=0.55f+0.3-timer -0.15;
            }
        }else{
            model.bipedRightArm.offsetZ+=timer -0.15;
            model.bipedLeftArm.offsetZ+=0.3-timer;
            model.bipedRightArmwear.offsetZ+=timer -0.15;
            model.bipedLeftArmwear.offsetZ+=0.3-timer;

            model.bipedRightLeg.offsetZ+=0.55f+0.3-timer;
            model.bipedLeftLeg.offsetZ+=0.55f+timer -0.15;
            model.bipedRightLegwear.offsetZ+=0.55f+0.3-timer;
            model.bipedLeftLegwear.offsetZ+=0.55f+timer -0.15;
        }

        model.bipedRightLeg.rotateAngleX=(model.bipedRightLeg.rotateAngleX/9)+(float)Math.toRadians(90);
        model.bipedLeftLeg.rotateAngleX=(model.bipedLeftLeg.rotateAngleX/9)+(float)Math.toRadians(90);
        model.bipedRightLeg.rotateAngleY+=(float)Math.toRadians(-6);
        model.bipedLeftLeg.rotateAngleY+=(float)Math.toRadians(6);
        model.bipedRightLeg.rotateAngleZ+=(float)Math.toRadians(-6);
        model.bipedLeftLeg.rotateAngleZ+=(float)Math.toRadians(6);



        model.bipedRightArm.rotateAngleX=(float)Math.toRadians(-90);
        model.bipedLeftArm.rotateAngleX=(float)Math.toRadians(-90);
        model.bipedRightArm.rotateAngleY=(float)Math.toRadians(20);
        model.bipedLeftArm.rotateAngleY=(float)Math.toRadians(-20);

        model.bipedRightArm.offsetY-=0.115;
        model.bipedLeftArm.offsetY-=0.115;
        model.bipedRightArmwear.offsetY-=0.115;
        model.bipedLeftArmwear.offsetY-=0.115;

        model.bipedRightArm.offsetX+=0.115;
        model.bipedLeftArm.offsetX-=0.115;
        model.bipedRightArmwear.offsetX+=0.115;
        model.bipedLeftArmwear.offsetX-=0.115;
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
    public String[] getDescription(){
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet de ramper."
        };
    }

    @Override
    public boolean isElongated() {
        return true;
    }

}
