/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.features.TimeBasedSaturation;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodStats.class)
public abstract class MixinFoodStats
{

    @Shadow private float foodExhaustionLevel;

    /**
     * @author
     */
    @Overwrite
    public void addExhaustion(float exhaustion)
    {
        this.foodExhaustionLevel = (float) Math.min(this.foodExhaustionLevel + (exhaustion * TimeBasedSaturation.getMultiplier()), 40.0F);
    }

}
