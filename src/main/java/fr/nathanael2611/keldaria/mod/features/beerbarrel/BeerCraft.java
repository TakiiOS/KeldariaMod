/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.beerbarrel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BeerCraft
{

    private List<Item> ingredients;
    private Item container;
    private ItemStack result;
    private int fermentationTime;

    BeerCraft(List<Item> ingredients, Item container, ItemStack result, int fermentationTime)
    {
        this.ingredients = ingredients;
        this.result = result;
        this.container = container;
        this.fermentationTime = fermentationTime;
    }

    public List<Item> getIngredients()
    {
        return ingredients;
    }

    public ItemStack getResult()
    {
        return result.copy();
    }

    public Item getContainer()
    {
        return container;
    }

    public int getFermentationTime()
    {
        return fermentationTime;
    }
}
