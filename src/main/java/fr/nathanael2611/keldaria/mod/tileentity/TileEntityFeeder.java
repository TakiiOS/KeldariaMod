/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Arrays;

public class TileEntityFeeder extends TileEntity
{

    private ContentType contentType = ContentType.NONE;
    private double percent;

    public void setContentType(ContentType contentType)
    {
        if(this.contentType != contentType)
        {
            this.contentType = contentType;
            markDirty();
            IBlockState state = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, state, state, 3);

        }

    }

    public void setPercent(double percent)
    {
        this.percent = percent;
        markDirty();
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
        if(this.percent == 0)
        {
            setContentType(ContentType.NONE);
        }
    }

    public void incrementPercent(double percent)
    {
        this.setPercent(Math.min(Math.max(getPercent() + percent, 0), 100));
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public double getPercent()
    {
        return percent;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("ContentType", this.contentType.getId());
        compound.setDouble("Percent", this.percent);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.contentType = ContentType.byId(compound.getInteger("ContentType"));
        this.percent = compound.getDouble("Percent");
        if(this.percent == 0)
        {
            setContentType(ContentType.NONE);
        }
    }

    public static enum ContentType
    {
        NONE(0, "none"), WHEAT(1, "wheat"), WATER(2, "water");

        private int id;
        private String name;

        ContentType(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getId()
        {
            return id;
        }

        public static ContentType byId(int id)
        {
            return Arrays.stream(values()).filter(value -> value.id == id).findFirst().orElse(ContentType.NONE);
        }
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

}
