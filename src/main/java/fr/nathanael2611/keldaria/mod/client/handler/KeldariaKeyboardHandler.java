/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.handler;

import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.animation.Animations;
import fr.nathanael2611.keldaria.mod.client.NewItemTextureCache;
import fr.nathanael2611.keldaria.mod.client.ShadersManager;
import fr.nathanael2611.keldaria.mod.client.gui.*;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.animation.PacketHandleAnimationRequest;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketOpenAccessories;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketToggleAThing;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeldariaKeyboardHandler
{

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_R))
        {
            NewItemTextureCache.MODELS.clear();
            NewItemTextureCache.IS_CREATING.clear();
            NewItemTextureCache.TRANSFORMS.clear();
            NewItemTextureCache.LAYERS.clear();
        }

        Minecraft mc = Minecraft.getMinecraft();
        if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown())
        {
            Animation animation = AnimationUtils.getPlayerHandledAnimation(mc.player);
            if(animation.isStopOnMove())
            {
                if(!mc.gameSettings.keyBindSneak.isKeyDown())
                {
                    if(animation != Animations.NONE)
                    {
                        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketHandleAnimationRequest(Animations.NONE.getName()));
                    }
                }
            }

        }

        AnimationUtils.ANIMATIONS_KEYS.forEach((keyBinding, animation) ->
        {
            if(keyBinding.isKeyDown())
            {
                if(AnimationUtils.getPlayerHandledAnimation(Minecraft.getMinecraft().player).getClass() == animation.getClass())
                {
                    animation = Animations.NONE;
                }
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketHandleAnimationRequest(animation.getName()));
            }
        });
        if(ClientProxy.KEY_ANIMATION.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiAnimationChooser());
        }
        else if(ClientProxy.KEY_ARMPOSES.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiArmsChooser().getGuiScreen());
        }
        else if(ClientProxy.KEY_RPCHAT.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiRolePlayChat());
        }
        else if(ClientProxy.KEY_SKIN_CHOOSER.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSkinChooser().getGuiScreen());
        }
        else if(ClientProxy.KEY_CLOTHS.isKeyDown())
        {
            //Minecraft.getMinecraft().displayGuiScreen(new GuiClothes(Minecraft.getMinecraft().player));
            //KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketOpenClothsWardrobe());
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketOpenAccessories());
        }
        else if(ClientProxy.KEY_WALKMODE.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_WALKMODE, false));
        }
        else if(ClientProxy.KEY_ROLL.isKeyDown())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiRollMenu().getGuiScreen());
        }
        else if(ClientProxy.KEY_ATTACK_MODE.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_CHANGE_COMBAT_TYPE, true));
        }
        else if(ClientProxy.KEY_HEAD_ACCESSORY.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_ACCESSORY_HEAD, false));
        }
        else if(ClientProxy.KEY_CHEST_ACCESSORY.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_ACCESSORY_CHEST, false));
        }
        else if(ClientProxy.KEY_LEGS_ACCESSORY.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_ACCESSORY_LEGS, false));
        }
        else if(ClientProxy.KEY_FEET_ACCESSORY.isKeyDown())
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketToggleAThing(PacketToggleAThing.ID_ACCESSORY_FEETS, false));
        }

    }

}
