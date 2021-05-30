/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.combat;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.item.ItemMace;
import fr.nathanael2611.keldaria.mod.item.ItemSpear;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.LinkedHashMap;

public class BlockingSystem
{

    private static final LinkedHashMap<Class, Integer> BLOCKING_ITEMS = Maps.newLinkedHashMap();
    private static final LinkedHashMap<Class, Integer> DAMAGES_REDUCES = Maps.newLinkedHashMap();

    static
    {
        BLOCKING_ITEMS.put(ItemShield.class, 60);
        BLOCKING_ITEMS.put(ItemMace.class, 40);
        BLOCKING_ITEMS.put(ItemSpear.class, 20);
        BLOCKING_ITEMS.put(ItemSword.class, 30);

        DAMAGES_REDUCES.put(ItemShield.class, 60);
        DAMAGES_REDUCES.put(ItemMace.class, 50);
        DAMAGES_REDUCES.put(ItemSpear.class, 20);
        DAMAGES_REDUCES.put(ItemSword.class, 40);
    }

    public static int getBaseBlockChances(Item item)
    {
        for (Class c : BLOCKING_ITEMS.keySet())
        {
            if(c.isInstance(item))
            {
                return BLOCKING_ITEMS.get(c);
            }
        }
        return 0;
    }

    public static int getBlockChances(EntityPlayer attacked, Entity attacker)
    {
        int base = getBaseBlockChances(attacked.getActiveItemStack().getItem());

        WeaponStat defenseStat = WeaponStat.getBlockingStat(attacked);
        WeaponStat attackStat = attacker instanceof EntityPlayer ? WeaponStat.getAttackStat((EntityPlayer) attacker) : null;

        double defenseStatPercent = defenseStat.getLevel(attacked) * 2.5;
        double resistanceStat = (EnumAptitudes.RESISTANCE.getPoints(attacked) - (attacker instanceof EntityPlayer ? EnumAptitudes.STRENGTH.getPoints((EntityPlayer) attacker) : 0)) * 5;
        double random = Helpers.randomDouble(0, 50 - resistanceStat);
        double chances = defenseStatPercent + resistanceStat + random;


        return base / 2 + (Helpers.crossMult(chances, 100, base));
    }

    public static int getReducePercent(Item item)
    {
        for (Class c : DAMAGES_REDUCES.keySet())
        {
            if(c.isInstance(item))
            {
                return DAMAGES_REDUCES.get(c);
            }
        }
        return 0;
    }

    public static float reduceDamages(float damage, Entity attacker, EntityPlayer attacked, ItemStack protectionItem)
    {
        int attackForce = attacker instanceof EntityPlayer ? EnumAptitudes.STRENGTH.getPoints((EntityPlayer) attacker) : 2;
        int resistance = EnumAptitudes.RESISTANCE.getPoints(attacked);
        int reduce = getReducePercent(protectionItem.getItem());

        WeaponStat defenseStat = WeaponStat.getBlockingStat(attacked);
        WeaponStat attackStat = attacker instanceof EntityPlayer ? WeaponStat.getAttackStat((EntityPlayer) attacker) : null;

        double defenseStatPercent = defenseStat.getLevel(attacked) * 2.5;
        double resistanceStat = (EnumAptitudes.RESISTANCE.getPoints(attacked) - (attacker instanceof EntityPlayer ? EnumAptitudes.STRENGTH.getPoints((EntityPlayer) attacker) : 0)) * 5;
        double random = Helpers.randomDouble(0, 50 - resistanceStat);
        double chances = defenseStatPercent + resistanceStat + random;


        reduce = reduce + (resistance * 10) - (attackForce * 5);
        return Helpers.crossMult(chances, 100, ((float) (damage * ((100 - reduce) * 0.01))));
    }

}
