/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.tesr;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.block.BlockCustomPainting;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.model.ModelClothingMannequin;
import fr.nathanael2611.keldaria.mod.clothe.Clothes;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityClothingMannequin;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

public class TileEntityClothingMannequinRenderer extends TileEntitySpecialRenderer<TileEntityClothingMannequin>
{

    public ModelPlayer modelMannequin = new ModelClothingMannequin(false);

    public static final HashMap<TileEntityClothingMannequin, ResourceLocation> LAST_RS = Maps.newHashMap();

    public static final HashMap<TileEntityClothingMannequin, String> LAST_CLOTHES = Maps.newHashMap();

    @Override
    public void render(TileEntityClothingMannequin te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        try
        {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.translate(x + 0.5F, y + 1.38F , z + 0.5F);
            List<String> clothes = te.getClothesURL();
            if(clothes.size() > 0)
            {
                String clothesAsString = Helpers.spaceListWithComa(clothes);
                String lastClothesAsString = LAST_CLOTHES.getOrDefault(te, "");
                if(!clothesAsString.equals(lastClothesAsString))
                {
                    Clothes obj = new Clothes("", clothes, new boolean[]{false, false, false, false});
                    BufferedImage skin = obj.assemble();
                    if(skin != null)
                    {
                        LAST_CLOTHES.put(te, clothesAsString);
                        LAST_RS.put(te, ImageCache.getAsResourceLocation(skin));
                    }
                }
                ResourceLocation loc = LAST_RS.get(te);
                if(loc != null)
                {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
                }
            }
            GlStateManager.scale(0.9, 0.9, 0.9);
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.rotate(180, 0, 1, 0);
            EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockCustomPainting.FACING);
            if(facing == EnumFacing.EAST)
            {
                GlStateManager.rotate(90, 0, 1, 0);
            }
            else if(facing == EnumFacing.WEST)
            {
                GlStateManager.rotate(90 + 180, 0, 1, 0);
            }
            else if(facing == EnumFacing.NORTH)
            {
            }
            else if (facing == EnumFacing.SOUTH)
            {
                GlStateManager.rotate(180, 0, 1, 0);
            }

            this.setLightmapDisabled(false);
            renderModel(te, modelMannequin);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
        catch (Exception ignored)
        {
        }

        GlStateManager.color(1, 1, 1, 1);
    }

    private void renderModel(TileEntityClothingMannequin te, ModelPlayer targetBiped)
    {
        GlStateManager.pushMatrix();
        //GlStateManager.translate(0, 1, 0);
        float SCALE = 0.0625F;
        {
            targetBiped.bipedHead.render(SCALE);
            GL11.glDisable(GL11.GL_CULL_FACE);
            targetBiped.bipedHeadwear.render(SCALE);
            GL11.glEnable(GL11.GL_CULL_FACE);

        }
        //GlStateManager.translate(0, -2, 0);
        targetBiped.bipedBody.render(SCALE);
        targetBiped.bipedBodyWear.render(SCALE);

        targetBiped.bipedRightArm.render(SCALE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -0.64);
        targetBiped.bipedRightArmwear.render(SCALE);
        GlStateManager.popMatrix();
        targetBiped.bipedLeftArm.render(SCALE);
        targetBiped.bipedLeftArmwear.render(SCALE);
        targetBiped.bipedRightLeg.render(SCALE);
        targetBiped.bipedRightLegwear.render(SCALE);
        targetBiped.bipedLeftLeg.render(SCALE);
        targetBiped.bipedLeftLegwear.render(SCALE);


        GlStateManager.popMatrix();
    }

}
