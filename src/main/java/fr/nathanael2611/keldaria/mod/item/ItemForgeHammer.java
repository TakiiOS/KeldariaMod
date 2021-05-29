package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.item.Item;

public class ItemForgeHammer extends Item {
    public ItemForgeHammer(){
        setCreativeTab(KeldariaTabs.KELDARIA);
        setMaxStackSize(1);
        setMaxDamage(30);
    }

    @Override
    public boolean isDamageable()
    {
        return true;
    }
}
