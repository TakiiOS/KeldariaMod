package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCookingFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCookingFurnace extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockCookingFurnace() {
        super(Material.ROCK);
        setCreativeTab(KeldariaTabs.KELDARIA);
        setHardness(2);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityCookingFurnace)
            {
                playerIn.openGui(Keldaria.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCookingFurnace();
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

    /**
     * Convert the BlockState into the correct metadata value
     */
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityCookingFurnace) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityCookingFurnace) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
        if(worldIn.getTileEntity(pos) instanceof TileEntityCookingFurnace)
        {
            TileEntityCookingFurnace te = (TileEntityCookingFurnace) worldIn.getTileEntity(pos);
            if(te != null)
            {
                if (te.isBurning()) {
                    for (int k = 0; k < 6; k++)
                    {
                        for (int i = 0; i < 7; i++)
                        {
                            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.3 + (0.1 * k), pos.getY() + 0.23, pos.getZ() + 0.2 + (0.1 * i), 0, 0, 0, 10, 10, 100, 1000);
                        }
                    }
                }
            }
        }
    }
}
