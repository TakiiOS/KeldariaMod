package fr.nathanael2611.keldaria.mod.item;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class ItemArmorPart extends Item
{

    private final EntityEquipmentSlot REQUIRED_SLOT;
    private final HashMap<EnumAttackType, Integer> PROTECTIONS_PERCENT = Maps.newHashMap();
    private final double speedModifier;
    private final String defaultSkin;
    private final Item repairItem;

    public ItemArmorPart(Item repairItem, EntityEquipmentSlot slot, double speedModifier, int sharpProtection, int hitProtection, int thrustProtection, int duration)
    {
        this(repairItem, slot, speedModifier, sharpProtection, hitProtection, thrustProtection, duration, null);
    }

    public ItemArmorPart(Item repairItem, EntityEquipmentSlot slot, double speedModifier, int sharpProtection, int hitProtection, int thrustProtection, int duration, String defaultSkin)
    {
        this.defaultSkin = defaultSkin;
        setMaxStackSize(1);
        this.REQUIRED_SLOT = slot;
        this.speedModifier = speedModifier;
        this.PROTECTIONS_PERCENT.put(EnumAttackType.SHARP, sharpProtection);
        this.PROTECTIONS_PERCENT.put(EnumAttackType.HIT, hitProtection);
        this.PROTECTIONS_PERCENT.put(EnumAttackType.THRUST, thrustProtection);
        this.setMaxDamage(duration);
        this.repairItem = repairItem;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == this.repairItem;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (defaultSkin != null)
        {
            if (!ItemClothe.isClothValid(stack))
            {
                ItemClothe.setClothURL(stack, this.defaultSkin);
            }
        }
    }

    public EntityEquipmentSlot getRequiredSlot()
    {
        return REQUIRED_SLOT;
    }

    public int getProtectionPercent(EnumAttackType attackType)
    {
        return this.PROTECTIONS_PERCENT.getOrDefault(attackType, 0);
    }

    public double getSpeedModifier()
    {
        return speedModifier;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("§7Protections:");
        EnumAttackType sharp = EnumAttackType.SHARP;
        EnumAttackType thrust = EnumAttackType.THRUST;
        EnumAttackType hit = EnumAttackType.HIT;
        tooltip.add("§7 - " + sharp.getFormattedName() + ": §f" + getProtectionPercent(sharp) + "%");
        tooltip.add("§7 - " + thrust.getFormattedName() + ": §f" + getProtectionPercent(thrust) + "%");
        tooltip.add("§7 - " + hit.getFormattedName() + ": §f" + getProtectionPercent(hit) + "%");
        if(!ItemClothe.isClothValid(stack))
        {
            tooltip.add("§cCette pièce d'armure n'a pas de skin! Appelez un staff pour en mettre un le plus rapidement possible!");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    public boolean canDamageArmor(DamageSource source)
    {
        return source.getTrueSource() != null || source.isUnblockable() || source.isFireDamage() || source.isExplosion();
    }
}
