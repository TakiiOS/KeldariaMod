/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.model;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.model.custom.CustomModelBase;
import fr.nathanael2611.keldaria.mod.client.model.custom.CustomModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.List;

public class ModelCustomItemTexture extends CustomModelBase
{

    private List<CustomModelRenderer> renderers = Lists.newArrayList();

    private BufferedImage img;
    private ResourceLocation rs;

    public ModelCustomItemTexture()
    {
        this.img = null;
        this.rs = null;
    }

    public ModelCustomItemTexture(String url)
    {
        this.img = ImageCache.get(url, null);
        this.textureWidth = img.getWidth();
        this.textureHeight = img.getHeight();
        for (int x = 0; x < img.getWidth(); x++)
        {
            int started = -1;
            for (int y = 0; y < img.getHeight(); y++)
            {
                int color = img.getRGB(x, y);

                if (started == -1 && !isTransparent(color))
                {
                    started = y;
                }
                else if(started != -1 && isTransparent(color))
                {
                    add(x, started, Math.max(0, y - started));
                    started = -1;
                }
            }
        }
    }

    private CustomModelRenderer builded;

    public void add(int x, int y, int height)
    {
        this.builded = new CustomModelRenderer(this, x, y);
        this.builded.addBox(x, y, 0, 1, height, 1);
        this.renderers.add(this.builded);
        this.builded = null;
    }

    private boolean isTransparent(int color)
    {
        return ((color >> 24) == 0x00);
    }

    public void render()
    {
        if(rs == null && this.img != null)
        {
            this.rs = ImageCache.getAsResourceLocation(this.img);
        }
        if(rs != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.32, 0.32, 0.52);
            GlStateManager.translate(0.2, 0.2, 0);
            GlStateManager.rotate(180, 0, 0, 1);
            {
                Minecraft.getMinecraft().getTextureManager().bindTexture(this.rs);
                this.renderers.forEach(model -> {
                    model .render(0.0625F);
                });
            }
            GlStateManager.popMatrix();
        }




    }


}
