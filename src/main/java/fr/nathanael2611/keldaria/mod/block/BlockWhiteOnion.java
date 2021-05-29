package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.Item;

/**
 * A white onion class
 *
 * @author Nathanael2611
 */
public class BlockWhiteOnion extends BlockOnion {

    public static final int MAX_AGE = 2;

    @Override
    protected Item getSeed() {
        return KeldariaItems.WHITE_ONION;
    }

    @Override
    protected Item getCrop() {
        return KeldariaItems.WHITE_ONION;
    }

    @Override
    public PropertyInteger getBaseAgeProperty() {
        return PropertyInteger.create("age", 0, MAX_AGE);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }
}