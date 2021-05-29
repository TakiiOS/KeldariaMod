package fr.nathanael2611.keldaria.mod.server.handler;

import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.skin.CustomSkinManager;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.keldaria.mod.server.chat.voicechatbridge.KeldariaVoices;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class KeldariaSyncHandler
{

    private Keldaria keldaria;

    public KeldariaSyncHandler(Keldaria keldaria)
    {
        this.keldaria = keldaria;
    }

    private int clothesSyncTimer = 0;
    private NBTTagCompound lastSenderClothesManager = null;
    private NBTTagCompound lastSenderWeaponsManager = null;
    private NBTTagCompound lastSenderSyncedAccessories = null;

    private final HashMap<String, BlockPos> LAST_POS_VOICE = Maps.newHashMap();
    private long lastVoiceChatReload = -1;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {

        if (!KeldariaVoices.hearTroughWalls(FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0]) && this.lastVoiceChatReload == -1 || System.currentTimeMillis() - this.lastVoiceChatReload > 3000)
        {
            this.lastVoiceChatReload = System.currentTimeMillis();
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
            {
                BlockPos lastPos = this.LAST_POS_VOICE.getOrDefault(player.getName(), BlockPos.ORIGIN);
                if (!lastPos.equals(player.getPosition()))
                {
                    KeldariaVoices.reloadHearList(player);
                }
                this.LAST_POS_VOICE.put(player.getName(), player.getPosition());
            }
        }

        this.clothesSyncTimer++;
        if (this.clothesSyncTimer >= 10)
        {
            {

                this.clothesSyncTimer = 0;
                this.keldaria.getClothesManager().collect();
                this.keldaria.getWeaponsManager().collect();
                this.keldaria.getSyncedAccessories().collect();

                if (this.lastSenderClothesManager == null)
                    this.lastSenderClothesManager = keldaria.getClothesManager().serializeNBT();
                if (this.lastSenderWeaponsManager == null)
                    this.lastSenderWeaponsManager = keldaria.getWeaponsManager().serializeNBT();
                if (this.lastSenderSyncedAccessories == null)
                    this.lastSenderSyncedAccessories = keldaria.getSyncedAccessories().serializeNBT();
                NBTTagCompound clothesNBT = keldaria.getClothesManager().serializeNBT();
                if (!clothesNBT.toString().equalsIgnoreCase(this.lastSenderClothesManager.toString()))
                {
                    this.keldaria.getClothesManager().sync();
                    this.lastSenderClothesManager = clothesNBT.copy();
                }
                this.lastSenderClothesManager = clothesNBT.copy();
                {
                    NBTTagCompound weaponsNBT = this.keldaria.getWeaponsManager().serializeNBT();
                    if (!weaponsNBT.toString().equalsIgnoreCase(lastSenderWeaponsManager.toString()))
                    {
                        keldaria.getWeaponsManager().sync();
                        this.lastSenderWeaponsManager = weaponsNBT.copy();
                    }
                }
                {
                    NBTTagCompound accessoriesNBT = this.keldaria.getSyncedAccessories().serializeNBT();
                    if (!accessoriesNBT.toString().equalsIgnoreCase(this.lastSenderSyncedAccessories.toString()))
                    {
                        this.keldaria.getSyncedAccessories().sync();
                        this.lastSenderSyncedAccessories = accessoriesNBT.copy();
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(event.player.world.isRemote)
            return;
        CustomSkinManager.getInstance().updateAll();
        keldaria.getClothesManager().collect();
        keldaria.getWeaponsManager().collect();
        keldaria.getSyncedAccessories().collect();
        CommonProxy.executeAfter(() ->
        {
            keldaria.getClothesManager().sync();
            keldaria.getWeaponsManager().sync();
            keldaria.getSyncedAccessories().sync();
        }, 2000);
    }

}
