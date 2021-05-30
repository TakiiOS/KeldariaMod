/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import net.minecraft.item.ItemFood;

/**
 * This class create a food item who is automatically in a CreativeTab
 * It also add some useful functions
 *
 * @author Nathanael2611
 */
public class ItemFoodBase extends ItemFood
{

    public ItemFoodBase(int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);
        //this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

    public ItemFoodBase(int amount, boolean isWolfFood)
    {
        super(amount, isWolfFood);
        //this.setCreativeTab(KeldariaTabs.KELDARIA);
    }

}
