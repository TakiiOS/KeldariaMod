package fr.nathanael2611.keldaria.mod.features.combat;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.item.ItemDagger;
import fr.nathanael2611.keldaria.mod.item.ItemMace;
import fr.nathanael2611.keldaria.mod.item.ItemSpear;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;

public enum EnumAttackType
{

    SHARP(0, "Entaille", true, 10),
    HIT(1, "Frappe", true, 60),
    THRUST(2, "Estoc", true, 80),
    UNBLOCKABLE(-1, "Unblockable", false, 0);

    public static final String KEY_ATTACK_TYPE = "keldaria:attacktype";

    private int id;
    private String formattedName;
    private boolean isUseableByPlayer;
    private int cooldownTicks;

    EnumAttackType(int id, String formattedName, boolean isUseableByPlayer, int cooldownTicks)
    {
        this.id = id;
        this.formattedName = formattedName;
        this.isUseableByPlayer = isUseableByPlayer;
        this.cooldownTicks = cooldownTicks;
    }

    public int getId()
    {
        return id;
    }

    public boolean isUseableByPlayer()
    {
        return isUseableByPlayer;
    }

    public String getFormattedName()
    {
        return formattedName;
    }

    public boolean isUnblockable()
    {
        return this == UNBLOCKABLE;
    }

    public int getCooldownTicks()
    {
        return cooldownTicks;
    }

    public void applyCooldown(EntityPlayer player)
    {
        WeaponStat stat = WeaponStat.getAttackStat(player);
        int ticks = this.cooldownTicks / 2 + (20 - Helpers.crossMult(stat != null ? stat.getLevel(player) : 0, 20, this.cooldownTicks / 2));
        if (player.getHeldItemOffhand().isEmpty())
        {
            ticks /= 2;
        }
        player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), ticks);
        player.getCooldownTracker().setCooldown(player.getHeldItemOffhand().getItem(), ticks);
    }

    public static EnumAttackType byIdOrDefault(int id, EnumAttackType def)
    {
        EnumAttackType attackType = byId(id);
        return attackType != null ? attackType : def;
    }

    public static EnumAttackType byId(int id)
    {
        for (EnumAttackType value : values())
        {
            if(value.id == id) return value;
        }
        return null;
    }

    public static double getMultiplier(ItemStack stack, EnumAttackType attackType)
    {
        if(stack.getItem() instanceof ItemSpear)
        {
            if(attackType == SHARP) return 0.9;
            if(attackType == HIT) return 0.3;
            if(attackType == THRUST) return 1.35;
        }
        else if(stack.getItem() instanceof ItemDagger)
        {
            if(attackType == SHARP) return 0.9;
            if(attackType == HIT) return 0.2;
            if(attackType == THRUST) return 1.5;
        }
        else if(stack.getItem() instanceof ItemSword)
        {
            if(attackType == SHARP) return 1.1;
            if(attackType == HIT) return 0.4;
            if(attackType == THRUST) return 1.4;
        }
        else if(stack.getItem() instanceof ItemMace)
        {
            if(attackType == SHARP) return  0.2;
            if(attackType == THRUST) return 0.2;
            if(attackType == HIT) return 1.6;
        }
        else if(stack.getItem() instanceof ItemShield)
        {
            if(attackType == SHARP) return  1.3;
            if(attackType == THRUST) return 1.05;
            if(attackType == HIT) return 1.5;
        }
        return 1;
    }

    public static EnumAttackType getPreferedAttackType(ItemStack stack)
    {
        if(stack.isEmpty()) return EnumAttackType.HIT;
        Item item = stack.getItem();
        if(item instanceof ItemDagger) return EnumAttackType.THRUST;
        if(item instanceof ItemSword) return EnumAttackType.SHARP;
        if(item instanceof ItemMace) return EnumAttackType.HIT;
        if(item == Items.STICK) return EnumAttackType.HIT;
        if(item instanceof ItemAxe) return EnumAttackType.SHARP;
        return EnumAttackType.HIT;
    }

    public static EnumAttackType getAttackType(EntityPlayer player)
    {
        return byIdOrDefault((player.world.isRemote ? ClientDatabases.getDatabase(KEY_ATTACK_TYPE) : Databases.getDatabase(KEY_ATTACK_TYPE)).getInteger(player.getName()), EnumAttackType.HIT);
    }

    public static void setAttackType(EntityPlayer player, EnumAttackType type)
    {
        if(type.isUseableByPlayer())
        {
            Databases.getDatabase(KEY_ATTACK_TYPE).setInteger(player.getName(), type.getId());
        }
    }

    public static void switchAttackType(EntityPlayer player)
    {

        Database db = Databases.getDatabase(KEY_ATTACK_TYPE);
        int actual = db.getInteger(player.getName());
        EnumAttackType needed = byId(actual + 1);
        if(needed != null && needed.isUseableByPlayer())
        {
            setAttackType(player, needed);
        }
        else
        {
            setAttackType(player, byIdOrDefault(0 , SHARP));
        }
        EnumAptitudes.updateModifiers(player);
    }

    public static EnumAttackType getAttackType(DamageSource source)
    {
        if(source.isProjectile()) return THRUST;
        else if(source.isExplosion()) return HIT;
        else if(source.isFireDamage()) return UNBLOCKABLE;
        else if(source == DamageSource.FALL) return UNBLOCKABLE;
        else if(source.getTrueSource() != null)
        {
            Entity trueSource = source.getTrueSource();
            if(trueSource instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) trueSource;
                return getAttackType(player);
            }
            else
            {
                EntityStats stats = EntityStats.getStats(trueSource);
                return stats.getAttackType();
            }
        }
        return UNBLOCKABLE;
    }

}
