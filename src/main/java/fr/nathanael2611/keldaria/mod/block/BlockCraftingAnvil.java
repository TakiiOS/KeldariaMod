/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCraftingAnvil extends Block
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockCraftingAnvil()
    {
        super(Material.ANVIL);
        setCreativeTab(KeldariaTabs.KELDARIA);
        setHardness(3f);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            /*$if(!playerIn.capabilities.isCreativeMode)
            {
                if(!Skills.getSkills(playerIn).hasSkill(EnumSkills.BLACKSMITH))
                {
                    Helpers.sendPopMessage(playerIn, "§cVous devez avoir la compétence §4" + EnumSkills.BLACKSMITH.getName() + "§c pour faire cela.", 1200);
                    return false;
                }
            }
            playerIn.openGui(Keldaria.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());*/
            if(playerIn.getHeldItemMainhand().getItem() == KeldariaItems.IRON_FORGE_HAMMER)
            {
                CraftManager.openManager("blacksmith", Helpers.getPlayerMP(playerIn));
            }
            else
            {
                Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§cVeuillez utiliser l'enclume avec un marteau de forgeron!", 1000);
            }
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



    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

}
