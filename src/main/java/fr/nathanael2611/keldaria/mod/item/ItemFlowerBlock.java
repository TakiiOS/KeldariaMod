/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.item;

import fr.nathanael2611.keldaria.mod.block.BlockFlower;
import fr.nathanael2611.keldaria.mod.features.EnumFlower;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFlowerBlock extends ItemBlock
{

    private EnumFlower flower;

    public ItemFlowerBlock(BlockFlower block)
    {
        super(block);
        this.flower = block.FLOWER;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        return flower.getFormattedName();
    }

    public EnumFlower getFlower()
    {
        return flower;
    }
}
