package fr.nathanael2611.keldaria.mod.season.snow.storage.impl;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SnowManager implements ISnowManager
{

    private final List<BlockPos> DIGGED_POS = Lists.newArrayList();

    @Override
    public void setSnow(BlockPos pos, boolean snow)
    {
        if(snow)
        {
            DIGGED_POS.remove(pos);
        }
        else if(!DIGGED_POS.contains(pos))
        {
            DIGGED_POS.add(pos);
        }
    }

    @Override
    public boolean isSnowy(BlockPos pos)
    {
        return !DIGGED_POS.contains(pos);
    }

    @Override
    public List<BlockPos> getDiggedPoses()
    {
        return this.DIGGED_POS;
    }

    @Override
    public void copy(ISnowManager from)
    {
        this.DIGGED_POS.clear();
        this.DIGGED_POS.addAll(from.getDiggedPoses());
    }

}
