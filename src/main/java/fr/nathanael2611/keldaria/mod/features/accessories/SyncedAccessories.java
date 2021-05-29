package fr.nathanael2611.keldaria.mod.features.accessories;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateAccessories;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;

public class SyncedAccessories implements INBTSerializable<NBTTagCompound>
{

    private final HashMap<String, Accessories> PLAYER_ACCESSORIES = Maps.newHashMap();

    public SyncedAccessories collect()
    {
        //PLAYER_ACCESSORIES.clear();
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player ->
        {
            Accessories weapons = new Accessories(player.getName(), ((IKeldariaPlayer) player).getInventoryAccessories(), ((IKeldariaPlayer) player).getInventoryArmor());
            PLAYER_ACCESSORIES.put(player.getName(), weapons);
        });
        return this;
    }

    public SyncedAccessories sync()
    {
        /*if(this.server)*/
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(
                    player -> KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateAccessories(), player));
        }
        return this;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.PLAYER_ACCESSORIES.forEach((playerName, weapons) ->
        {
            list.appendTag(weapons.serializeNBT());
        });
        compound.setTag("Accessories", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.PLAYER_ACCESSORIES.clear();
        NBTTagList list = nbt.getTagList("Accessories", Constants.NBT.TAG_COMPOUND);
        list.forEach(nbtBase ->
        {
            NBTTagCompound compound = (NBTTagCompound) nbtBase;
            Accessories accessories = new Accessories(compound);
            this.PLAYER_ACCESSORIES.put(accessories.getPlayerName(), accessories);
        });
    }

    public Accessories getAccessories(EntityPlayer player)
    {
        if(PLAYER_ACCESSORIES.containsKey(player.getName()) && PLAYER_ACCESSORIES.get(player.getName()) != null)
        {
            return PLAYER_ACCESSORIES.get(player.getName());
        }
        else
        {
            return PLAYER_ACCESSORIES.put(player.getName(), new Accessories(player.getName()));
        }
    }
}
