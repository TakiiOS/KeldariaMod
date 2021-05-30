/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.features.SharpeningHelpers;
import fr.nathanael2611.keldaria.mod.registry.KeldariaBlocks;
import fr.nathanael2611.keldaria.mod.registry.KeldariaSounds;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSharpeningStone extends Block
{

    public static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.175D, 0.625D, 0.3D, 0.805D);

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, PILLAR_AABB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return PILLAR_AABB;
    }

    public BlockSharpeningStone()
    {
        super(Material.ROCK);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        this.setHardness(1F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(KeldariaBlocks.SHARPENING_STONE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if(SharpeningHelpers.canBeSharped(heldItem))
        {
            playerIn.playSound(KeldariaSounds.SHARPENING, 100F, 100);
            SharpeningHelpers.incrementSharpness(heldItem, Helpers.RANDOM.nextInt(3), playerIn);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
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

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

}

