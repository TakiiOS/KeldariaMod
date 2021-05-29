package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.registry.BlockSieve;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TileEntitySieve extends TileEntity implements ITickable
{

    private long lastCount = -1;
    private int water = 0;
    private long lastGet = -1;
    private long time = -1;

    public TileEntitySieve()
    {
    }

    public boolean isSalted()
    {
        if(water <= 0 || lastCount == -1 || lastGet == -1 || time == -1) return false;
        return System.currentTimeMillis() - lastGet > time;
    }

    public ItemStack get(EntityPlayer player)
    {
        if(player.getHeldItemMainhand().getItem() == KeldariaItems.WOODEN_POT)
        {
            this.lastGet = -1;
            this.time = -1;
            return new ItemStack(KeldariaItems.SALT_WOODEN_POT);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void update()
    {
        if(world.isRemote) return;
        if(world.getBiome(pos) == Biomes.OCEAN || world.getBiome(pos) == Biomes.DEEP_OCEAN)
        {
            long current = System.currentTimeMillis();
            if(lastCount == -1 || current - lastCount > 5000)
            {
                water = countWater();
                lastCount = current;
            }
            if(water > 0)
            {
                if(this.lastGet == -1)
                {
                    this.lastGet = current;
                }

                if(this.time == -1)
                {
                    this.time = (1000*60) * 60 * 4;
                    this.time  *= (1 - (0.03 * water));
                }
            }
            IBlockState state = world.getBlockState(this.pos);
            boolean salted = state.getValue(BlockSieve.SALTED);
            if(isSalted())
            {
                if(!salted)
                {
                    world.setBlockState(this.pos, state.withProperty(BlockSieve.SALTED, true), 3);
               /* NBTTagCompound comp = new NBTTagCompound();
                writeToNBT(comp);
                world.getTileEntity(this.pos).readFromNBT(comp);*/
                }
            }
            else
            {
                if(salted)
                {
                    world.setBlockState(this.pos, state.withProperty(BlockSieve.SALTED, false), 3);
               /* world.setTileEntity(this.pos, this);
                NBTTagCompound comp = new NBTTagCompound();
                writeToNBT(comp);
                world.getTileEntity(this.pos).readFromNBT(comp);*/

                }
            }
        }
    }

    public int countWater()
    {
        int n = 0;
        BlockPos pos = getPos().north().down();
        for(int y = pos.getY(); y > 0; y --)
        {
            BlockPos a = new BlockPos(pos.getX(), y, pos.getZ());
            if(world.getBlockState(a).getBlock() == Blocks.WATER)
            {
                n++;
            }
            else break;
        }
        return n;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setLong("lastCount", this.lastCount);
        compound.setLong("lastGet", this.lastGet);
        compound.setLong("time", this.time);
        compound.setInteger("water", this.water);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.lastCount = compound.getLong("lastCount");
        this.lastGet = compound.getLong("lastGet");
        this.time = compound.getLong("time");
        this.water = compound.getInteger("water");
    }
}
