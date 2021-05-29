package fr.nathanael2611.keldaria.mod.proxy;

import de.matthiasmann.twl.utils.PNGDecoder;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.client.ren.REN;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFruitBlock;
import fr.nathanael2611.keldaria.mod.block.furniture.TileEntityChessPlate;
import fr.nathanael2611.keldaria.mod.client.KeldariaDiscordRPC;
import fr.nathanael2611.keldaria.mod.client.handler.KeldariaClientHandler;
import fr.nathanael2611.keldaria.mod.client.handler.KeldariaKeyboardHandler;
import fr.nathanael2611.keldaria.mod.client.handler.KeldariaRenderHandler;
import fr.nathanael2611.keldaria.mod.client.layer.LayerHorseStorage;
import fr.nathanael2611.keldaria.mod.client.layer.LayerQuiver;
import fr.nathanael2611.keldaria.mod.client.layer.LayerSpur;
import fr.nathanael2611.keldaria.mod.client.layer.LayerWeapons;
import fr.nathanael2611.keldaria.mod.client.render.RenderLammergeier;
import fr.nathanael2611.keldaria.mod.client.render.entity.RenderHomingPigeon;
import fr.nathanael2611.keldaria.mod.client.render.entity.bestiary.RenderBestiarySpider;
import fr.nathanael2611.keldaria.mod.client.render.teisr.RoundShieldRenderer;
import fr.nathanael2611.keldaria.mod.client.render.tesr.*;
import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import fr.nathanael2611.keldaria.mod.entity.EntityLucrain;
import fr.nathanael2611.keldaria.mod.entity.bestiary.spider.Spider;
import fr.nathanael2611.keldaria.mod.item.ItemRemedyVial;
import fr.nathanael2611.keldaria.mod.item.ItemWashingSoap;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.season.SeasonHandler;
import fr.nathanael2611.keldaria.mod.tileentity.*;
import fr.nathanael2611.obfuscate.remastered.client.ObfuscateEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Map;

public class ClientProxy extends CommonProxy
{

    public static String accessToken;

    public static final KeyBinding KEY_ANIMATION = new KeyBinding("Animation Menu", Keyboard.KEY_L, "Keldaria");
    public static final KeyBinding KEY_RPCHAT = new KeyBinding("RP-Chat-Gui", Keyboard.KEY_R, "Keldaria");
    public static final KeyBinding KEY_SKIN_CHOOSER = new KeyBinding("Skin Chooser", Keyboard.KEY_N, "Keldaria");
    public static final KeyBinding KEY_ARMPOSES = new KeyBinding("Positions", Keyboard.KEY_I, "Keldaria");
    public static final KeyBinding KEY_CLOTHS = new KeyBinding("Vêtement/Armure", Keyboard.KEY_K, "Keldaria");
    public static final KeyBinding KEY_WALKMODE = new KeyBinding("Mode Marche", Keyboard.KEY_J, "Keldaria");
    public static final KeyBinding KEY_ROLL = new KeyBinding("Roll-Menu", Keyboard.KEY_H, "Keldaria");
    public static final KeyBinding KEY_ATTACK_MODE = new KeyBinding("Change AttackMode", Keyboard.KEY_C, "Keldaria");

    public static final KeyBinding KEY_HEAD_ACCESSORY = new KeyBinding("Accessoire Tête", Keyboard.KEY_NONE, "Keldaria Accessories");
    public static final KeyBinding KEY_CHEST_ACCESSORY = new KeyBinding("Accessoire Torse", Keyboard.KEY_NONE, "Keldaria Accessories");
    public static final KeyBinding KEY_LEGS_ACCESSORY = new KeyBinding("Accessoire Jambes", Keyboard.KEY_NONE, "Keldaria Accessories");
    public static final KeyBinding KEY_FEET_ACCESSORY = new KeyBinding("Accessoire Pieds", Keyboard.KEY_NONE, "Keldaria Accessories");

    public static final REN REN = new REN();

    @Override
    public void preInitialization(FMLPreInitializationEvent event)
    {
        super.preInitialization(event);
        Display.setTitle("Keldaria - Serveur RP Médiéval Fantastique");
        KeldariaDiscordRPC.start();
        this.setCustomIcon();
        this.registerEvents();
        this.registerTESR();
        REN.preInit(event);
    }

