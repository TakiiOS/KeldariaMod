package fr.nathanael2611.keldaria.mod.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftIngredient
{

    private Item item;
    private int meta;

    public CraftIngredient(Item item, int meta)
    {
        this.item = item;
        this.meta = meta;
    }

    public Item getItem()
    {

        return item;
    }

    public int getMeta()
    {
        return meta;
    }

    public boolean isStackIsOfIngredient(ItemStack stack)
    {
        return stack.getItem() == this.item && stack.getMetadata() == meta;
    }

    public boolean isNeedSameMetadata()
    {
        return this.meta != -1;
    }
}
