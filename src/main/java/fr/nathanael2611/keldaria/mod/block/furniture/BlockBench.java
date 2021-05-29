package fr.nathanael2611.keldaria.mod.block.furniture;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBench extends BlockFurniture
{

    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockBench(Material materialIn)
    {
        super(materialIn, new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.7, 0.9));
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(LEFT, false)
                .withProperty(RIGHT, false));
    }



    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }


    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
        EnumFacing direction = state.getValue(FACING);
        boolean left = false;
        boolean right = false;
        if(direction == EnumFacing.NORTH)
        {
            left = world.getBlockState(pos.east()).getBlock() == this;
            right = world.getBlockState(pos.west()).getBlock() == this;
        }
        else if(direction == EnumFacing.SOUTH)
        {
            left = world.getBlockState(pos.west()).getBlock() == this;
            right = world.getBlockState(pos.east()).getBlock() == this;
        }
        else if(direction == EnumFacing.WEST)
        {
            left = world.getBlockState(pos.north()).getBlock() == this;
            right = world.getBlockState(pos.south()).getBlock() == this;
        }
        else if(direction == EnumFacing.EAST)
        {
            left = world.getBlockState(pos.south()).getBlock() == this;
            right = world.getBlockState(pos.north()).getBlock() == this;
        }
        return state
                .withProperty(LEFT, left)
                .withProperty(RIGHT, right);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,FACING, LEFT, RIGHT);
    }
}
