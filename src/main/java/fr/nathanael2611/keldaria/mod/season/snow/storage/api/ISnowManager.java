package fr.nathanael2611.keldaria.mod.season.snow.storage.api;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ISnowManager
{

    void setSnow(BlockPos pos, boolean snow);

    default void dig(BlockPos pos)
    {
        this.setSnow(pos, false);
    }

    default void snow(BlockPos pos)
    {
        this.setSnow(pos, true);
    }

    boolean isSnowy(BlockPos pos);

    default boolean isDigged(BlockPos pos)
    {
        return !this.isSnowy(pos);
    }

    List<BlockPos> getDiggedPoses();

    void copy(ISnowManager from);

}