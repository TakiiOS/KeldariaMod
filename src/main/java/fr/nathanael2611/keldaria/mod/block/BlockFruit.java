package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFruitBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFruit extends Block
{

    private final AxisAlignedBB BB;

    public BlockFruit()
    {
        super(Material.LEAVES);

        this.BB = new AxisAlignedBB(0.3, 0.65, 0.3, 0.7, 1, 0.7);

        setHardness(200F);
        this.setTickRandomly(true);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {

        return BB;
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


    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
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

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityFruitBlock();
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        this.checkViability(worldIn, pos);
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 100;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if(fromPos.equals(pos.up()))
        {
            this.checkViability(worldIn, pos);
        }
    }


    public void checkViability(World worldIn, BlockPos pos)
    {
        IBlockState up = worldIn.getBlockState(pos.up());
        if(!up.getBlock().isLeaves(up, worldIn, pos.up()))
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityFruitBlock)
            {
                TileEntityFruitBlock fruit = (TileEntityFruitBlock) te;
                EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), fruit.take());
                worldIn.spawnEntity(item);
            }
            worldIn.setBlockToAir(pos);

        }
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return true;

        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityFruitBlock)
        {
            TileEntityFruitBlock fruit = (TileEntityFruitBlock) te;
            EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), fruit.take());
            worldIn.spawnEntity(item);
        }

        return true;
    }
}
