/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.render.teisr;

import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.model.ModelRoundShield;
import fr.nathanael2611.keldaria.mod.features.ItemTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RoundShieldRenderer extends TileEntityItemStackRenderer
{

    public static final ResourceLocation ROUND_SHIELD = new ResourceLocation("keldaria", "textures/models/round_shield.png");
    private ModelRoundShield roundShield = new ModelRoundShield();

    @Override
    public void renderByItem(ItemStack itemStackIn)
    {

        {
            if(ItemTextures.hasTexture(itemStackIn))
            {
                ResourceLocation loc = ImageCache.getAsResourceLocation(ItemTextures.getTexture(itemStackIn), ImageCache.getBlank());
                if(loc != null) Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
            }
            else
            {
                Minecraft.getMinecraft().getTextureManager().bindTexture(ROUND_SHIELD);
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 0.15);
        GlStateManager.translate(-0.3, 0, 0);
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.translate(0, 0.5, -0);
        GlStateManager.scale(0.05F, 0.05F, 0.05F);

        this.roundShield.render(1);
        GlStateManager.popMatrix();

    }
}
