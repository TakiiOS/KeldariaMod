package fr.nathanael2611.keldaria.mod.animation;

import fr.nathanael2611.keldaria.mod.util.ModelHelpers;
import fr.nathanael2611.obfuscate.remastered.client.ModelPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is the parent of all animations classes.
 * It define basic player animation and all useful functions.
 *
 * @author Nathanael2611
 */
public class Animation {

    private String animationName;
    private boolean isSneakModifyHitbox;

    @SideOnly(Side.CLIENT)
    protected ModelBiped chestplate;
    @SideOnly(Side.CLIENT)
    protected ModelBiped leggings;

    public Animation(String name){
        this.animationName = name;
        this.isSneakModifyHitbox = true;
    }
    public Animation(String name, boolean isSneakModifyHitbox){
        this.animationName = name;
        this.isSneakModifyHitbox = isSneakModifyHitbox;
    }

    int timer = 0;

    /**
     * This method is used to modify the playerModel to adapt it to the animation.
     */
    public void renderAnimation(ModelPlayerEvent event){
        timer++;


        chestplate = ModelHelpers.getPlayerChestplateModel((AbstractClientPlayer)event.getEntityPlayer());
        leggings = ModelHelpers.getPlayerLeggingsModel((AbstractClientPlayer)event.getEntityPlayer());

        if(chestplate != null){
            chestplate.bipedRightArm.offsetY = 0;
            chestplate.bipedLeftArm.offsetY = 0;

            chestplate.bipedHeadwear.offsetY = 0;
            chestplate.bipedHead.offsetY = 0;
            chestplate.bipedBody.offsetY = 0;

            chestplate.bipedRightArm.offsetX = 0;
            chestplate.bipedLeftArm.offsetX = 0;

            chestplate.bipedHeadwear.offsetX = 0;
            chestplate.bipedHead.offsetX = 0;
            chestplate.bipedBody.offsetX = 0;

            chestplate.bipedRightArm.offsetZ = 0;
            chestplate.bipedLeftArm.offsetZ = 0;

            chestplate.bipedHeadwear.offsetZ = 0;
            chestplate.bipedHead.offsetZ = 0;
            chestplate.bipedBody.offsetZ = 0;

            chestplate.bipedRightLeg.offsetY = 0;
            chestplate.bipedLeftLeg.offsetY = 0;

            chestplate.bipedRightLeg.offsetX = 0;
            chestplate.bipedLeftLeg.offsetX = 0;

            chestplate.bipedRightLeg.offsetZ = 0;
            chestplate.bipedLeftLeg.offsetZ = 0;
        }
        if(leggings != null){
            leggings.bipedRightLeg.offsetY = 0;
            leggings.bipedLeftLeg.offsetY = 0;

            leggings.bipedRightLeg.offsetX = 0;
            leggings.bipedLeftLeg.offsetX = 0;

            leggings.bipedRightLeg.offsetZ = 0;
            leggings.bipedLeftLeg.offsetZ = 0;

        }

        render(event.getEntityPlayer(), event.getModelPlayer(), event.getPartialTicks());

    }

    void render(EntityPlayer player, ModelPlayer model, float partialTicks) {
    }


    public String getName() {
        return animationName;
    }


    public AxisAlignedBB getAnimationHitbox(EntityPlayer player, float scale)
    {
        float width  = getWidth(scale);
        float height = getHeight(scale);
        return createHitboxByDimensions(player, width, height);
    }

    public AxisAlignedBB getAnimationSneakingHitbox(EntityPlayer player, float scale)
    {
        float width  = getWidth(scale);
        float height = getSneakingHeight(scale);
        return createHitboxByDimensions(player, width, height);
    }

    public static AxisAlignedBB createHitboxByDimensions(Entity player, float width, float height)
    {
        return new AxisAlignedBB(
                player.posX - width / 2,
                player.posY,
                player.posZ - width / 2,
                player.posX + width / 2,
                player.posY + height,
                player.posZ + width / 2
        );
    }

    public float getHeight(float scale)
    {
        return 1.8F * scale;
    }

    public float getSneakingHeight(float scale)
    {
        return getHeight(scale) - (getHeight(scale) / 5);
    }

    public float getWidth(float scale)
    {
        return 0.6f * scale;
    }

    public float getEyeHeight(float scale)
    {
        return getHeight(scale) - (0.18f * scale);
    }

    public float getSneakingEyeHeight(float scale){
        return getEyeHeight(scale)-getEyeHeight(scale)/5;
    }

    public boolean isStopOnMove(){
        return true;
    }

    public boolean isSneakModifyHitbox() {
        return isSneakModifyHitbox;
    }

    public boolean isPlayerCanMoveIfSneaked()
    {
        return true;
    }

    public boolean isInAnimationMenu()
    {
        return true;
    }

    public String[] getDescription(){
        return new String[]{
            "§eNONE",
            "§7Permet de restaurer l'animation par défaut."
        };
    }

    public boolean isElongated()
    {
        return false;
    }
}
