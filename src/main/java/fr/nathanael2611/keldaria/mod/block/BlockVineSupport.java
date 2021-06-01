/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityVineSupport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVineSupport extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockVineSupport() {
        super(Material.WOOD);
        setCreativeTab(KeldariaTabs.KELDARIA);
        setHardness(2);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityVineSupport)
        {
            TileEntityVineSupport vineSupport = (TileEntityVineSupport) te;
            if(vineSupport.isActive() && vineSupport.isGrapesGrowed())
            {
                EntityItem item = new EntityItem(worldIn);
                ItemStack stack = vineSupport.takeGrapes(playerIn);
                ExpiredFoods.create(stack);
                item.setItem(stack);
                item.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                worldIn.spawnEntity(item);
            }
            else if(!vineSupport.isActive() && playerIn.getHeldItem(hand).getItem() == KeldariaItems.GRAPES)
            {
                vineSupport.plant();
                playerIn.getHeldItem(hand).shrink(1);
            }
        }
        return true;
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityVineSupport();
    }
}
