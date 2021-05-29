package fr.nathanael2611.keldaria.mod.server.zone;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateZone;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.server.zone.storage.ZoneProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

public class ZoneHandler
{

    public static final ResourceLocation ZONE_MANAGER_LOCATION = new ResourceLocation(Keldaria.MOD_ID, "zone_manager");

    @SubscribeEvent
    public void onCapabilityAttach(AttachCapabilitiesEvent<Chunk> event)
    {
        event.addCapability(ZONE_MANAGER_LOCATION, new ZoneProvider());
    }

    @SubscribeEvent
    public void playerEnterChunk(PlayerEvent.EnteringChunk event)
    {
        if (event.getEntity() instanceof EntityPlayerMP)
        {
            Chunk chunk = event.getEntity().world.getChunk(event.getNewChunkX(), event.getNewChunkZ());

            sendChunkRadius((EntityPlayerMP) event.getEntity(), chunk, 1);
        }
    }

    @SubscribeEvent
    public void playerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            Chunk chunk = event.player.world.getChunk(event.player.getPosition());

            sendChunkRadius((EntityPlayerMP) event.player, chunk, 4);

        }
    }

    public static void sendChunkRadius(EntityPlayerMP player, Chunk center, int radius)
    {
        ChunkPos pos = center.getPos();
        for (int x = pos.x - radius; x <= pos.x + radius; x++)
        {
            for (int z = pos.z - radius; z <= pos.z + radius; z++)
            {
                Chunk c = player.world.getChunk(x, z);
                if (c != null && c.isLoaded())
                {
                    sendChunk(player, c);
                }
            }
        }
    }

    public static void sendChunks(EntityPlayerMP player, Collection<Chunk> chunks)
    {
        for (Chunk chunk : chunks)
        {
            sendChunk(player, chunk);
        }
    }

    public static void sendChunk(EntityPlayerMP player, Chunk chunk)
    {
        sendChunk(player, chunk, chunk.getPos().x, chunk.getPos().z);
    }

    public static void sendChunk(EntityPlayerMP player, Chunk c, int posX, int posZ)
    {
        if (c != null && c.isLoaded())
        {
            RPZone zone = Zones.getZone(c);
            KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateZone(posX, posZ, zone), player);
        }
    }

}
