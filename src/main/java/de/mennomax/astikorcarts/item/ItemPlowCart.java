/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.item;

import de.mennomax.astikorcarts.entity.AbstractDrawn;
import de.mennomax.astikorcarts.entity.EntityPlowCart;
import net.minecraft.world.World;

public class ItemPlowCart extends AbstractCartItem
{
    public ItemPlowCart()
    {
        super("plowcart");
    }

    @Override
    public AbstractDrawn newCart(World worldIn)
    {
        return new EntityPlowCart(worldIn);
    }
}
