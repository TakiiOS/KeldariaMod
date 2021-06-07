package fr.nathanael2611.keldaria.mod.client.render.entity.animal;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.item.Item;

public class FoodEntry
{

    private Item item;
    private int affectionPercent;

    public FoodEntry(Item item, int affectionPercent)
    {
        this.item = item;
        this.affectionPercent = affectionPercent;
    }

    public Item getItem()
    {
        return item;
    }

    public int getAffectionPercent()
    {
        return affectionPercent;
    }

    public boolean processEat()
    {
        return Helpers.randomInteger(0, 100) <= affectionPercent;
    }

}
