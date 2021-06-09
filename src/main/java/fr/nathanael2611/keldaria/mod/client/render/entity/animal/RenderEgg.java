package fr.nathanael2611.keldaria.mod.client.render.entity.animal;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.model.animal.ModelEgg;
import fr.nathanael2611.keldaria.mod.entity.animal.KeldaChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderEgg extends RenderKeldAnimal<KeldaChicken.KeldaEgg>
{

    public static final ResourceLocation LOC = new ResourceLocation(Keldaria.MOD_ID, "textures/entity/egg.png");

    public RenderEgg(RenderManager renderManager)
    {
        super(renderManager, new ModelEgg(), 0.2F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(KeldaChicken.KeldaEgg entity)
    {
        return LOC;
    }
}
