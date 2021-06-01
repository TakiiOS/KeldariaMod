package fr.nathanael2611.keldaria.mod.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWoodBit extends Item
{

    @Override
    public int getItemBurnTime(ItemStack itemStack)
    {
        return 20*5;
    }


}
