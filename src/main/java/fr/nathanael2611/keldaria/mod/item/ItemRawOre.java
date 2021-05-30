package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemRawOre extends Item
{

    public static Item getRaw(Item item)
    {
        if(item instanceof ItemBlock)
        {
            Block block = ((ItemBlock) item).getBlock();
            if(block == Blocks.IRON_ORE)
            {
                return KeldariaItems.RAW_IRON;
            }
            else if(block == Blocks.GOLD_ORE)
            {
                return KeldariaItems.RAW_GOLD;
            }
        }
        return item;
    }

}
