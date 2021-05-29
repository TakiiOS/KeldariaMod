package fr.nathanael2611.keldaria.mod.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityCustomPainting extends TileEntity
{

    private String link;
    private int width;
    private int height;

    public void setLink(String link)
    {
        this.link = link;
        save();
    }

    public void setWidth(int width)
    {
        this.width = width;
        save();
    }

    public void setHeight(int height)
    {
        this.height = height;
        save();
    }

    public String getLink()
    {
        return link;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.link = compound.hasKey("Link", Constants.NBT.TAG_STRING) ? compound.getString("Link") : "";
        this.width = compound.hasKey("Width") ? compound.getInteger("Width") : 1;
        this.height = compound.hasKey("Height") ? compound.getInteger("Height") : 1;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("Link", this.link == null ? "" : this.link);
        compound.setInteger("Width", this.width);
        compound.setInteger("Height", this.height);
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

    public void save()
    {
        this.markDirty();
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

}
