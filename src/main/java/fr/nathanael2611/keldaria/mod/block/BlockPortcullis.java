package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityPortcullis;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPortcullis extends Block
{
    public BlockPortcullis()
    {
        super(Material.ROCK);
        setHardness(100);
        setCreativeTab(KeldariaTabs.KELDARIA);
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
        return new TileEntityPortcullis();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return true;
        if(hand != EnumHand.MAIN_HAND) return true;
        Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§cBlock de herse, réservé au staff. Demandez à un staff de le configurer si la herse est cassée.", 2000);
        return true;
    }
}