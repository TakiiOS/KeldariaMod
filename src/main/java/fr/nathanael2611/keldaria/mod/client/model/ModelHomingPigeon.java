/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.model;

import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ModelHomingPigeon extends ModelBase
{
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;

	public ModelHomingPigeon() {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(1.0F, 24.0F, 0.0F);
		setRotationAngle(bone, 0.1745F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -3.0F, -4.0F, -4.0F, 3, 3, 5, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 7, 18, -3.0F, -3.0F, -5.0F, 3, 2, 1, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 12, 18, -3.0F, -5.0F, -2.0F, 3, 1, 3, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 18, 0, -3.0F, -5.0F, 1.0F, 3, 3, 2, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-1.5F, -4.0F, 3.0F);
		setRotationAngle(bone2, -0.2618F, 0.0F, 0.0F);
		bone.addChild(bone2);
		bone2.cubeList.add(new ModelBox(bone2, 16, 9, -1.5F, -2.0F, -1.0F, 3, 3, 3, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 3, -0.5F, 0.0F, 2.0F, 1, 1, 1, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, -4.0F, 0.0F);
		setRotationAngle(bone3, -0.0873F, 0.0F, 0.0F);
		bone.addChild(bone3);
		bone3.cubeList.add(new ModelBox(bone3, 11, 3, 0.0F, -1.0F, -3.0F, 1, 1, 5, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 0, 8, 0.0F, 0.0F, -4.0F, 1, 2, 6, 0.0F, false));
		bone3.cubeList.add(new ModelBox(bone3, 21, 15, 0.0F, 2.0F, -3.0F, 1, 1, 4, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-3.0F, -4.0F, 0.0F);
		setRotationAngle(bone4, -0.0873F, 0.0F, 0.0F);
		bone.addChild(bone4);
		bone4.cubeList.add(new ModelBox(bone4, 11, 3, -1.0F, -1.0F, -3.0F, 1, 1, 5, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 0, 8, -1.0F, 0.0F, -4.0F, 1, 2, 6, 0.0F, false));
		bone4.cubeList.add(new ModelBox(bone4, 21, 15, -1.0F, 2.0F, -3.0F, 1, 1, 4, 0.0F, false));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(0.0F, -1.0F, -1.0F);
		setRotationAngle(bone5, -0.1745F, 0.0F, 0.0F);
		bone.addChild(bone5);
		bone5.cubeList.add(new ModelBox(bone5, 0, 0, -1.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F, false));
		bone5.cubeList.add(new ModelBox(bone5, 0, 8, -3.0F, -1.0F, 0.0F, 1, 2, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if(entity instanceof EntityHomingPigeon)
		{
			EntityHomingPigeon pigeon = (EntityHomingPigeon) entity;
			bone.render(f5);

			if(!pigeon.getLetter().isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.scale(-1, -1, -1);
				GlStateManager.translate(0, -1.4, 0);
				GlStateManager.translate(0, 0, -0.16);
				GlStateManager.scale(0.4, 0.4, 0.4);
				Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, pigeon.getLetter(), ItemCameraTransforms.TransformType.FIXED);
				GlStateManager.popMatrix();
			}
		}

	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if(entityIn instanceof EntityHomingPigeon)
		{
			EntityHomingPigeon pigeon = (EntityHomingPigeon) entityIn;
			this.bone3.rotateAngleY = 0F;
			this.bone4.rotateAngleY = 0F;
			this.bone5.rotateAngleY = 0f;
			this.bone5.rotationPointY = -1f;
			this.bone3.rotateAngleZ = 0F;
			this.bone4.rotateAngleZ = 0F;
			this.bone5.rotateAngleZ = 0f;

			this.bone3.rotateAngleX = 0F;
			this.bone4.rotateAngleX = 0F;
			this.bone5.rotateAngleX = 0F;
			this.bone2.rotateAngleX = 0F;
			this.bone2.rotateAngleZ = 0F;
			this.bone2.rotateAngleY = 0F;

			this.bone2.rotationPointY = -4f;
			this.bone2.rotateAngleY = netHeadYaw * 0.017453292F;
			this.bone2.rotateAngleX = -headPitch * 0.017453292F;


			if(pigeon.onGround)
			{

				//this.bone5.rotateAngleY = limbSwing;

			}
			else
			{

				this.bone5.rotateAngleX = -90;

				this.bone3.rotationPointY = -4;
				this.bone3.rotateAngleZ = -0.5f + MathHelper.cos(ageInTicks) * (float) Math.PI / 5F;

				this.bone4.rotationPointY = -4;
				this.bone4.rotateAngleZ =  0.5f - MathHelper.cos(ageInTicks) * (float) Math.PI / 5F;

			}
		}




		//bone3.rotateAngleY = limbSwing;
		//bone3.rotateAngleZ = limbSwing;
		//bone4.rotateAngleX = limbSwing;

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}