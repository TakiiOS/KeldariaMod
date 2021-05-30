/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts;

import de.mennomax.astikorcarts.capabilities.IPull;
import de.mennomax.astikorcarts.capabilities.PullFactory;
import de.mennomax.astikorcarts.capabilities.PullStorage;
import de.mennomax.astikorcarts.handler.ClientEventHandler;
import de.mennomax.astikorcarts.handler.CommonEventHandler;
import de.mennomax.astikorcarts.handler.PacketHandler;
import de.mennomax.astikorcarts.init.ModEntities;
import de.mennomax.astikorcarts.init.ModKeybindings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

//@Mod(modid = AstikorCarts.MODID, version = AstikorCarts.VERSION, acceptedMinecraftVersions = "[1.12,1.13)")
public class AstikorCarts
{
    public static final String MODID = "astikorcarts";
    public static final String VERSION = "1.12.2-0.1.2.7";

    public void preInit(FMLPreInitializationEvent event)
    {
        PacketHandler.registerPackets();
        CapabilityManager.INSTANCE.register(IPull.class, new PullStorage(), PullFactory::new);
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
        if(event.getSide() == Side.CLIENT)
        {
            ModEntities.registerRenders();
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        }
    }

    public void init(FMLInitializationEvent event)
    {
        if(event.getSide() == Side.CLIENT)
        {
            ModKeybindings.registerKeyBindings();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
