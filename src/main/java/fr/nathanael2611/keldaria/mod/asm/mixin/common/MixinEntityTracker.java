/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.IEntityTrackerAccessor;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.util.IntHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTracker.class)
public abstract class MixinEntityTracker implements IEntityTrackerAccessor
{

    @Shadow @Final private IntHashMap<EntityTrackerEntry> trackedEntityHashTable;

    @Override
    public IntHashMap<EntityTrackerEntry> getTrackingEntries()
    {
        return this.trackedEntityHashTable;
    }
}
