/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.handler.KeldariaClientHandler;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowStorage;
import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.server.zone.storage.ZoneStorage;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateZone implements IMessage
{

    private int x, z;
    private String zoneName;

    public PacketUpdateZone()
    {
    }

    public PacketUpdateZone(int x, int z, RPZone zoneName)
    {
        this.x = x;
        this.z = z;
        this.zoneName = zoneName.getName();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.zoneName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, this.zoneName);
    }

    public static class Handler implements IMessageHandler<PacketUpdateZone, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketUpdateZone message, MessageContext ctx)
        {
            KeldariaClientHandler.RUN_WHEN_PLAYER_IS_HERE.add(() -> {
                World world = Minecraft.getMinecraft().world;
                if(world != null)
                {
                    Chunk chunk = world.getChunk(message.x, message.z);
                    if(chunk != null && chunk.isLoaded())
                    {
                        IZoneManager manager = chunk.getCapability(ZoneStorage.CAPABILITY_ZONE, null);
                        if(manager != null)
                            manager.setZone(Zones.byName(message.zoneName));
                    }
                }
            });
            return null;
        }
    }

}
