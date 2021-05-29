package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

public class BlockRefinedLog extends BlockLog
{

    public BlockRefinedLog()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        BlockLog.EnumAxis axis = state.getValue(LOG_AXIS);
        switch (axis)
        {
            case Y: return 0;
            case X: return 1;
            case Z: return 2;
            default: return 0;
        }
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 1:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 2:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }


    public static Block refine(IBlockState state)
    {
        Block block = state.getBlock();
        if(block instanceof BlockLog)
        {
            BlockPlanks.EnumType planks = state.getValue(block instanceof BlockNewLog ? BlockNewLog.VARIANT : BlockOldLog.VARIANT);
            switch (planks)
            {
                case SPRUCE: return KeldariaBlocks.SPRUCE_REFINED_LOG;
                case OAK: return KeldariaBlocks.OAK_REFINED_LOG;
                case DARK_OAK: return KeldariaBlocks.DARK_OAK_REFINED_LOG;
                case BIRCH: return KeldariaBlocks.BIRCH_REFINED_LOG;
                case ACACIA: return KeldariaBlocks.ACACIA_REFINED_LOG;
                case JUNGLE: return KeldariaBlocks.JUNGLE_REFINED_LOG;
            }
        }
        return Blocks.AIR;
    }

    public static Item bit(IBlockState state)
    {
        Block block = state.getBlock();
        if(block instanceof BlockLog)
        {
            BlockPlanks.EnumType planks = state.getValue(block instanceof BlockNewLog ? BlockNewLog.VARIANT : BlockOldLog.VARIANT);
            switch (planks)
            {
                case SPRUCE: return KeldariaItems.SPRUCE_BIT;
                case OAK: return KeldariaItems.OAK_BIT;
                case DARK_OAK: return KeldariaItems.DARK_OAK_BIT;
                case BIRCH: return KeldariaItems.BIRCH_BIT;
                case ACACIA: return KeldariaItems.ACACIA_BIT;
                case JUNGLE: return KeldariaItems.JUNGLE_BIT;
            }
        }
        return Items.AIR;
    }

}
