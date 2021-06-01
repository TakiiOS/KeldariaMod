package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFieldsAnalyzer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockFieldsAnalyzer extends Block
{
    public BlockFieldsAnalyzer()
    {
        super(Material.BARRIER);
        this.setBlockUnbreakable();
        this.setResistance(6000001.0F);
        this.disableStats();
        this.translucent = true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFieldsAnalyzer();
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * @deprecated call via {@link IBlockState#getAmbientOcclusionLightValue()} whenever possible.
     * Implementing/overriding is fine.
     */
    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state)
    {
        return 1.0F;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }

    public static BlockPos getPosInChunk(Chunk chunk)
    {
        ChunkPos pos = chunk.getPos();
        int x = pos.x << 4;
        int y = 255;
        int z = pos.z << 4;
        return new BlockPos(x, y, z);
    }

    public static void place(Chunk chunk)
    {
        BlockPos pos = getPosInChunk(chunk);
        chunk.getWorld().setBlockState(pos, KeldariaBlocks.FIELDS_ANALYZER.getDefaultState(), 3);
    }

    public static boolean has(Chunk chunk)
    {
        return chunk.getWorld().getBlockState(getPosInChunk(chunk)).getBlock() instanceof BlockFieldsAnalyzer;
    }

    public static void addField(Chunk chunk, BlockPos field)
    {
        if(!has(chunk))
        {
            place(chunk);
        }
        BlockPos pos = getPosInChunk(chunk);
        TileEntity te = chunk.getWorld().getTileEntity(pos);
        if(te instanceof TileEntityFieldsAnalyzer)
        {
            TileEntityFieldsAnalyzer analyzer = (TileEntityFieldsAnalyzer) te;
            analyzer.add(field);
        }
    }

}