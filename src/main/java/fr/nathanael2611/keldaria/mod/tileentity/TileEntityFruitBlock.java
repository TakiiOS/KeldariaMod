/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.List;

public class TileEntityFruitBlock extends TileEntity
{

    private long lastHarvest = System.currentTimeMillis();
    private long growTime;
    private long maturingTime;
    private ItemStack maturingStack = ItemStack.EMPTY;
    private ItemStack matureStack = ItemStack.EMPTY;
    private List<Season> infertileSeasons = Lists.newArrayList();

    public boolean isGrowed()
    {
        return System.currentTimeMillis() > this.lastHarvest + growTime;
    }

    public boolean isMatured()
    {
        return System.currentTimeMillis() > this.lastHarvest + growTime + maturingTime;
    }

    public double getMaturingPercent()
    {
        if(!isGrowed()) return 0;
        if(isMatured()) return 100;
        long timeMatured = growTime + maturingTime;
        return Helpers.getPercent(System.currentTimeMillis() - lastHarvest, timeMatured);
    }

    public ItemStack take()
    {
        if(isMatured())
        {
            this.lastHarvest = System.currentTimeMillis();
            this.save();
            return this.matureStack.copy();
        }
        if(isGrowed())
        {
            if(this.getMaturingPercent() > 50)
            {
                this.lastHarvest = System.currentTimeMillis();
                this.save();
                return this.maturingStack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public void setTimes(long growTime, long maturingTime)
    {
        this.growTime = growTime;
        this.maturingTime = maturingTime;
        this.save();
    }

    public void setMaturingStack(ItemStack maturingStack)
    {
        this.maturingStack = maturingStack;
        this.save();
    }

    public ItemStack getMatureStack()
    {
        return matureStack.copy();
    }

    public void setMatureStack(ItemStack matureStack)
    {
        this.matureStack = matureStack;
        this.save();
    }

    public ItemStack getMaturingStack()
    {
        return maturingStack.copy();

    }

    public void setLastHarvest(long lastHarvest)
    {
        this.lastHarvest = lastHarvest;
        this.save();
    }

    public long getGrowTime()
    {
        return growTime;
    }

    public long getMaturingTime()
    {
        return maturingTime;
    }

    public void setInfertileSeasons(List<Season> nonFertileSeasons)
    {
        this.infertileSeasons = nonFertileSeasons;
        this.save();
    }

    public List<Season> getInfertileSeasons()
    {
        return infertileSeasons;
    }

    public boolean isFertileIn(Season season)
    {
        return !this.infertileSeasons.contains(season);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setLong("LastHarvest", this.lastHarvest);
        compound.setLong("GrowTime", this.growTime);
        compound.setLong("MaturingTime", this.maturingTime);
        compound.setTag("MaturingStack", this.maturingStack.serializeNBT());
        compound.setTag("MatureStack", this.matureStack.serializeNBT());
        NBTTagList seasons = new NBTTagList();
        for (Season infertileSeason : this.infertileSeasons)
        {
            seasons.appendTag(new NBTTagString(infertileSeason.getName()));
        }
        compound.setTag("InfertileSeasons", seasons);
        return compound;
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.lastHarvest = compound.getLong("LastHarvest");
        this.growTime = compound.getLong("GrowTime");
        this.maturingTime = compound.getLong("MaturingTime");
        this.maturingStack = new ItemStack(compound.getCompoundTag("MaturingStack"));
        this.matureStack = new ItemStack(compound.getCompoundTag("MatureStack"));
        this.infertileSeasons = Lists.newArrayList();
        NBTTagList seasons = compound.getTagList("InfertileSeasons", Constants.NBT.TAG_STRING);
        for (NBTBase season : seasons)
        {
            if(season instanceof NBTTagString)
            {
                Season s = Season.byName(((NBTTagString) season).getString());
                if (s != null)
                {
                    this.infertileSeasons.add(s);
                }
            }
        }
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

    public void save()
    {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

}
