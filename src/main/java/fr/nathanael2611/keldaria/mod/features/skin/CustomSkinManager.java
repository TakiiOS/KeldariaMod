package fr.nathanael2611.keldaria.mod.features.skin;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketSetSkins;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CustomSkinManager
{

    public static final String DB_NAME = Keldaria.MOD_ID + ":customskins";

    private static CustomSkinManager instance;

    public static CustomSkinManager getInstance()
    {
        if (instance == null) instance = new CustomSkinManager();
        return instance;
    }

    private DatabaseReadOnly getDB()
    {
        return Databases.getDatabase(DB_NAME);
    }

    public boolean hasSkin(EntityPlayer player)
    {
        return hasSkin(player.getName());
    }

    public boolean hasSkin(String playerName)
    {
        if (getDB().getString(playerName) == null) return false;
        Skin skin = Skin.fromString(getDB().getString(playerName));
        return (!skin.isDefault());
    }

    public Skin getSkin(EntityPlayer player)
    {
        return getSkin(player.getName());
    }

    public Skin getSkin(String playerName)
    {
        return hasSkin(playerName) ? Skin.fromString(getDB().getString(playerName)) : Skin.DEFAULT_SKIN;
    }

    public void setSkin(EntityPlayer player, Skin skin)
    {
        setSkin(player.getName(), skin);
    }

    public void setSkin(String playerName, Skin skin)
    {
        Databases.getDatabase(DB_NAME).setString(playerName, skin.toString());
        KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketSetSkins(playerName, skin.getLink()));
    }

    public void updateAll()
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketSetSkins(this));
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            compound.setString(player.getName(), getSkin(player).getLink());
        }
        return compound;
    }

}
