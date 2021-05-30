/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.obfuscate.remastered.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBipedArmor extends ModelBiped
{
    private ModelBiped source;

    public ModelBipedArmor(final ModelBiped source, final float modelSize) {
        super(modelSize);
        this.source = source;

    }

    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn) {
        copyModelAngles(this.source.bipedHeadwear, this.bipedHeadwear);
        copyModelAngles(this.source.bipedHead, this.bipedHead);
        copyModelAngles(this.source.bipedBody, this.bipedBody);
        copyModelAngles(this.source.bipedRightArm, this.bipedRightArm);
        copyModelAngles(this.source.bipedLeftArm, this.bipedLeftArm);
        copyModelAngles(this.source.bipedRightLeg, this.bipedRightLeg);
        copyModelAngles(this.source.bipedLeftLeg, this.bipedLeftLeg);

        copyModelOffset(this.source.bipedHeadwear, this.bipedHeadwear);
        copyModelOffset(this.source.bipedHead, this.bipedHead);
        copyModelOffset(this.source.bipedBody, this.bipedBody);
        copyModelOffset(this.source.bipedRightArm, this.bipedRightArm);
        copyModelOffset(this.source.bipedLeftArm, this.bipedLeftArm);
        copyModelOffset(this.source.bipedRightLeg, this.bipedRightLeg);
        copyModelOffset(this.source.bipedLeftLeg, this.bipedLeftLeg);
    }

    public void copyModelOffset(ModelRenderer source, ModelRenderer destination)
    {
        destination.offsetX = source.offsetX;
        destination.offsetY = source.offsetY;
        destination.offsetZ = source.offsetZ;
    }
}
