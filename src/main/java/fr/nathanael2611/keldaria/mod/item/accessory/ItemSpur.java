package fr.nathanael2611.keldaria.mod.item.accessory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.List;

public class ItemSpur extends ItemAccessory
{

    private int spurSize;
    private float damage;
    private List<PotionEffect> potionEffects;

    public ItemSpur(int spurSize, float damage, List<PotionEffect> potionEffects)
    {
        this.spurSize = spurSize;
        this.damage = damage;
        this.potionEffects = potionEffects;
    }

    public int getSpurSize()
    {
        return spurSize;
    }

    public float getDamage()
    {
        return damage;
    }

    public List<PotionEffect> getPotionEffects()
    {
        return potionEffects;
    }

    @Override
    public EntityEquipmentSlot getRequiredEquipmentSlot()
    {
        return EntityEquipmentSlot.FEET;
    }

    @Override
    public void use(EntityPlayer player)
    {
        if(!player.world.isRemote)
        {
            if(player.isRiding() && player.getRidingEntity() instanceof AbstractHorse)
            {
                AbstractHorse horse = (AbstractHorse) player.getRidingEntity();
                horse.attackEntityFrom(DamageSource.GENERIC, this.damage);
                for (PotionEffect potionEffect : this.potionEffects)
                {
                    horse.addPotionEffect(new PotionEffect(potionEffect));
                }
            }
        }
    }
}
