/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class ItemSpear extends ItemSword
{

    public ItemSpear(ToolMaterial material)
    {
        super(material);

    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            //multimap.remove(SharedMonsterAttributes.ATTACK_SPEED.getName(), multimap.get(SharedMonsterAttributes.ATTACK_SPEED.getName()));
            //multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), multimap.get(SharedMonsterAttributes.ATTACK_SPEED.getName()).fo);
            /*AtomicReference<AttributeModifier> modifier = new AtomicReference<>();
            multimap.forEach((s, attributeModifier) -> {
                if(s.equals(SharedMonsterAttributes.ATTACK_SPEED.getName()))
                {
                    modifier.set(attributeModifier);
                }
            });
            multimap.remove(SharedMonsterAttributes.ATTACK_SPEED.getName(), modifier.get());*/
            multimap.get(SharedMonsterAttributes.ATTACK_SPEED.getName()).clear();
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.65D, 0));
        }

        return multimap;
    }

}
