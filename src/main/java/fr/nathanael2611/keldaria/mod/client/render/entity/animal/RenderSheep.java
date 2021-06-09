package fr.nathanael2611.keldaria.mod.client.render.entity.animal;

import fr.nathanael2611.keldaria.mod.client.model.animal.ModelKeldaSheep;
import fr.nathanael2611.keldaria.mod.client.render.entity.animal.layer.LayerSheepWool;
import fr.nathanael2611.keldaria.mod.entity.animal.KeldaSheep;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSheep extends RenderKeldAnimal<KeldaSheep>
{
    private static final ResourceLocation SHEARED_SHEEP_TEXTURES = new ResourceLocation("textures/entity/sheep/sheep.png");

    public RenderSheep(RenderManager p_i47195_1_)
    {
        super(p_i47195_1_, new ModelKeldaSheep(), 0.7F);
        this.addLayer(new LayerSheepWool(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(KeldaSheep entity)
    {
        return SHEARED_SHEEP_TEXTURES;
    }
}