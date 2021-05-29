package fr.nathanael2611.keldaria.mod.features.lockpick;

import net.minecraft.util.math.BlockPos;

public class Lock
{
    public  boolean  isLocked;
    private BlockPos lockPos;
    private int keyid;

    public Lock(BlockPos lockPos, int keyid, boolean isLocked)
    {
        this.lockPos = lockPos;
        this.keyid = keyid;
        this.isLocked = isLocked;
    }

    public Lock(){
        this.lockPos = new BlockPos(0, 0, 0);
        this.isLocked = false;
        this.keyid = LocksHelper.randomCode();
    }

    public BlockPos getLockPos()
    {
        return lockPos;
    }

    public int getKeyid()
    {
        return keyid;
    }

}