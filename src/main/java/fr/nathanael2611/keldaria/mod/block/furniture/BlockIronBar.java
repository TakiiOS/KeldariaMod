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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockIronBar extends BlockFurniture
{

    public static final PropertyBool BACK = PropertyBool.create("back");
    public static final PropertyBool FORWARD = PropertyBool.create("forward");
    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public BlockIronBar(Material materialIn)
    {
        super(materialIn, new AxisAlignedBB(0.3, 0.3, 0.3, 1 - 0.3, 1 - 0.3, 1 - 0.3));
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(BlockIronBar.BACK, false)
                .withProperty(BlockIronBar.FORWARD, false)
                .withProperty(BlockIronBar.LEFT, false)
                .withProperty(BlockIronBar.RIGHT, false)
                .withProperty(BlockIronBar.UP, false)
                .withProperty(BlockIronBar.DOWN, false));
    }

    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
        final boolean back = world.getBlockState(pos.south()).getBlock() == this;
        final boolean forward = world.getBlockState(pos.north()).getBlock() == this;
        final boolean left = world.getBlockState(pos.west()).getBlock() == this;
        final boolean right = world.getBlockState(pos.east()).getBlock() == this;
        final boolean up = world.getBlockState(pos.up()).getBlock() == this;
        final boolean down= world.getBlockState(pos.down()).getBlock() == this;
        return state
                .withProperty(BlockIronBar.BACK, back)
                .withProperty(BlockIronBar.FORWARD, forward)
                .withProperty(BlockIronBar.LEFT, left)
                .withProperty(BlockIronBar.RIGHT, right)
                .withProperty(BlockIronBar.UP, up)
                .withProperty(BlockIronBar.DOWN, down);
    }

    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockIronBar.BACK, BlockIronBar.FORWARD, BlockIronBar.LEFT, BlockIronBar.RIGHT, BlockIronBar.UP, BlockIronBar.DOWN);
    }


}
