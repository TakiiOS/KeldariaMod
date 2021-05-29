package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityVineSupport extends TileEntity
{

    public static final long LEAVES_GROW_TIME = 1 * (60 * (60 * 1000));
    public static final long GRAPES_GROW_TIME = 24 * (60 * (60 * 1000));
    private boolean planted = false;
    private long plantedTime = -1;

    public boolean isActive()
    {
        return planted && plantedTime != -1;
    }

    public boolean isLeavesGrowed()
    {
        return System.currentTimeMillis() - plantedTime > LEAVES_GROW_TIME;
    }

    public boolean isGrapesGrowed()
    {
        return System.currentTimeMillis() - plantedTime > GRAPES_GROW_TIME;
    }

    public void plant()
    {
        this.plantedTime = System.currentTimeMillis();
        this.planted = true;
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

    public ItemStack takeGrapes(EntityPlayer player)
    {
        this.plantedTime = System.currentTimeMillis() - (GRAPES_GROW_TIME - LEAVES_GROW_TIME);
        this.planted = true;
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
        return new ItemStack(KeldariaItems.GRAPES, 4);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("PlantedTime"))
        {
            this.plantedTime = compound.getLong("PlantedTime");
        }
        if (compound.hasKey("Planted"))
        {
            this.planted = compound.getBoolean("Planted");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (plantedTime != -1)
        {
            compound.setLong("PlantedTime", this.plantedTime);
            compound.setBoolean("Planted", planted);
        }
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    public long getPlantedTime()
    {
        return plantedTime;
    }

    public boolean isPlanted()
    {
        return planted;
    }
}
