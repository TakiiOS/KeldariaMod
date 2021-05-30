/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.features.EnumFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFloweringPot extends TileEntity
{

    private EnumFlower flowerSeed = null;

    private long flowerPlantedTime = -1;


    public boolean isActive()
    {
        return flowerSeed != null && flowerPlantedTime != -1;
    }

    public boolean isGrowed()
    {
        return System.currentTimeMillis() - flowerPlantedTime > flowerSeed.getGrowTime();
    }

    public void plant(EnumFlower flower)
    {
        this.flowerPlantedTime = System.currentTimeMillis();
        this.flowerSeed = flower;
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if(compound.hasKey("FlowerPlantedTime"))
        {
            this.flowerPlantedTime = compound.getLong("FlowerPlantedTime");
        }
        if(compound.hasKey("Flower"))
        {
            this.flowerSeed = EnumFlower.byName(compound.getString("Flower"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if(flowerPlantedTime != -1)
        {
            compound.setLong("FlowerPlantedTime", this.flowerPlantedTime);
            if(flowerSeed != null)
            {
                compound.setString("Flower", this.flowerSeed.getName());
            }
        }
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public EnumFlower getFlowerSeed()
    {
        return flowerSeed;
    }

    public long getFlowerPlantedTime()
    {
        return flowerPlantedTime;
    }
}
