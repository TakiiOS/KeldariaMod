package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.asm.IBlockCropAccessor;
import fr.nathanael2611.keldaria.mod.features.skill.PeasantStuff;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class TileEntityFieldsAnalyzer extends TileEntity implements ITickable
{

    private final List<BlockPos> POSES = Lists.newArrayList();
    private long lastGrow = -1;

    @Override
    public void update()
    {
        if(world.isRemote) return;
        long current = System.currentTimeMillis();
        if(lastGrow == -1)
        {
            lastGrow = current;
        }
        if(current - lastGrow > PeasantStuff.GROW_INTERVAL)
        {
            this.growAll((int) ((current - lastGrow) / PeasantStuff.GROW_INTERVAL));
            lastGrow = current;
        }
    }

    private void growAll(int times)
    {
        for (int i = 0; i < this.POSES.size(); i++)
        {
            this.grow(this.POSES.get(i), times);
        }
    }

    private void grow(BlockPos pos, int times)
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockCrops)
        {
            BlockCrops block = (BlockCrops) state.getBlock();
            ((IBlockCropAccessor)block).growTimes(world, pos, state, times);
        }
        else
        {
            POSES.remove(pos);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (BlockPos pos : this.POSES)
        {
            list.appendTag(new NBTTagString(Helpers.blockPosToString(pos)));
        }
        compound.setTag("Poses", list);
        compound.setLong("LastGrow", this.lastGrow);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.POSES.clear();
        if(compound.hasKey("Poses"))
        {
            NBTTagList list = compound.getTagList("Poses", Constants.NBT.TAG_STRING);
            for (NBTBase nbtBase : list)
            {
                if(nbtBase instanceof NBTTagString)
                {
                    BlockPos pos = Helpers.parseBlockPosFromString(((NBTTagString) nbtBase).getString());
                    this.POSES.add(pos);
                }
            }
        }
        if(compound.hasKey("LastGrow"))
        {
            this.lastGrow = compound.getLong("LastGrow");
        }
    }

    public void add(BlockPos pos)
    {
        POSES.add(pos);
    }

}