/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.item.ItemCard;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityCardCarpet extends TileEntityFurnitureContainer
{

    public TileEntityCardCarpet()
    {
        super(9, 3, true);
    }

    public List<String> getCards()
    {
        List<String> links = Lists.newArrayList();
        for (ItemStack stack : this.getItemsList())
        {
            links.add(ItemCard.getCardLink(stack));
        }
        return links;
    }

}
