package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import net.minecraft.item.Item;

/**
 * Coins is the server money
 *
 * @author Nathanael2611
 */
public class ItemCoin extends Item
{
    /**
     * Constructor
     */
    public ItemCoin()
    {
        setMaxStackSize(50);
        setCreativeTab(KeldariaTabs.KELDARIA);
    }
}
