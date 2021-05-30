/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class ModelHelpers {

    // RenderLivingBase
    public static final String LAYER_RENDERERS = "field_177097_h";

    // LayerArmorBase
    public static final String MODEL_ARMOR = "field_177186_d";
    public static final String MODEL_LEGGINGS = "field_177189_c";

    public static List<LayerRenderer<AbstractClientPlayer>> getLayerRenderers(AbstractClientPlayer player)
    {
        RenderPlayer render = getRenderPlayer(player);
        if(render == null) return null;
        return ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, render, LAYER_RENDERERS);
    }

    public static ModelBiped getPlayerChestplateModel(AbstractClientPlayer player) {
        for (Object aList : getLayerRenderers(player))
            if (aList instanceof LayerBipedArmor)
                return ObfuscationReflectionHelper.getPrivateValue(LayerArmorBase.class, (LayerArmorBase) aList, MODEL_ARMOR, "modelArmor");
        return null;
    }

    public static ModelBiped getPlayerLeggingsModel(AbstractClientPlayer player) {
        for (Object aList : getLayerRenderers(player))
            if (aList instanceof LayerBipedArmor)
                return ObfuscationReflectionHelper.getPrivateValue(LayerArmorBase.class, (LayerArmorBase) aList, MODEL_LEGGINGS, "modelLeggings");
        return null;
    }


    public static RenderPlayer getRenderPlayer(AbstractClientPlayer player)
    {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager manager = mc.getRenderManager();
        return manager.getSkinMap().get(player.getSkinType());
    }
}
