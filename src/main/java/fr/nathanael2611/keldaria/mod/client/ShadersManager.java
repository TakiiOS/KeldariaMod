/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.features.OpticIssue;
import fr.nathanael2611.keldaria.mod.features.accessories.Accessories;
import fr.nathanael2611.keldaria.mod.item.ItemGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ShadersManager
{

    public static boolean LoadShader = false;
    public static String localJarName = "";
    public static boolean isDefault = true;
    public static int lastThird = 0;
    public static boolean lastF1 = false;

    @SubscribeEvent
    public void clientTickEvent(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.player != null && mc.player.world != null)
        {
            boolean loadDefault = false;
            if(OpticIssue.MYOPIA.has(mc.player))
            {
                try{
                    Accessories accessories = Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player);
                    if(!(accessories.getInventory().getAccessory(EntityEquipmentSlot.HEAD).getItem() instanceof ItemGlasses))
                    {
                        loadShader("blur", false);
                    }
                    else
                    {
                        loadDefault = true;
                    }
                } catch(Exception e)
                {
                    loadDefault = true;
                }
            }
            else
            {
                loadDefault = true;
            }
            if(loadDefault)
            {
                loadShader("", true);
            }
        }
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent event)
    {
        if (isDefault)
        {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
        }
        if (LoadShader & !isDefault)
        {
            Minecraft.getMinecraft().entityRenderer.stopUseShader();
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation(localJarName));
            LoadShader = false;
        }
    }

    public static void loadShader(final String jarName, final boolean isDefaultIn)
    {
        Minecraft mc = Minecraft.getMinecraft();
        String s = "minecraft:shaders/post/" + jarName + ".json";
        if (lastF1 == mc.gameSettings.hideGUI && lastThird == mc.gameSettings.thirdPersonView && localJarName.equalsIgnoreCase(s))
            return;
        localJarName = s;
        isDefault = isDefaultIn;
        LoadShader = true;
        lastThird = mc.gameSettings.thirdPersonView;
        lastF1 = mc.gameSettings.hideGUI;

    }

}
