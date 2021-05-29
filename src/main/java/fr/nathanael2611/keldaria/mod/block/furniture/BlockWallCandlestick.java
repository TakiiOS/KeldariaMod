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

public class BlockWallCandlestick extends BlockRotatableFurniture
{

    public static final AxisAlignedBB AXIS_NORTH = new AxisAlignedBB(1, 1, 1, 0, 0, 0.7);
    public static final AxisAlignedBB AXIS_EAST = new AxisAlignedBB(0, 0, 0, 0.3, 1, 1);
    public static final AxisAlignedBB AXIS_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, 0.3);
    public static final AxisAlignedBB AXIS_WEST = new AxisAlignedBB(1, 1, 1, 0.7, 0, 0);

    public BlockWallCandlestick(Material materialIn)
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
        if(facing == EnumFacing.SOUTH)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.20, pos.getY() + 0.85, pos.getZ() + 0.3, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.20, pos.getY() + 0.85, pos.getZ() + 0.3, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.38, 0, 0, 0);
        }
        else if(facing == EnumFacing.NORTH)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.20, pos.getY() + 0.85, pos.getZ() + 1 - 0.3, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.20, pos.getY() + 0.85, pos.getZ() + 1 - 0.3, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 1 - 0.38, 0, 0, 0);
        }
        else if(facing == EnumFacing.EAST)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.30, pos.getY() + 0.85, pos.getZ() + 0.2, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.3, pos.getY() + 0.85, pos.getZ() + 1 - 0.2, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.38, pos.getY() + 0.9, pos.getZ() + 0.5, 0, 0, 0);
        }
        else if(facing == EnumFacing.WEST)
        {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.30, pos.getY() + 0.85, pos.getZ() + 0.2, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() +1 - 0.3, pos.getY() + 0.85, pos.getZ() + 1 - 0.2, 0, 0, 0);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.38, pos.getY() + 0.9, pos.getZ() + 0.5, 0, 0, 0);
        }
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }
}
