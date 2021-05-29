package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.block.furniture.BlockFurnitureContainer;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityCardDeck;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCardDeck extends BlockFurnitureContainer
{
    public BlockCardDeck()
    {
        super(Material.WOOD, new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.3, 0.7));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return true;
        if(playerIn.isSneaking())
        {
            Helpers.sendInRadius(pos, new TextComponentString("§6" + playerIn.getName() + " semble avoir ouvert la pioche..."), 7);
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityCardDeck)
        {
            ((TileEntityCardDeck) te).pick(playerIn);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCardDeck();
    }



}
