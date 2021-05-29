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
