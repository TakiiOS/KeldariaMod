package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSendWritingUpdate;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSyncPlayerWriting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;

public class ChatBubblesManager
{

    private static ChatBubblesManager INSTANCE;

    private final HashMap<String, Boolean> IS_PLAYER_WRITING = Maps.newHashMap();

    public static ChatBubblesManager getInstance()
    {
        return INSTANCE != null ? INSTANCE : (INSTANCE = new ChatBubblesManager());
    }

    public boolean isWriting(String playerName)
    {
        return this.IS_PLAYER_WRITING.getOrDefault(playerName, false);
    }

    public void setWriting(String playerName, boolean writing, boolean sync)
    {
        this.IS_PLAYER_WRITING.put(playerName, writing);
        if(sync)
        {
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(playerMP -> KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketSyncPlayerWriting(playerName, writing), playerMP));
        }
    }

    public void setWriting(String playerName, boolean writing)
    {
        this.setWriting(playerName, writing, false);
    }

    public void handleWritePacketToServer(boolean writing)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketSendWritingUpdate(writing));
    }

}
