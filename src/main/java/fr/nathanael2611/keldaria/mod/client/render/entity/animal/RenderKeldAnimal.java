package fr.nathanael2611.keldaria.mod.client.render.entity.animal;

import fr.nathanael2611.keldaria.mod.entity.animal.EntityKeldAnimal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;

public abstract class RenderKeldAnimal<T extends EntityKeldAnimal> extends RenderLiving<T>
{
    public RenderKeldAnimal(RenderManager renderManager, ModelBase model, float shadowSize)
    {
        super(renderManager, model, shadowSize);
    }


    @Override
    protected void preRenderCallback(T animal, float partialTickTime)
    {
        double scale = animal.getStats().getSize();
        GlStateManager.scale(scale, scale, scale);
        super.preRenderCallback(animal, partialTickTime);
    }


}
