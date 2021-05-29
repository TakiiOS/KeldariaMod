package fr.nathanael2611.keldaria.mod.features.combatstats;

import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.item.ItemDagger;
import fr.nathanael2611.keldaria.mod.item.ItemMace;
import fr.nathanael2611.keldaria.mod.item.ItemSpear;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;

public enum WeaponStat
{

    SWORD("sword", "Epéiste", Items.IRON_SWORD), SPEAR("spear", "Lancier", KeldariaItems.IRON_SPEAR), DAGGER("dagger", "Dague", KeldariaItems.IRON_DAGGER), MACE("mace", "Masse", KeldariaItems.IRON_MACE), AXE("axe", "Hache", Items.IRON_AXE), ARCHER("archer", "Archer", Items.BOW), SHIELD("shield", "Bouclier", Items.SHIELD),
    ;

    private String key;
    private String name;
    private Item weaponItem;

    WeaponStat(String key, String name, Item weaponItem)
    {
        this.key = "keldaria:weaponstat." + key;
        this.name = name;
        this.weaponItem = weaponItem;
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }

    public Item getWeaponItem()
    {
        return weaponItem;
    }

    public double getPercent(EntityPlayer player)
    {
        return Helpers.getPercent(this.getLevel(player), 20D);
    }

    public double getLevel(EntityPlayer player)
    {
        if (player.world.isRemote)
        {
            return ClientDatabases.getPersonalPlayerData().getDouble(this.key);
        } else
        {
            return Databases.getPlayerData(player).getDouble(this.key);
        }
    }

    public void set(EntityPlayer player, double points)
    {
        Database db = Databases.getPlayerData(player);
        db.setDouble(this.key, points);
    }

    public void increment(EntityPlayer player, double level)
    {
        this.set(player, Math.max(0, Math.min(this.getLevel(player) + level, 20)));
    }

    public void decrement(EntityPlayer player, double level)
    {
        this.increment(player, -level);
    }

    public void naturalIncrement(EntityPlayer player, double level)
    {
        double actualLevel = getLevel(player);

        double diviser = (actualLevel + 1);
        if (actualLevel > 16)
        {
            diviser *= 3;
        } else if (actualLevel > 13)
        {
            diviser *= 2;
        } else if (actualLevel > 8)
        {
            diviser *= 1.5;
        }

        diviser *= 1 + ((EnumAptitudes.INTELLIGENCE.getPoints(player) * 0.1));

        level /= diviser;

        this.increment(player, level);
    }

    public void naturalDecrement(EntityPlayer player, double level)
    {
        this.naturalIncrement(player, -level);
    }

    public static ItemStack getAttackItem(EntityPlayer player)
    {
        return player.getHeldItemMainhand();
    }

    public static ItemStack getBlockingItem(EntityPlayer player)
    {
        return player.isActiveItemStackBlocking() ? player.getActiveItemStack() : ItemStack.EMPTY;
    }

    public static double getStatPercent(double damage, DamageSource source, EntityPlayer attacker, EntityLivingBase attacked)
    {
        WeaponStat stat = getStatForDamageSource(attacker, source);
        if(stat == null)
        {
            return 0;
        }
        double attackerStat = stat != null ? stat.getLevel(attacker) : 0;
        double attackedStat = attacked instanceof EntityPlayer ? stat.getLevel((EntityPlayer) attacked) : 0;
        double percent = (attackerStat * 2.5) - (attackedStat * 0.5);
        return percent;
    }

    public static WeaponStat getStatForDamageSource(EntityPlayer attacker, DamageSource source)
    {
        if (source.isProjectile()) return ARCHER;
        else return getAttackStat(attacker);
    }

    public static WeaponStat getBlockingStat(EntityPlayer player)
    {
        return getStatFor(getBlockingItem(player));
    }

    public static WeaponStat getAttackStat(EntityPlayer player)
    {
        return getStatFor(getAttackItem(player));
    }

    public static WeaponStat getStatFor(ItemStack stack)
    {
        Item item = stack.getItem();
        return item instanceof ItemBow ? ARCHER :
                item instanceof ItemShield ? SHIELD :
                        item instanceof ItemMace ? MACE :
                                item instanceof ItemAxe ? AXE :
                                        item instanceof ItemSpear ? SPEAR :
                                                item instanceof ItemDagger ? DAGGER :
                                                        item instanceof ItemSword ? SWORD : null;
    }

}
