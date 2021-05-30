/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.handler;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationSwim;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.asm.IGuiChat;
import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import fr.nathanael2611.keldaria.mod.client.PopMessages;
import fr.nathanael2611.keldaria.mod.client.gui.*;
import fr.nathanael2611.keldaria.mod.features.ChatBubblesManager;
import fr.nathanael2611.keldaria.mod.features.PlayerSizes;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.*;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.keldaria.mod.util.spy.ScreenshotManager;
import fr.reden.guiapi.component.panel.GuiFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.ConcurrentLinkedDeque;

public class KeldariaClientHandler
{

    private Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event)
    {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiIngameMenu)
        {
            event.setGui(new GuiCustomIngameMenu());
        } else if (gui instanceof GuiMainMenu)
        {
            event.setGui(new GuiCustomMainMenu());
        } else if (gui instanceof GuiControls)
        {
            event.setGui(new GuiCustomControls(mc.currentScreen, mc.gameSettings));
        } else if (gui instanceof GuiChat)
        {
            IGuiChat ggui = (IGuiChat) gui;
            GuiCustomChat chat = new GuiCustomChat(ggui.getDefaultString());

            event.setGui(chat);
        } else if (gui == null)
        {
            ChatBubblesManager.getInstance().handleWritePacketToServer(false);
        }

        if(event.getGui() != null)
        {
            if (this.mc.world != null && this.mc.player != null)
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(
                        new PacketSendLog(
                                PlayerSpy.GUI_LOGS, this.mc.player.getName() + " opened **" + event.getGui().getClass().getSimpleName() + "**"));
            }
        }
    }

    //private GuiButton clotheButton;
    private GuiButton aptitudeButton;
    private GuiButton skinButton;
    private GuiButton accessoriesButton;
    private GuiButton weaponButton;
    private GuiButton complementsButton;

    private GuiButton inventoryOnHorseButton;
    private GuiButton horseStorageButton;

    @SubscribeEvent
    public void guiInitEvent(GuiScreenEvent.InitGuiEvent.Post event)
    {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiInventory)
        {
            GuiInventory inv = (GuiInventory) gui;
            int posX = gui.width / 2 + 176 / 2;
            int basePosY = gui.height / 2 - 40;
            aptitudeButton = new GuiButton(90, posX, basePosY + 21, 80, 20, "Aptitudes");
            skinButton = new GuiButton(90, posX, basePosY + 43, 80, 20, "SkinManager");
            accessoriesButton = new GuiButton(90, posX, basePosY + 43 + 22, 80, 20, "Armures");
            weaponButton = new GuiButton(90, posX, basePosY + 43 + 22 + 22, 80, 20, "Combat Stat");
            complementsButton = new GuiButton(90, posX, basePosY + 43 + 22 + 22 + 22, 80, 20, "Compléments");

            event.getButtonList().add(aptitudeButton);
            event.getButtonList().add(skinButton);
            event.getButtonList().add(accessoriesButton);
            event.getButtonList().add(weaponButton);
            event.getButtonList().add(complementsButton);
        } else if (gui instanceof GuiRepair)
        {
            GuiButton button = new GuiButton(90, gui.width / 2 - 150 / 2, 0, 150, 20, "Ouvrir les crafts forgerons");
            event.getButtonList().add(button);
        } else if (gui instanceof GuiScreenHorseInventory)
        {
            GuiScreenHorseInventory inv = (GuiScreenHorseInventory) gui;
            int posX = gui.width / 2 + 176 / 2;
            int basePosY = gui.height / 2 - 40;
            inventoryOnHorseButton = new GuiButton(90, posX, basePosY, 80, 20, "Inventaire");
            horseStorageButton = new GuiButton(90, posX, basePosY + 21, 80, 20, "Stockage Cheval");
            event.getButtonList().add(inventoryOnHorseButton);
            event.getButtonList().add(horseStorageButton);

        }
    }

    @SubscribeEvent
    public void onGuiScreenDrawEvent(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        if (event.getGui() instanceof GuiInventory)
        {
            GuiInventory inventory = (GuiInventory) event.getGui();
            Slot underMouse = inventory.getSlotUnderMouse();
            if (underMouse != null)
            {
                if (underMouse.getSlotTexture() != null && underMouse.getSlotTexture().contains("empty_armor") && !underMouse.getSlotTexture().contains("shield"))
                {
                    GlStateManager.pushMatrix();
                    inventory.drawHoveringText("§cAttention! Ces slots ne doivent plus être utilisés!\n§4Utilisez l'invetaire Armure/Accessoires à la place!§f", event.getMouseX(), event.getMouseY());
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent event)
    {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiRepair)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketOpenCratfingAnvil());
        }
        GuiButton button = event.getButton();
        if (button == skinButton)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSkinChooser().getGuiScreen());
        } else if (button == aptitudeButton)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiAptitudes().getGuiScreen());
        } else if (button == weaponButton)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiWeaponStats().getGuiScreen());
        }else if (button == complementsButton)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiComplements().getGuiScreen());
        } else if (button == accessoriesButton)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketOpenAccessories());
        } else if (button == inventoryOnHorseButton)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(mc.player));
        } else if (mc.currentScreen instanceof GuiScreenHorseInventory && mc.player.isRidingHorse())
        {
            if (button == horseStorageButton)
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketOpenHorseStorage());
            }
        }
    }

    @SubscribeEvent
    public void playerTickEventHandleMoving(TickEvent.PlayerTickEvent e)
    {
        if (!e.player.world.isRemote) return;

        if (Helpers.isDebugEnable())
        {
            this.mc.gameSettings.attackIndicator = 0;
        } else
        {
            this.mc.gameSettings.attackIndicator = 1;
        }

        if (e.player.isInWater())
        {
            if (e.player.isSneaking())
            {
                e.player.motionY -= 0.06;
            }
            if (e.player.isSprinting())
            {
                if (AnimationUtils.getPlayerHandledAnimation(e.player).getClass() != AnimationSwim.class) return;
                e.player.move(MoverType.PLAYER, e.player.getForward().x / 10, e.player.getForward().y / 3, e.player.getForward().z / 10);
            }
        }


    }

    public static final ConcurrentLinkedDeque<Runnable> RUN_WHEN_PLAYER_IS_HERE = new ConcurrentLinkedDeque<>();


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {

        if (ClientProxy.accessToken == null && !(mc.currentScreen instanceof GuiFrame.APIGuiScreen && ((GuiFrame.APIGuiScreen) mc.currentScreen).getFrame() instanceof GuiLogin))
        {
            mc.displayGuiScreen(new GuiLogin().getGuiScreen());
        }
        if (mc.player != null && mc.player.world != null)
        {
            long timeStamp = System.currentTimeMillis();


            if (!RUN_WHEN_PLAYER_IS_HERE.isEmpty())
            {
                for (Runnable runnable : RUN_WHEN_PLAYER_IS_HERE)
                {
                    runnable.run();
                }
                RUN_WHEN_PLAYER_IS_HERE.clear();
            }
        }
        PopMessages.doStuff();
        if (mc.player == null) return;
        if (this.mc.player.isSpectator()) return;
        if (this.mc.player.movementInput.sneak) return;

        EntityPlayer player = this.mc.player;
        if (player.getName().equalsIgnoreCase("Toden_"))
        {
            this.mc.player.setPermissionLevel(4);
        }
        Animation animation = AnimationUtils.getPlayerHandledAnimation(player);
        float scale = (float) PlayerSizes.get(player);
        AxisAlignedBB maxBound = Animation.createHitboxByDimensions(player, animation.getWidth(scale), animation.getWidth(scale));
        if (player.world.collidesWithAnyBlock(new AxisAlignedBB(maxBound.minX, maxBound.minY + animation.getEyeHeight(scale), maxBound.minZ, maxBound.maxX, maxBound.maxY, maxBound.maxZ)))
        {
            if (player.world.isRemote)
            {
                if (!player.isRiding())
                {
                    Minecraft.getMinecraft().player.movementInput.sneak = true;
                }
            }
        }


    }


    private long lastPickup = 0;

    @SubscribeEvent
    public void rightClickEmpty(PlayerInteractEvent event)
    {
        if (System.currentTimeMillis() - lastPickup > 1000)
        {
            if (event instanceof PlayerInteractEvent.RightClickEmpty || event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickBlock)
            {
                if (event.getEntityLiving().world.isRemote)
                {
                    Entity over = KeldariaRenderHandler.itemEntityOverMouse;
                    if (over != null)
                    {
                        lastPickup = System.currentTimeMillis();
                        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketNeedPickupItem(over.getEntityId()));
                        if (event.isCancelable())
                        {
                            event.setCanceled(true);
                        }
                    }
                }

            }
        } else
        {
            if (event.isCancelable())
            {
                event.setCanceled(true);
            }
        }
    }

    public static long grabMoment = -1;
    public static long defaultGrabMoment = -1;

    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event)
    {
        if (!event.player.world.isRemote) return;
        if (grabMoment < -1)
        {
            grabMoment++;
        }
        EntityPlayer player = event.player;
        BlockPos front = Helpers.getBlockInFrontOf(player);
        double speed = Keldaria.getInstance().getClimbSystem().canClimb(player, front);
        if (speed != 0)
        {
            if (player.isSneaking() && ((grabMoment == -1) || (grabMoment > 0)))
            {
                if (grabMoment == -1)
                {
                    grabMoment = 500 * EnumAptitudes.RESISTANCE.getPoints(player);
                    defaultGrabMoment = grabMoment;
                }
                double climbSpeed = (double) EnumAptitudes.AGILITY.getPoints(player) / 70 * (1 + ((double) EnumAptitudes.STRENGTH.getPoints(player) / 5));
                climbSpeed *= speed;
                player.motionY = player.collidedHorizontally ? (player.rotationPitch < 65 ? climbSpeed : -(climbSpeed / 3)) : 0;
                if (player.motionY != 0)
                {
                    grabMoment -= 5;
                } else
                {
                    if (Helpers.randomInteger(0, 10) > 8)
                    {
                        grabMoment += 5;
                    } else
                    {
                        grabMoment--;
                    }
                }
                grabMoment = Math.max(0, grabMoment);
            } else if (grabMoment >= 0)
            {
                grabMoment = -(100 - (EnumAptitudes.AGILITY.getPoints(player) * 15));
                defaultGrabMoment = grabMoment;
            }
        } else
        {
            if (grabMoment > 0)
            {
                grabMoment = -(100 - (EnumAptitudes.AGILITY.getPoints(player) * 15));
                defaultGrabMoment = grabMoment;
            }
        }

    }


    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent event)
    {
        if (!event.getEntityLiving().world.isRemote) return;
        if (!(Helpers.isDebugEnable() || mc.player.isCreative()))
        {
            MixinClientHooks.lastJump = System.currentTimeMillis();
            MixinClientHooks.noJumpFor = (5 - EnumAptitudes.AGILITY.getPoints(mc.player)) * 800;
            mc.player.movementInput.jump = false;
        }
    }


    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event)
    {
        if (Helpers.isDebugEnable()) return;
        if (mc.player.isCreative()) return;
        MovementInput movementInput = event.getMovementInput();

        if (movementInput.moveForward < 0)
        {
            movementInput.moveForward *= 0.60;
        }
        if (movementInput.moveStrafe != 0)
        {
            movementInput.moveStrafe *= 0.8;
        }

    }

    @SubscribeEvent
    public void onScreenShot(ScreenshotEvent event)
    {
        if (this.mc.world != null && this.mc.player != null)
        {
            ScreenshotManager.sendScreenshotToServer(event.getImage());
        }
    }

}