    @Override
    public void initialization(FMLInitializationEvent event)
    {
        super.initialization(event);
        this.registerKeyBindings();
        this.registerColorHandlers();
        this.addNewLayers();

        RenderingRegistry.registerEntityRenderingHandler(EntityLucrain.class,
                new RenderLammergeier(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(Spider.class,
                new RenderBestiarySpider(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityHomingPigeon.class,
                new RenderHomingPigeon(Minecraft.getMinecraft().getRenderManager()));

        REN.init(event);
    }

    private void addNewLayers()
    {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();

        Class c = RenderManager.class;
        Field field = c.getDeclaredFields()[1];
        field.setAccessible(true);
        Map<String, RenderPlayer> skinMap = /*manager.getSkinMap()*/ null;
        try
        {
            skinMap = (Map<String, RenderPlayer>) field.get(manager);
            skinMap.get("default").addLayer(new LayerWeapons());
            //skinMap.get("slim").addLayer(new LayerWeapons());
            skinMap.get("default").addLayer(new LayerSpur());
            //skinMap.get("slim").addLayer(new LayerSpur());
            skinMap.get("default").addLayer(new LayerQuiver());
            //skinMap.get("slim").addLayer(new LayerQuiver());

            skinMap.put("slim", skinMap.get("default"));

        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        Render<EntityHorse> render = manager.getEntityClassRenderObject(EntityHorse.class);
        if (render instanceof RenderHorse)
        {
            RenderHorse renderHorse = (RenderHorse) render;
            renderHorse.addLayer(new LayerHorseStorage());
        }
    }

    private void registerColorHandlers()
    {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex > 0 ? -1 : ItemWashingSoap.getSoap(stack).getRGB(), KeldariaItems.SOAP);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex > 0 ? -1 : (ItemRemedyVial.haveRemedy(stack) ? ItemRemedyVial.getRemedy(stack).getRGB() : 0), KeldariaItems.REMEDY_VIAL);
        SeasonHandler.registerColorResolvers();
    }

    private void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new KeldariaClientHandler());
        MinecraftForge.EVENT_BUS.register(new KeldariaRenderHandler());
        MinecraftForge.EVENT_BUS.register(new KeldariaKeyboardHandler());
        MinecraftForge.EVENT_BUS.register(new ObfuscateEvents());
        //MinecraftForge.EVENT_BUS.register(new ClientCombatEvents());
    }

    private void registerKeyBindings()
    {
        ClientRegistry.registerKeyBinding(KEY_ANIMATION);
        ClientRegistry.registerKeyBinding(KEY_RPCHAT);
        ClientRegistry.registerKeyBinding(KEY_SKIN_CHOOSER);
        ClientRegistry.registerKeyBinding(KEY_CLOTHS);
        ClientRegistry.registerKeyBinding(KEY_WALKMODE);
        ClientRegistry.registerKeyBinding(KEY_ROLL);
        ClientRegistry.registerKeyBinding(KEY_ATTACK_MODE);
        ClientRegistry.registerKeyBinding(KEY_HEAD_ACCESSORY);
        ClientRegistry.registerKeyBinding(KEY_CHEST_ACCESSORY);
        ClientRegistry.registerKeyBinding(KEY_LEGS_ACCESSORY);
        ClientRegistry.registerKeyBinding(KEY_FEET_ACCESSORY);
        ClientRegistry.registerKeyBinding(KEY_ARMPOSES);
        AnimationUtils.registerAnimationKeys();
    }

    private void registerTESR()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCookingFurnace.class, new TileEntityCookingFurnaceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFloweringPot.class, new TileEntityFloweringPotRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVineSupport.class, new TileEntityVineSupportRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHRPSign.class, new TileEntityHRPSignRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHRPCarpet.class, new TileEntityHRPCarpetRender());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeeder.class, new TileEntityFeederRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCustomPainting.class, new TileEntityCustomPaintingRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCardCarpet.class, new TileEntityCardCarpetRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityClothingMannequin.class, new TileEntityClothingMannequinRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChessPlate.class, new TileEntityChessPlateRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCardDeck.class, new TileEntityCardDeckRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallpaper.class, new TileEntityWallpaperRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFruitBlock.class, new TileEntityFruitBlockRenderer());

        KeldariaItems.WOODEN_ROUND_SHIELD.setTileEntityItemStackRenderer(new RoundShieldRenderer());

    }

    private void setCustomIcon()
    {
        Util.EnumOS os = Util.getOSType();
        if (os != Util.EnumOS.OSX)
        {
            try
            {
                Display.setIcon(new ByteBuffer[]{loadIcon("16.PNG"), loadIcon("32.PNG")});
            }
            catch (Exception e)
            {
                System.out.println("Failed to set custom icon");
                e.printStackTrace();
            }
        }
    }

    private static ByteBuffer loadIcon(String path) throws IOException
    {
        try (InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Keldaria.MOD_ID, path)).getInputStream())
        {
            PNGDecoder decoder = new PNGDecoder(inputStream);
            ByteBuffer bytebuf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
            decoder.decode(bytebuf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            bytebuf.flip();
            return bytebuf;
        }
    }

}
