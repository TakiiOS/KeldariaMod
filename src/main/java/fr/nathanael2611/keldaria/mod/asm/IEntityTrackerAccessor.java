/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm;

import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.util.IntHashMap;

public interface IEntityTrackerAccessor
{

    IntHashMap<EntityTrackerEntry> getTrackingEntries();

}
