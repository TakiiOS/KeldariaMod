package fr.nathanael2611.keldaria.mod.block.furniture;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFurnitureContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWallCabinet extends BlockFurnitureContainer
{

    public static final AxisAlignedBB AXIS_NORTH = new AxisAlignedBB(1, 1, 1, 0, 0, 0.3);
    public static final AxisAlignedBB AXIS_EAST = new AxisAlignedBB(0, 0, 0, 0.7, 1, 1);
    public static final AxisAlignedBB AXIS_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, 0.7);
    public static final AxisAlignedBB AXIS_WEST = new AxisAlignedBB(1, 1, 1, 0.3, 0, 0);

    public BlockWallCabinet(Material materialIn)
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

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFurnitureContainer(9*2, 9, false);
    }
}
