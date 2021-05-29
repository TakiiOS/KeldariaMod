package fr.nathanael2611.keldaria.mod.asm;

import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.util.IntHashMap;

public interface IEntityTrackerAccessor
{

    IntHashMap<EntityTrackerEntry> getTrackingEntries();

}
