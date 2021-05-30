/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.combat;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class EntityStats
{

    public static EntityStats getStats(Entity entity)
    {
        if(entity instanceof IHasStat)
        {
            return ((IHasStat) entity).getCombatStats();
        }
        String name = entity.getName();
        String[] parts = name.split("=");
        if(parts.length == 2)
        {
            String infosStr = parts[1];
            String[] two = infosStr.split("-");
            if(two.length == 2)
            {
                EnumAttackType attackMode = EnumAttackType.byIdOrDefault(Helpers.parseOrZero(two[0]), EnumAttackType.HIT);
                String[] list = two[1].split(",");
                HashMap<EnumAttackType, Integer> resistance = Maps.newHashMap();
                for (int i = 0; i < list.length; i++)
                {
                    EnumAttackType attackType = EnumAttackType.byId(i);
                    if(attackMode != null)
                    {
                        resistance.put(attackType, Helpers.parseOrZero(list[i]));
                    }
                }
                return new EntityStats(attackMode, resistance);
            }
        }
        return new EntityStats(EnumAttackType.HIT, Maps.newHashMap());
    }

    private EnumAttackType attackType;
    private HashMap<EnumAttackType, Integer> resistance;

    public EntityStats(EnumAttackType attackType, HashMap<EnumAttackType, Integer> resistance)
    {
        this.attackType = attackType;
        this.resistance = resistance;
    }

    public EnumAttackType getAttackType()
    {
        return attackType;
    }

    public int getProtectionPercent(EnumAttackType attackType)
    {
        return this.resistance.getOrDefault(attackType, 0);
    }

    public static EntityStats from(EnumAttackType attackType, int sharpResistance, int hitResistance, int thrustResistance)
    {
        HashMap<EnumAttackType, Integer> resistances = Maps.newHashMap();
        resistances.put(EnumAttackType.SHARP, sharpResistance);
        resistances.put(EnumAttackType.HIT, hitResistance);
        resistances.put(EnumAttackType.THRUST, thrustResistance);
        return new EntityStats(attackType, resistances);
    }

}
