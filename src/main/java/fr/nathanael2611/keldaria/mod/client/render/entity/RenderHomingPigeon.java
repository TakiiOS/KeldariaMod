package fr.nathanael2611.keldaria.mod.client.render.entity;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.model.ModelHomingPigeon;
import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderHomingPigeon extends RenderLiving<EntityHomingPigeon>
{

    private static final ResourceLocation TEXTURE = new ResourceLocation(Keldaria.MOD_ID, "textures/entity/homingpigeon/homingpigeon.png");
    private static final ResourceLocation TEXTURE_TAMED = new ResourceLocation(Keldaria.MOD_ID, "textures/entity/homingpigeon/homingpigeon_tamed.png");

    private static final ModelHomingPigeon MODEL = new ModelHomingPigeon();

    public RenderHomingPigeon(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, MODEL, 0.3F);
    }

    @Override
    protected void preRenderCallback(EntityHomingPigeon entitylivingbaseIn, float partialTickTime)
    {
        //super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        //GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.rotate(180, 0, 1, 0);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityHomingPigeon entity)
    {
        return entity.isTamed() ? TEXTURE_TAMED : TEXTURE;
    }
}
