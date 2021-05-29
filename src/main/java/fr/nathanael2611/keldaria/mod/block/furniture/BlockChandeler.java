package fr.nathanael2611.keldaria.mod.block.furniture;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockChandeler extends BlockFurniture
{
    public BlockChandeler()
    {
        super(Material.IRON, null);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.35, pos.getY() + 0.3, pos.getZ() + 0.05, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.35, pos.getY() + 0.3, pos.getZ() + 0.05, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.05, pos.getY() + 0.3, pos.getZ() + 0.35, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.05, pos.getY() + 0.3, pos.getZ() + 1 - 0.35, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.05, pos.getY() + 0.3, pos.getZ() + 0.35, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.05, pos.getY() + 0.3, pos.getZ() + 1 - 0.35, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.35, pos.getY() + 0.3, pos.getZ() + 1 - 0.05, 0, 0, 0);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1 - 0.35, pos.getY() + 0.3, pos.getZ() + 1 - 0.05, 0, 0, 0);
    }
}
