/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block.furniture;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockWallLantern extends BlockRotatableFurniture
{

    public static final AxisAlignedBB AXIS_NORTH = new AxisAlignedBB(1, 1, 1, 0, 0, 0.7);
    public static final AxisAlignedBB AXIS_EAST = new AxisAlignedBB(0, 0, 0, 0.3, 1, 1);
    public static final AxisAlignedBB AXIS_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, 0.3);
    public static final AxisAlignedBB AXIS_WEST = new AxisAlignedBB(1, 1, 1, 0.7, 0, 0);

    public BlockWallLantern(Material materialIn)
    {
        super(materialIn, null);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return getBoundingBox(state);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return getBoundingBox(blockState);
    }

    public static AxisAlignedBB getBoundingBox(IBlockState state)
    {
        EnumFacing facing = state.getValue(FACING);
        if(facing == EnumFacing.SOUTH) return AXIS_SOUTH;
        else if(facing == EnumFacing.NORTH) return AXIS_NORTH;
        else if(facing == EnumFacing.EAST) return AXIS_EAST;
        else return AXIS_WEST;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing facing = stateIn.getValue(FACING);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);

        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
