/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.beerbarrel;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BeerBarrelRegistry
{

    private List<BeerCraft> REGISTY = Lists.newArrayList();

    private static final BeerCraft EMPTY = new BeerCraft(Lists.newArrayList(Items.AIR, Items.AIR, Items.AIR), Items.AIR, ItemStack.EMPTY, 0);

    public void init()
    {
        this.add(new BeerCraft(Lists.newArrayList(Items.SUGAR, KeldariaItems.ORGE, KeldariaItems.ORGE), KeldariaItems.EMPTY_CHOP, new ItemStack(KeldariaItems.BEER_CHOP), 200));
        this.add(new BeerCraft(Lists.newArrayList(Items.SUGAR, KeldariaItems.GRAPES, KeldariaItems.GRAPES), KeldariaItems.EMPTY_WINE_BOTTLE, new ItemStack(KeldariaItems.WINE_BOTTLE), 500));
    }

    public boolean contains(List<Item> ingredients, Item container)
    {
        return this.get(ingredients, container).getResult().getItem() != ItemStack.EMPTY.getItem();
    }

    public BeerCraft get(List<Item> ingredients, Item container)
    {
        if(ingredients.size() != 3) return EMPTY;
        BeerCraft craft = EMPTY;
        for (BeerCraft beerCraft : this.REGISTY)
        {
            if(container == beerCraft.getContainer() && ingredients.containsAll(beerCraft.getIngredients()))
            {
                //System.out.println(ingredients + " "  + beerCraft.getIngredients());
                craft = beerCraft;
            }
        }
        return craft;
    }

    private void add(BeerCraft craft)
    {
        this.REGISTY.add(craft);
    }

}
