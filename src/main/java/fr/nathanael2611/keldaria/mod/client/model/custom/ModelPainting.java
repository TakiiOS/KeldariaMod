package fr.nathanael2611.keldaria.mod.client.model.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ModelPainting extends CustomModelBase
{

    private CustomModelRenderer renderer;

    public ModelPainting()
    {
        this.textureWidth = 1;
        this.textureHeight = 1;
        this.renderer = new CustomModelRenderer(this, 0, 0);
        this.renderer.addBox(0, 0, 0, textureWidth, textureHeight, 1);
    }

    public void render(ResourceLocation loc)
    {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
        GlStateManager.scale(-1, -1, -0.00001);
        this.renderer.render(1);
        GlStateManager.popMatrix();

    }

}
