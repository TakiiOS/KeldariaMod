/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.network.toclient;

import fr.nathanael2611.keldaria.mod.client.handler.KeldariaClientHandler;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowProvider;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowStorage;
import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.season.snow.storage.impl.SnowManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUpdateSnowManager implements IMessage
{

    public static final int MODE_CHUNK = 0;
    public static final int MODE_BLOCK = 1;

    private int syncMode;
    private ISnowManager snowManager;
    private ChunkPos chunkPos;
    private BlockPos blockPos;
    private boolean snowy;

    public PacketUpdateSnowManager(Chunk chunk)
    {
        this.syncMode = MODE_CHUNK;
        this.snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
        if(this.snowManager == null)
        {
            this.snowManager = new SnowManager();
        }
        this.chunkPos = chunk.getPos();
    }

    public PacketUpdateSnowManager(Chunk chunk, BlockPos pos)
    {
        this.syncMode = MODE_BLOCK;
        this.snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
        if(this.snowManager == null)
        {
            this.snowManager = new SnowManager();
        }
        this.blockPos = pos;
        this.snowy = this.snowManager.isSnowy(pos);
    }

    public PacketUpdateSnowManager()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.syncMode = buf.readInt();
        if(this.syncMode == MODE_CHUNK)
        {
            SnowProvider provider = new SnowProvider();
            provider.deserializeNBT(ByteBufUtils.readTag(buf).getTag("manager"));
            this.snowManager = provider.getCapability(SnowStorage.CAPABILITY_SNOW, null);
            Vector2i cpro = Helpers.stringToChunkPos(ByteBufUtils.readUTF8String(buf));
            this.chunkPos = new ChunkPos(cpro.x, cpro.y);
        }
        else if(syncMode == MODE_BLOCK)
        {
            this.blockPos = Helpers.parseBlockPosFromString(ByteBufUtils.readUTF8String(buf));
            this.snowy = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.syncMode);
        if(this.syncMode == MODE_CHUNK)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("manager", new SnowProvider(this.snowManager).serializeNBT());
            ByteBufUtils.writeTag(buf, compound);
            ByteBufUtils.writeUTF8String(buf, Helpers.chunkPosToString(this.chunkPos.x, this.chunkPos.z));
        }
        else if(this.syncMode == MODE_BLOCK)
        {
            ByteBufUtils.writeUTF8String(buf, Helpers.blockPosToString(this.blockPos));
            buf.writeBoolean(this.snowy);
        }
    }

    public static class Handler implements IMessageHandler<PacketUpdateSnowManager, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketUpdateSnowManager message, MessageContext ctx)
        {
            if(message.syncMode == MODE_CHUNK)
            {
                Runnable toRun = () ->
                {
                    World world = Minecraft.getMinecraft().player.world;
                    if(world != null)
                    {
                        Chunk chunk = world.getChunk(message.chunkPos.x, message.chunkPos.z);
                        ISnowManager before = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                        if (before != null)
                        {
                            before.copy(message.snowManager);
                        }
                        world.markBlockRangeForRenderUpdate(message.chunkPos.getXStart(), 0, message.chunkPos.getZStart(), message.chunkPos.getXEnd(), 256, message.chunkPos.getZEnd());
                    }
                };
                if(Minecraft.getMinecraft().player == null)
                {
                    KeldariaClientHandler.RUN_WHEN_PLAYER_IS_HERE.add(toRun);
                }
                else
                {
                    toRun.run();
                }
            }
            else if(message.syncMode == MODE_BLOCK)
            {
                World world = Minecraft.getMinecraft().player.world;
                Chunk chunk = world.getChunk(message.blockPos);
                ISnowManager before = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                if(before != null)
                {
                    before.setSnow(message.blockPos, message.snowy);
                }
                world.markBlockRangeForRenderUpdate(message.blockPos.north().east().up(), message.blockPos.south().west().down());
            }
            return null;
        }
    }

}
