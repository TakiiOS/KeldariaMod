/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCard extends Item
{

    public ItemCard()
    {
        setMaxStackSize(1);
    }


    public static String getCardLink(ItemStack stack)
    {
        return Helpers.getCompoundTag(stack).getString("CardLink");
    }

    public static void setCardLink(ItemStack stack, String link)
    {
        Helpers.getCompoundTag(stack).setString("CardLink", link);
    }

    public static ItemStack create(String cardName, String cardLink)
    {
        ItemStack stack = new ItemStack(KeldariaItems.CARD);
        stack.setStackDisplayName(cardName);
        setCardLink(stack, cardLink);
        return stack;
    }

}
