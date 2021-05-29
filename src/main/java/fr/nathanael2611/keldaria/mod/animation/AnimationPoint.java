package fr.nathanael2611.keldaria.mod.animation;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

public class AnimationPoint extends Animation
{
    public AnimationPoint()
    {
        super("point", false);
    }

    @Override
    void render(EntityPlayer player, ModelPlayer model, float partialTicks)
    {
        ModelRenderer renderer = player.getPrimaryHand() == EnumHandSide.RIGHT ? model.bipedRightArm : model.bipedLeftArm;
        renderer.rotateAngleZ = model.bipedHead.rotateAngleZ;
        renderer.rotateAngleX = 80 + model.bipedHead.rotateAngleX * 1.5f;
        renderer.rotateAngleY = model.bipedHead.rotateAngleY;
    }

    @Override
    public String[] getDescription()
    {
        return new String[]{
                "§e"+getName().toUpperCase(),
                "§7Permet de pointer quelque chose."
        };
    }
}
