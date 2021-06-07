package fr.nathanael2611.keldaria.mod.client.render.entity.animal;

import fr.nathanael2611.keldaria.mod.entity.animal.EntityPig;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPig extends RenderKeldAnimal<EntityPig>
{
    private static final ResourceLocation PIG_TEXTURES = new ResourceLocation("textures/entity/pig/pig.png");

    public RenderPig(RenderManager p_i47198_1_)
    {
        super(p_i47198_1_, new ModelPig(), 0.7F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityPig entity)
    {
        return PIG_TEXTURES;
    }
}