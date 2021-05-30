/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.obfuscate.remastered.client;

import fr.nathanael2611.obfuscate.remastered.client.model.ModelBipedArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class ObfuscateEvents
{
    private boolean setupPlayerRender;

    public ObfuscateEvents() {
        this.setupPlayerRender = false;
    }

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (!this.setupPlayerRender) {
            final Render render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject((Class) AbstractClientPlayer.class);

            /*Map<String, RenderPlayer> customSkinMap = Maps.<String, RenderPlayer>newHashMap();
            customSkinMap.put("default", new RenderCustomPlayer(render.getRenderManager()));
            customSkinMap.put("slim", new RenderCustomPlayer(render.getRenderManager(), true));
            ObfuscationReflectionHelper.setPrivateValue(
                    RenderManager.class, render.getRenderManager(),
                    customSkinMap, "skinMap","field_178636_l"
            );

            ObfuscationReflectionHelper.findMethod().*/
            final Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>)render.getRenderManager().getSkinMap();
            this.patchPlayerRender(skinMap.get("default"), false);
            this.patchPlayerRender(skinMap.get("slim"), true);
            this.setupPlayerRender = true;
        }
    }

    private void patchPlayerRender(RenderPlayer player, final boolean smallArms) {
        final ModelBiped model = player.getMainModel();
        final List<LayerRenderer<EntityLivingBase>> layers = (List<LayerRenderer<EntityLivingBase>>) ObfuscationReflectionHelper.getPrivateValue((Class) RenderLivingBase.class, (Object)player, new String[] { "field_177097_h" });
        if (layers != null) {
            final ModelBiped source = model;
            layers.forEach(layerRenderer -> {
                if (layerRenderer instanceof LayerBipedArmor) {
                    this.patchArmor((LayerBipedArmor) layerRenderer, source);
                }
            });
        }
        ObfuscationReflectionHelper.setPrivateValue((Class) RenderLivingBase.class, (Object)player, (Object)model, new String[] { "field_77045_g" });
    }

    private void patchArmor(final LayerBipedArmor layerBipedArmor, final ModelBiped source) {
        ObfuscationReflectionHelper.setPrivateValue((Class) LayerArmorBase.class, (Object)layerBipedArmor, (Object)new ModelBipedArmor(source, 1.0f), new String[] { "field_177186_d" });
        ObfuscationReflectionHelper.setPrivateValue((Class) LayerArmorBase.class, (Object)layerBipedArmor, (Object)new ModelBipedArmor(source, 0.5f), new String[] { "field_177189_c" });
    }
}
