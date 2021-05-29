package fr.nathanael2611.keldaria.mod.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityPortcullis extends TileEntity implements ITickable
{

    public int x1, y1, z1, x2, y2, z2;
    private BlockPos b1;
    private BlockPos b2;
    private int timer = 0;

    @Override
    public void update()
    {
        if (x1 + y1 + z1 + x2 + y2 + z2 > 0)
        {
            timer++;
            if (timer > (needUp() ? 30 : 15))
            {
                timer = 0;
                BlockPos pos1 = getPos1();
                BlockPos pos2 = getPos2();
                this.b1 = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
                this.b2 = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
                if ((b2.getX() - b1.getX() + 1) * (b2.getY() - b1.getY() + 1) * (b2.getZ() - b1.getZ() + 1) <= 1000) move();
            }
        }
    }

    private BlockPos getPos1()
    {
        return new BlockPos(x1, y1, z1);
    }

    private BlockPos getPos2()
    {
        return new BlockPos(x2, y2, z2);
    }

    private boolean needUp()
    {
        return getWorld().getRedstonePowerFromNeighbors(getPos()) > 0;
    }

    private void move()
    {
        if(can())
            if(needUp())
                for(int x = b1.getX(); x <= b2.getX(); x ++)
                    for (int z = b1.getZ(); z <= b2.getZ(); z++)
                        for(int y = b2.getY() -1 ; y >= b1.getY(); y --)
                        {
                            IBlockState state = world.getBlockState(new BlockPos(x, y, z));
                            world.setBlockToAir(new BlockPos(x, y, z));
                            world.setBlockState(new BlockPos(x, y + 1, z), state);
                        }
            else for(int x = b1.getX(); x <= b2.getX(); x ++)
                for (int z = b1.getZ(); z <= b2.getZ(); z++)
                    for(int y = b1.getY() + 1; y <= b2.getY(); y ++)
                    {
                        IBlockState state = world.getBlockState(new BlockPos(x, y, z));
                        world.setBlockToAir(new BlockPos(x, y, z));
                        world.setBlockState(new BlockPos(x, y - 1, z), state);
                    }
    }

    public boolean can()
    {
        int y = (needUp() ? b2 : b1).getY();
        for(int x = b1.getX(); x <= b2.getX(); x ++)
            for(int z = b1.getZ(); z <= b2.getZ(); z ++)
                if(world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.AIR) return false;
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.x1 = readIfHave("x1", compound);
        this.x2 = readIfHave("x2", compound);
        this.y1 = readIfHave("y1", compound);
        this.y2 = readIfHave("y2", compound);
        this.z1 = readIfHave("z1", compound);
        this.z2 = readIfHave("z2", compound);
    }

    private int readIfHave(String val, NBTTagCompound compound)
    {
        return compound.hasKey(val) ? compound.getInteger(val) : 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("x1", this.x1);
        compound.setInteger("x2", this.x2);
        compound.setInteger("y1", this.y1);
        compound.setInteger("y2", this.y2);
        compound.setInteger("z1", this.z1);
        compound.setInteger("z2", this.z2);
        return compound;
    }
}