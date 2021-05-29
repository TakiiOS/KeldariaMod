package fr.nathanael2611.keldaria.mod.client.render.entity.bestiary;

import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.entity.bestiary.EntityBestiary;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class RenderBestiary<T extends EntityBestiary> extends RenderLiving<EntityBestiary>
{


    public RenderBestiary(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBestiary entity)
    {
        return entity.hasCustomTexture() ?
                ImageCache.getAsResourceLocation(entity.getCustomTexture(), ImageCache.getBlank()) :
                this.getBaseEntityTexture(entity);
    }

    protected abstract ResourceLocation getBaseEntityTexture(EntityBestiary entityBestiary);

}
