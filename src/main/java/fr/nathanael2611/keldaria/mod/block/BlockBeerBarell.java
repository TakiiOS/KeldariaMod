/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityBeerBarrel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBeerBarell extends BlockContainer
{

    public BlockBeerBarell()
    {
        super(Material.WOOD);
        setCreativeTab(KeldariaTabs.KELDARIA);
        setHardness(2f);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            playerIn.openGui(Keldaria.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityBeerBarrel();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityBeerBarrel)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBeerBarrel) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
