/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.cookingfurnace;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will handle the cooking furnace crafting registry
 * Used for get all furnace crafts
 */
public class CookingRegistry {

    private HashMap<Item, CookableItem> cookableItems = new HashMap<>();

    /**
     * Initialize the Cooking Furnace crafting registry, and register all crafts
     */
    public void init()
    {
        this.cookableItems.clear();
        this.addCookableItem(new CookableItem(KeldariaItems.MUSHROOM_BOWL, new ItemStack(KeldariaItems.FRIED_MUSHROOM_BOWL), 240));
        this.addCookableItem(new CookableItem(KeldariaItems.BREAD_DOUGH, new ItemStack(Items.BREAD), 260));
        this.addCookableItem(new CookableItem(Items.POTATO, new ItemStack(Items.BAKED_POTATO), 120));
        this.addCookableItem(new CookableItem(KeldariaItems.BACON, new ItemStack(KeldariaItems.COOKED_BACON), 180));
        this.addCookableItem(new CookableItem(Items.POTIONITEM, new ItemStack(KeldariaItems.FRESH_WATER), 80));
        this.addVanillaRecipes();
    }

    private void addVanillaRecipes()
    {
        List<ItemStack> willRemoved = new ArrayList<>();
        Map<ItemStack, ItemStack> vanillaRecipes = FurnaceRecipes.instance().getSmeltingList();
        vanillaRecipes.forEach((entry, result) ->
        {
            if(entry.getItem() instanceof ItemFood || entry.getItem() instanceof ItemFood)
            {
                if(!cookableItems.containsKey(entry.getItem()))
                {
                    addCookableItem(new CookableItem(entry.getItem(), result, 300 * 3));
                }
                willRemoved.add(entry);
            }
        });
        willRemoved.forEach(vanillaRecipes::remove);
    }

    public void addCookableItem(CookableItem item)
    {
        cookableItems.put(item.getItem(), item);
    }

    public HashMap<Item, CookableItem> getCookableItems()
    {
        return cookableItems;
    }

    public boolean containsCookEntry(Item entry)
    {
        return cookableItems.containsKey(entry);
    }

    public CookableItem getCookableItem(Item entry)
    {
        if(containsCookEntry(entry)) return cookableItems.get(entry);
        return null;
    }
}
