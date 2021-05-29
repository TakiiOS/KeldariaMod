package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.block.BlockBrazier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityBrazier extends TileEntity implements ITickable
{

    private int fireTicks;

    @Override
    public void update()
    {
        if(this.world.isRemote) return;
        if(this.fireTicks > 0)
        {
            if (this.getBlockState().getValue(BlockBrazier.IGNITED))
            {

                this.fireTicks--;
                if (this.fireTicks == 0)
                {
                    if (this.getBlockState().getValue(BlockBrazier.IGNITED))
                    {
                        this.setBlockState(this.getBlockState().withProperty(BlockBrazier.IGNITED, false));
                    }
                }
            } else
            {
                this.fireTicks = 0;
            }
        }
    }

    public IBlockState getBlockState()
    {
        return this.world.getBlockState(this.pos);
    }

    public void setBlockState(IBlockState state)
    {
        this.world.setBlockState(this.pos, state);
    }

    public int getFireTicks()
    {
        return fireTicks;
    }

    public void setFireTicks(int fireTicks)
    {
        this.fireTicks = fireTicks;
    }
}