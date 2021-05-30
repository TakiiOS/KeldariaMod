/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.armoposes;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSetArms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;

public class ArmPoses implements INBTSerializable<NBTTagCompound>
{

    private final HashMap<String, Arms> PLAYERS = Maps.newHashMap();

    public void setPose(EntityPlayer player, Arms arms)
    {
        PLAYERS.put(player.getName(), arms);
        this.sync();
    }

    public boolean hasPose(EntityPlayer player)
    {
        return PLAYERS.containsKey(player.getName());
    }

    public Arms getPose(EntityPlayer player)
    {
        return PLAYERS.getOrDefault(player.getName(), new Arms());
    }

    public void resetPose(EntityPlayer player)
    {
        PLAYERS.remove(player.getName());
        this.sync();
    }

    public void sync()
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketSetArms());
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.PLAYERS.forEach((playerName, arms) ->
        {
            NBTTagCompound nbt = arms.serializeNBT();
            nbt.setString("OwnerName", playerName);
            list.appendTag(nbt);
        });
        compound.setTag("Players", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.PLAYERS.clear();
        NBTTagList list = nbt.getTagList("Players", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            NBTTagCompound compound = (NBTTagCompound) nbtBase;
            Arms arms = new Arms(compound);
            this.PLAYERS.put(compound.getString("OwnerName"), arms);
        });
    }
}
