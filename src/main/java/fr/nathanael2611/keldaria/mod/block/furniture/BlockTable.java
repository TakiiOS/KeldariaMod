/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block.furniture;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTable extends BlockFurniture
{

    public static final PropertyBool BACK = PropertyBool.create("back");
    public static final PropertyBool FORWARD = PropertyBool.create("forward");
    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");

    public BlockTable(Material materialIn)
    {
        super(materialIn, null);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(BlockTable.BACK, false)
                .withProperty(BlockTable.FORWARD, false)
                .withProperty(BlockTable.LEFT, false)
                .withProperty(BlockTable.RIGHT, false));
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
        final boolean back = world.getBlockState(pos.south()).getBlock() == this;
        final boolean forward = world.getBlockState(pos.north()).getBlock() == this;
        final boolean left = world.getBlockState(pos.west()).getBlock() == this;
        final boolean right = world.getBlockState(pos.east()).getBlock() == this;
        return state
                .withProperty(BlockTable.BACK, back)
                .withProperty(BlockTable.FORWARD, forward)
                .withProperty(BlockTable.LEFT, left)
                .withProperty(BlockTable.RIGHT, right);
    }

    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockTable.BACK, BlockTable.FORWARD, BlockTable.LEFT, BlockTable.RIGHT);
    }


}
