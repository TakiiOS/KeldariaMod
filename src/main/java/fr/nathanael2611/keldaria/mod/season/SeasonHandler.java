package fr.nathanael2611.keldaria.mod.season;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.api.RenderBlockEvent;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toclient.PacketUpdateSnowManager;
import fr.nathanael2611.keldaria.mod.proxy.CommonProxy;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowProvider;
import fr.nathanael2611.keldaria.mod.season.snow.storage.SnowStorage;
import fr.nathanael2611.keldaria.mod.season.snow.storage.api.ISnowManager;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SeasonHandler
{

    public static final ResourceLocation SNOW_MANAGER_LOCATION = new ResourceLocation(Keldaria.MOD_ID, "snow_manager");
    public static final ResourceLocation GROW_MANAGER_LOCATION = new ResourceLocation(Keldaria.MOD_ID, "grow_manager");

    private Keldaria keldaria;
    private final ExecutorService SERVICE = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("season-thread-%d").build());

    public static SeasonHandler INSTANCE;

    public SeasonHandler(Keldaria keldaria)
    {
        INSTANCE = this;
        this.keldaria = keldaria;
    }

    private long lastChunksCheck = -1;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        long time = System.currentTimeMillis();
        if (this.lastChunksCheck == -1 || time - this.lastChunksCheck > 10000)
        {
            this.lastChunksCheck = time;
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);

            if (Season.isWinter(world))
            {
                this.execute(() ->
                {
                    for (Chunk loadedChunk : Helpers.getLoadedChunks(world))
                    {
                        if (loadedChunk != null)
                        {
                            ISnowManager snowManager = loadedChunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                            if (snowManager != null)
                            {
                                if (world.isRaining())
                                {
                                    for (int i = 0; i < snowManager.getDiggedPoses().size(); i++)
                                    {
                                        BlockPos diggedPose = snowManager.getDiggedPoses().get(i);
                                        if (world.isRainingAt(diggedPose) || world.isRainingAt(diggedPose.up()))
                                        {
                                            snowManager.snow(diggedPose);
                                            sendUpdate(loadedChunk, diggedPose);
                                        }
                                    }
                                }
                                loadedChunk.markDirty();
                            }
                        }
                    }
                });
            }

        }
    }

    @SubscribeEvent
    public void onCapabilityAttach(AttachCapabilitiesEvent<Chunk> event)
    {
        event.addCapability(SNOW_MANAGER_LOCATION, new SnowProvider());

    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event)
    {
        if (event.getWorld().isRemote) return;
        World world = event.getWorld();
        if (Season.isWinter(event.getWorld()))
        {
            SERVICE.execute(() ->
            {
                Chunk chunk = world.getChunk(event.getPos());
                ISnowManager snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                if (snowManager != null)
                {
                    snowManager.dig(event.getPos());
                    chunk.markDirty();
                    sendUpdate(chunk, event.getPos());
                }
            });
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event)
    {
        if (event.getWorld().isRemote) return;
        World world = event.getWorld();
        if (Season.isWinter(event.getWorld()))
        {
            SERVICE.execute(() ->
            {
                Chunk chunk = world.getChunk(event.getPos());
                ISnowManager snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                if (snowManager != null)
                {
                    snowManager.dig(event.getPos().down());
                    chunk.markDirty();
                    sendUpdate(chunk, event.getPos().down());
                }
            });
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (!Season.isWinter(player.world)) return;
        EnumHand hand = event.getHand();
        ItemStack handItem = player.getHeldItem(hand);
        if (handItem.getItem() instanceof ItemSpade && !player.getCooldownTracker().hasCooldown(handItem.getItem()))
        {
            Chunk chunk = player.world.getChunk(event.getPos());
            ISnowManager snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
            Biome biome = player.world.getBiome(event.getPos());
            if (
                    snowManager != null &&
                            snowManager.isSnowy(event.getPos()) && Season.canSnowIn(biome) &&
                            Blocks.SNOW_LAYER.canPlaceBlockAt(event.getWorld(), event.getPos().up())
                            && Blocks.SNOW_LAYER.canPlaceBlockAt(event.getWorld(), event.getPos()) &&
                            chunk.canSeeSky(event.getPos().up()))
            {
                event.setCanceled(true);
                snowManager.dig(event.getPos());
                chunk.markDirty();
                sendUpdate(chunk, event.getPos());
                player.world.markAndNotifyBlock(event.getPos(), chunk, player.world.getBlockState(event.getPos()), player.world.getBlockState(event.getPos()), 3);
                player.getCooldownTracker().setCooldown(handItem.getItem(), 20);
                EntityItem snow = new EntityItem(player.world, event.getPos().getX(), event.getPos().getY() + 1, event.getPos().getZ());
                snow.setItem(new ItemStack(Items.SNOWBALL, 2));
                player.world.spawnEntity(snow);
            }
        }
    }

    public static void sendUpdate(Chunk chunk, BlockPos pos)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketUpdateSnowManager(chunk, pos));
    }

    public static void sendChunk(Chunk chunk, EntityPlayerMP player)
    {
        KeldariaPacketHandler.getInstance().getNetwork().sendTo(new PacketUpdateSnowManager(chunk), player);
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event)
    {
        if (event.getEntity().world.isRemote || !(event.getEntity() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntity();
        World world = player.world;
        if (world.getChunkProvider() instanceof ChunkProviderServer)
        {
            ChunkProviderServer chunkProvider = (ChunkProviderServer) world.getChunkProvider();
            for (Chunk loadedChunk : chunkProvider.getLoadedChunks())
            {
                CommonProxy.executeAfter(() -> sendChunk(loadedChunk, (EntityPlayerMP) player), 5000);
            }
        }
    }




    @SubscribeEvent
    public void onChunkLoaded(ChunkEvent.Load event)
    {
        if (event.getWorld().isRemote) return;
        if (Season.isWinter(event.getWorld()))
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToAll(new PacketUpdateSnowManager(event.getChunk()));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderBlock(RenderBlockEvent event)
    {
        if (Season.isWinter(Minecraft.getMinecraft().world))
        {
            IBlockState state = event.getState();
            IBlockState toDraw = Blocks.SNOW_LAYER.getDefaultState();
            //if (event.getAccess() instanceof ChunkCache)
            {
                BlockPos posUp = event.getPos().up();
                Chunk chunk = Minecraft.getMinecraft().world.getChunk(event.getPos());
                Biome biome = Minecraft.getMinecraft().world.getBiome(posUp);
                ISnowManager snowManager = chunk.getCapability(SnowStorage.CAPABILITY_SNOW, null);
                if (snowManager != null && Season.canSnowIn(biome))
                {
                    if (
                            snowManager.isSnowy(event.getPos()) &&
                                    /*Blocks.SNOW_LAYER.canPlaceBlockAt(Minecraft.getMinecraft().world, event.getPos()) &&*/
                                    Minecraft.getMinecraft().world.canSnowAt(event.getPos(), true)&&
                                    //Blocks.SNOW_LAYER.canPlaceBlockAt(Minecraft.getMinecraft().world, posUp) &&
                                    chunk.canSeeSky(posUp))
                    {
                        //MixinClientHooks.renderBlock(event.getBlockRendererDispatcher(), event.getBlockModelRenderer(), event.getBlockFluidRenderer(), toDraw, posUp, event.getAccess(), event.getBufferBuilder(), false);
                        IBakedModel model = event.getBlockRendererDispatcher().getModelForState(Blocks.SNOW_LAYER.getDefaultState());
                        event.getBlockModelRenderer().renderModel(event.getAccess(),
                                model, Blocks.SNOW_LAYER.getDefaultState(), event.getPos().up(), event.getBufferBuilder(), true);

                        if(event.getState().getBlock() == Blocks.GRASS)
                        {
                            event.setState(Blocks.GRASS.getDefaultState().withProperty(BlockGrass.SNOWY, true));
                        }
                    }
                }
            }
        }
    }


    @SideOnly(Side.CLIENT)
    private static BiomeColorHelper.ColorResolver originalGrassColorResolver;
    @SideOnly(Side.CLIENT)
    private static BiomeColorHelper.ColorResolver originalFoliageColorResolver;

    @SideOnly(Side.CLIENT)
    public static void registerColorResolvers()
    {
        originalGrassColorResolver = BiomeColorHelper.GRASS_COLOR;
        originalFoliageColorResolver = BiomeColorHelper.FOLIAGE_COLOR;

        BiomeColorHelper.GRASS_COLOR = (biome, blockPosition) -> {
            World world = Minecraft.getMinecraft().world;
            return SeasonColorUtils.applySeasonalGrassColouring(Season.getSeason(world), biome, originalGrassColorResolver.getColorAtPos(biome, blockPosition));
        };

        BiomeColorHelper.FOLIAGE_COLOR = (biome, blockPosition) -> {
            World world = Minecraft.getMinecraft().world;
            return SeasonColorUtils.applySeasonalFoliageColouring(Season.getSeason(world), biome, originalFoliageColorResolver.getColorAtPos(biome, blockPosition));
        };
    }

    public void execute(Runnable runnable)
    {
        this.SERVICE.execute(runnable);
    }

}
