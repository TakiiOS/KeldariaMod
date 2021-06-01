package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.IBlockCropAccessor;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockCrops.class)
public abstract class MixinBlockCrops implements IBlockCropAccessor
{

    @Shadow protected abstract int getAge(IBlockState state);

    @Shadow public abstract int getMaxAge();

    @Shadow public abstract IBlockState withAge(int age);

    /**
     * @author
     */
    @Overwrite
    public void grow(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = this.getAge(state) + 1;
        int j = this.getMaxAge();

        if (i > j)
        {
            i = j;
        }

        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    @Override
    public void growTimes(World worldIn, BlockPos pos, IBlockState state, int times)
    {
        int i = this.getAge(state) + times;
        int j = this.getMaxAge();

        if (i > j)
        {
            i = j;
        }

        worldIn.setBlockState(pos, this.withAge(i), 2);
    }
}
