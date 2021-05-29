package fr.nathanael2611.keldaria.mod.client.ren;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactory implements IRenderFactory<EntityPlayerDummy>
{
    public Render<? super EntityPlayerDummy> createRenderFor(final RenderManager manager) {
        return (Render<? super EntityPlayerDummy>)new RenderPlayerDummy(manager);
    }
}
