package fr.nathanael2611.keldaria.mod.block;

import fr.nathanael2611.keldaria.mod.item.ItemIgnitedArrow;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.registry.KeldariaTabs;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityBrazier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Date;

public class BlockBrazier extends Block
{

    public static final PropertyBool IGNITED = PropertyBool.create("ignited");

    public BlockBrazier()
    {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(IGNITED, Boolean.FALSE));
        this.setCreativeTab(KeldariaTabs.KELDARIA);
        this.setHardness(3F);

    }


    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, IGNITED);
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
     */
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

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return true;
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if(state.getValue(IGNITED))
        {

            if(heldItem.getItem() == Items.ARROW)
            {
                heldItem.shrink(1);
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 0.4F + 0.8F);
                ItemStack ignitedArrow = new ItemStack(KeldariaItems.IGNITED_ARROW);
                ItemIgnitedArrow.setIgnitDate(ignitedArrow, new Date().getTime());
                playerIn.addItemStackToInventory(ignitedArrow);
            }
        }
        else
        {
            if(heldItem.getItem() == Items.FLINT_AND_STEEL)
            {
                heldItem.damageItem(1, playerIn);
                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 100 + 0.8F);
                state = state.withProperty(IGNITED, true);
                worldIn.setBlockState(pos, state);
                ((TileEntityBrazier) (worldIn.getTileEntity(pos))).setFireTicks((60 * 5) * 20);
            }
        }
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(IGNITED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return meta == 1 ? this.getDefaultState().withProperty(IGNITED, true) : this.getDefaultState();
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
        return new TileEntityBrazier();
    }
}
