package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCookingTable extends Block
{

    public BlockCookingTable()
    {
        super(Material.WOOD);
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        this.setHardness(1F);
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            if(EnumJob.COOK.has(playerIn))
            {
                CraftManager.openManager("cooking", Helpers.getPlayerMP(playerIn));
            }
            else
            {
                Helpers.sendPopMessage((EntityPlayerMP) playerIn, "§c ● Vous devez avoir la compétence §4" + EnumJob.COOK.getFormattedName() + "§c pour faire cela!", 1000);
            }
        }
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
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
}
