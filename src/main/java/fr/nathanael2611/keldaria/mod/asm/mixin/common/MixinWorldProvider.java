/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.SkyColor;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldProvider.class)
public abstract class MixinWorldProvider {

    @Shadow protected World world;

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
    {
        return MixinClientHooks.getSkyColor(world, cameraEntity, partialTicks);
    }

    @Overwrite
    public int getMoonPhase(long worldTime)
    {
        return KeldariaDate.lastDate.getMoonPhase();
    }
}
