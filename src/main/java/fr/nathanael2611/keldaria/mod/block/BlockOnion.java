/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.Item;

/**
 * The onion plant class
 *
 * @author Nathanael2611
 */
public class BlockOnion extends BlockCrops {

    public static final int MAX_AGE = 4;

    
    public PropertyInteger getBaseAgeProperty() {
        return PropertyInteger.create("age", 0, MAX_AGE);
    }

    @Override
    protected Item getSeed() {
        return KeldariaItems.ONION;
    }

    @Override
    protected Item getCrop() {
        return KeldariaItems.ONION;
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return getBaseAgeProperty();
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {getAgeProperty()});
    }
}
