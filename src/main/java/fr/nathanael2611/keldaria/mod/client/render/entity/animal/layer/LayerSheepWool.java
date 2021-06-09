package fr.nathanael2611.keldaria.mod.client.render.entity.animal.layer;

import fr.nathanael2611.keldaria.mod.client.model.animal.ModelKeldaSheepFull;
import fr.nathanael2611.keldaria.mod.client.render.entity.animal.RenderSheep;
import fr.nathanael2611.keldaria.mod.entity.animal.KeldaSheep;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

public class LayerSheepWool implements LayerRenderer<KeldaSheep>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    private final RenderSheep sheepRenderer;
    private final ModelKeldaSheepFull sheepModel = new ModelKeldaSheepFull();

    public LayerSheepWool(RenderSheep sheepRendererIn)
    {
        this.sheepRenderer = sheepRendererIn;
    }

    public void doRenderLayer(KeldaSheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.getFleece().isSheared() && !entitylivingbaseIn.isInvisible())
        {
            this.sheepRenderer.bindTexture(TEXTURE);
            float[] afloat = EntitySheep.getDyeRgb(entitylivingbaseIn.getFleece().getColor());
            GlStateManager.color(afloat[0], afloat[1], afloat[2]);
            this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
            this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}