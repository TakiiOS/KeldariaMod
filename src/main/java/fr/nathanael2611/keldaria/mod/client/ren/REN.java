/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.ren;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.lwjgl.input.Keyboard;

public class REN
{
    public static final REN instance;
    public static float bodyOffset;
    public static int[] modes;
    public static boolean customItemOverride;
    private static boolean wasF4DownLastTick;
    public static byte currentMode;
    private static Entity dummy;
    private static String[] overrideItems;

    public void preInit(final FMLPreInitializationEvent event)
    {
        this.initModMetadata(event);
        RenderingRegistry.registerEntityRenderingHandler((Class) EntityPlayerDummy.class, (IRenderFactory) new RenderFactory());
        REN.overrideItems = new String[]{"minecraft:filled_map", "minecraft:compass", "minecraft:clock"};
        REN.bodyOffset = 0.35f;
        REN.modes = new int[]{111, 011, 001};
        //REN.modes = config.get("general", "Modes", new String[] { "111", "011", "001" }, "Mode IDs.  \nFirst number is whether to render arms in 3D or 2D mode.  \nSecond number is whether or not to render the body model.  \nThird number is whether or not to render the HUD.  \nAdd, delete, or change the order as you wish.").getIntList();
        //config.save();
    }

    public void init(final FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register((Object) REN.instance);
        MinecraftForge.EVENT_BUS.register((Object) REN.instance);
        EntityRegistry.registerModEntity(new ResourceLocation("keldaria", "dummy"), (Class) EntityPlayerDummy.class, "PlayerDummy", 31, (Object) "keldaria", 100, 10, false);
    }

    private void initModMetadata(final FMLPreInitializationEvent event)
    {
        final ModMetadata meta = event.getModMetadata();
        meta.name = "Real First-Person Render";
        meta.description = "Small mod that adds in full-body rendering.";
        meta.authorList.clear();
        meta.authorList.add("don_bruce");
        meta.authorList.add("Wyn Price");
        meta.authorList.add("kenijey");
        meta.modId = "realrender";
        meta.version = "1.2.3";
        meta.autogenerated = false;
    }

    @SubscribeEvent
    public void on(final TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals((Object) TickEvent.Phase.START))
        {
            if (Keyboard.isKeyDown(62))
            {
                if (!REN.wasF4DownLastTick)
                {
                    if (REN.currentMode == REN.modes.length - 1)
                    {
                        REN.currentMode = 0;
                    } else
                    {
                        ++REN.currentMode;
                    }
                }
                REN.wasF4DownLastTick = true;
            } else
            {
                REN.wasF4DownLastTick = false;
            }
        }
        REN.customItemOverride = false;
        final EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().player;
        if (player != null)
        {
            for (final String itemName : REN.overrideItems)
            {
                if (Item.getByNameOrId(itemName) != null && (player.getHeldItemMainhand().getItem() == Item.getByNameOrId(itemName) || player.getHeldItemOffhand().getItem() == Item.getByNameOrId(itemName)))
                {
                    REN.customItemOverride = true;
                    break;
                }
            }
            if (REN.dummy == null)
            {
                if (Minecraft.getMinecraft().world != null)
                {
                    REN.dummy = new EntityPlayerDummy((World) Minecraft.getMinecraft().world);
                    Minecraft.getMinecraft().world.spawnEntity(REN.dummy);
                    REN.dummy.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
                }
            } else if (REN.dummy.world.provider.getDimension() != player.world.provider.getDimension() || REN.dummy.getDistanceSq((Entity) player) > 2.0 || REN.dummy.isDead)
            {
                REN.dummy = null;
            }
        }
    }

    @SubscribeEvent
    public void on(final RenderHandEvent event)
    {
        event.setCanceled(REN.modes[REN.currentMode] / 100 == 1 && !REN.customItemOverride && !Minecraft.getMinecraft().player.isElytraFlying());
    }

    @SubscribeEvent
    public void onItemToss(final InputEvent.KeyInputEvent event)
    {
        if (!Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty() && Minecraft.getMinecraft().gameSettings.keyBindDrop.isPressed())
        {
            KeyBinding.onTick(Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode());
            Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    static
    {
        instance = new REN();
        REN.currentMode = 0;
    }
}
