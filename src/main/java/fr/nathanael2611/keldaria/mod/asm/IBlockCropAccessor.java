package fr.nathanael2611.keldaria.mod.asm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockCropAccessor
{

    void growTimes(World worldIn, BlockPos pos, IBlockState state, int times);

}
