/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block.furniture;

import fr.nathanael2611.keldaria.mod.item.ItemPawn;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFurnitureContainer;

public class TileEntityChessPlate extends TileEntityFurnitureContainer
{

    public TileEntityChessPlate()
    {
        super(8 * 8,8, true);
        this.itemConsumer = stack -> stack.getItem() instanceof ItemPawn;
    }

}
