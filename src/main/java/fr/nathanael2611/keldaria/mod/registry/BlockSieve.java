package fr.nathanael2611.keldaria.mod.registry;

import fr.nathanael2611.keldaria.mod.tileentity.TileEntitySieve;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSieve extends Block
{

    public static final PropertyBool SALTED = PropertyBool.create("salted");

    private AxisAlignedBB bb = new AxisAlignedBB(0, 0, 0, 1, 0.3, 1);

    public BlockSieve()
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SALTED, Boolean.FALSE));

    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(SALTED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return meta == 1 ? this.getDefaultState().withProperty(SALTED, true) : this.getDefaultState();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, SALTED);
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return bb != null ? bb : super.getBoundingBox(state, source, pos);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return bb != null ? bb : super.getCollisionBoundingBox(blockState, worldIn, pos);
    }


    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return true;

        TileEntity te = worldIn.getTileEntity(pos);

        if(te instanceof TileEntitySieve)
        {
            TileEntitySieve sieve = (TileEntitySieve) te;
            if(!sieve.isSalted())
            {
                return false;
            }
            if(playerIn.getHeldItemMainhand().getItem() == KeldariaItems.WOODEN_POT)
            {
                playerIn.getHeldItemMainhand().shrink(1);
                playerIn.addItemStackToInventory(sieve.get(playerIn));
            }
            else
            {
                playerIn.sendMessage(new TextComponentString("§cVeuillez avoir un pot en bois dans la main pour récolter le sel."));
            }
        }
        return true;
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
        return new TileEntitySieve();
    }
}
