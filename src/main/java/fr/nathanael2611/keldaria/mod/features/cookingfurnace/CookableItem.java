package fr.nathanael2611.keldaria.mod.features.cookingfurnace;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CookableItem
{

    private final Item item;
    private final ItemStack result;
    private final int cookingTime;

    public CookableItem(Item item, ItemStack result, int cookingTime)
    {
        this.item = item;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    public Item getItem()
    {
        return item;
    }

    public ItemStack getResult()
    {
        return result.copy();
    }

    public int getCookingTime()
    {
        return cookingTime;
    }
}
