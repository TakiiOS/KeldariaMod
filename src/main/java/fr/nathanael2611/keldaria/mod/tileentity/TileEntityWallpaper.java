package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.List;

public class TileEntityWallpaper extends TileEntity
{

    private String wallPaper = "";
    private HashMap<BlockPos, List<EnumFacing>> paintedFaces = Maps.newHashMap();
    private int totalSize;

    public TileEntityWallpaper()
    {
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("WallPaper", this.wallPaper);
        compound.setInteger("TotalSize", this.totalSize);
        NBTTagCompound faces = new NBTTagCompound();
        this.paintedFaces.forEach((pos, list) -> {
            NBTTagList tagList = new NBTTagList();
            for (EnumFacing enumFacing : list)
            {
                tagList.appendTag(new NBTTagInt(enumFacing.getIndex()));
            }
            faces.setTag(Helpers.blockPosToString(pos), tagList);
        });
        compound.setTag("Faces", faces);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.wallPaper = compound.getString("WallPaper");
        this.totalSize = compound.getInteger("TotalSize");
        this.paintedFaces.clear();
        NBTTagCompound faces = compound.getCompoundTag("Faces");
        for (String s : faces.getKeySet())
        {
            BlockPos pos = Helpers.parseBlockPosFromString(s);
            if(pos != BlockPos.ORIGIN)
            {
                this.paintedFaces.put(pos, Lists.newArrayList());
                NBTTagList tagList = faces.getTagList(s, Constants.NBT.TAG_INT);
                for (NBTBase nbtBase : tagList)
                {
                    if(nbtBase instanceof NBTTagInt)
                    {
                        EnumFacing facing = EnumFacing.byIndex(((NBTTagInt) nbtBase).getInt());
                        if(facing != null)
                        {
                            this.unroll(pos, facing, false);
                        }
                    }
                }
            }
        }
    }

    public void unroll(BlockPos pos, EnumFacing facing)
    {
        this.unroll(pos, facing, true);
    }
    public void unroll(BlockPos pos, EnumFacing facing, boolean save)
    {

        if (isTotallyUnrolled()) return;
        if (!this.paintedFaces.containsKey(pos))
        {
            this.paintedFaces.put(pos, Lists.newArrayList());
        }
        if(this.paintedFaces.get(pos).contains(facing)) return;
        this.paintedFaces.get(pos).add(facing);
        if(save) this.save();
    }

    public boolean isTotallyUnrolled()
    {
        return this.getUnrolledPapers() >= this.totalSize;
    }

    public int getUnrolledPapers()
    {
        int i = 0;
        for (List<EnumFacing> value : this.paintedFaces.values())
        {
            i += value.size();
        }
        return i;
    }

    public void save()
    {
        //if (this.world.isRemote) return;
        this.markDirty();
        IBlockState state = world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }


    public int getTotalSize()
    {
        return totalSize;
    }

    public String getWallPaper()
    {
        return wallPaper;
    }

    public HashMap<BlockPos, List<EnumFacing>> getPaintedFaces()
    {
        return paintedFaces;
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
