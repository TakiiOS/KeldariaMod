package fr.nathanael2611.keldaria.mod.client.render.entity.bestiary;

import fr.nathanael2611.keldaria.mod.client.model.bestiary.ModelTarantula;
import fr.nathanael2611.keldaria.mod.entity.bestiary.EntityBestiary;
import fr.nathanael2611.keldaria.mod.entity.bestiary.spider.Spider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBestiarySpider extends RenderBestiary<Spider>
{

    public RenderBestiarySpider(RenderManager renderManager)
    {
        super(renderManager, new ModelTarantula(), 1);
    }

    @Override
    protected void preRenderCallback(EntityBestiary entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);

        if(entitylivingbaseIn instanceof Spider)
        {
            Spider spider = (Spider) entitylivingbaseIn;
            GlStateManager.scale(spider.getType().getSize(), spider.getType().getSize(), spider.getType().getSize());
        }

    }

    @Override
    protected ResourceLocation getBaseEntityTexture(EntityBestiary entityBestiary)
    {
        return new ResourceLocation("keldaria", "textures/entity/spider/tarantula.png");
    }
}
