package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.item.ItemFlowerBlock;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFloweringPot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFloweringPot extends Block
{
    public BlockFloweringPot(Material materialIn)
    {
        super(materialIn);
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
        return new TileEntityFloweringPot();
    }


    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
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
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }


    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) return true;
        ItemStack stack = playerIn.getHeldItem(hand);

        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityFloweringPot)
        {
            TileEntityFloweringPot pot = (TileEntityFloweringPot) te;

            if (stack.getItem() instanceof ItemFlowerBlock)
            {
                ItemFlowerBlock flowerBlock = (ItemFlowerBlock) stack.getItem();
                if (!pot.isActive())
                {
                    pot.plant(flowerBlock.getFlower());
                    ((EntityPlayerMP) playerIn).connection.processChatMessage(new CPacketChatMessage("*plante une " + flowerBlock.getFlower().getName() + " dans le pot."));
                }
            } else
            {
                if (pot.isGrowed())
                {
                    pot.plant(EnumJob.PEASANT.has(playerIn) ? pot.getFlowerSeed() : null);
                    worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(pot.getFlowerSeed().getItem())));
                    ((EntityPlayerMP) playerIn).connection.processChatMessage(new CPacketChatMessage("*cueille une " + pot.getFlowerSeed().getName() + " depuis le pot, laissant la racine, afin qu'une autre fleur puisse pousser.."));
                }
            }

        }
        return true;
    }
}
