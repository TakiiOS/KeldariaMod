/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.handler;

import com.google.common.collect.Lists;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import fr.nathanael2611.keldaria.mod.client.CustomSkins;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.PopMessages;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.GuiArmorInventory;
import fr.nathanael2611.keldaria.mod.client.gui.GuiArmsChooser;
import fr.nathanael2611.keldaria.mod.command.CommandLore;
import fr.nathanael2611.keldaria.mod.crafting.storage.KnownRecipesStorage;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.entity.animal.EntityKeldAnimal;
import fr.nathanael2611.keldaria.mod.features.*;
import fr.nathanael2611.keldaria.mod.features.accessories.Accessories;
import fr.nathanael2611.keldaria.mod.features.armoposes.ArmPoses;
import fr.nathanael2611.keldaria.mod.features.armoposes.Arms;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.features.containment.ContainmentSystem;
import fr.nathanael2611.keldaria.mod.features.containment.Points;
import fr.nathanael2611.keldaria.mod.features.food.ExpiredFoods;
import fr.nathanael2611.keldaria.mod.features.food.FoodQuality;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import fr.nathanael2611.keldaria.mod.features.thirst.Thirst;
import fr.nathanael2611.keldaria.mod.inventory.InventoryQuiver;
import fr.nathanael2611.keldaria.mod.item.*;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemAccessory;
import fr.nathanael2611.keldaria.mod.item.accessory.ItemQuiver;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFloweringPot;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.obfuscate.remastered.client.ModelPlayerEvent;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.component.panel.GuiFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class KeldariaRenderHandler
{

    private Minecraft mc = Minecraft.getMinecraft();

    private int frame = 0;
    private int timer = 0;

    public static Entity itemEntityOverMouse = null;


    @SubscribeEvent
    public void onModelPlayer(ModelPlayerEvent.SetupAngles.Post e)
    {
        EntityPlayer player = e.getEntityPlayer();
        ModelPlayer modelPlayer = e.getModelPlayer();
        /*FightControl<EntityPlayer> control = FightHandler.getFightControl(player);
        if(control != null && control.isAttacking())
        {
            Attack attack = control.getAttack();
            if(attack.getSwingTypes() == SwingTypes.LATERAL_RIGHT)
            {
                modelPlayer.bipedRightArm.rotateAngleX = (float) Math.toRadians(-Helpers.getYaw(player) + -control.getAttack().getRotation().y);
                modelPlayer.bipedRightArm.rotateAngleY = (float) Math.toRadians(-10);
                modelPlayer.bipedRightArm.rotateAngleZ = (float) Math.toRadians(90);
                modelPlayer.bipedRightArm.offsetY = 0.1f;
                modelPlayer.bipedRightArmwear.offsetY = 0.1f;

                /*modelPlayer.bipedLeftArm.rotateAngleX = (float) Math.toRadians(40 +  (-Helpers.getYaw(player) + -control.getAttack().getRotation().y));
                modelPlayer.bipedLeftArm.rotateAngleY = (float) Math.toRadians(-10);
                modelPlayer.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(80);
                modelPlayer.bipedLeftArm.offsetZ = -((float) Math.cos(modelPlayer.bipedRightArm.rotateAngleX) * 0.4f);*/
         /*   }
            else if(attack.getSwingTypes() == SwingTypes.LATERAL_LEF)
            {
                modelPlayer.bipedRightArm.rotateAngleX = (float) Math.toRadians(-Helpers.getYaw(player) + -control.getAttack().getRotation().y);
                modelPlayer.bipedRightArm.rotateAngleY = (float) Math.toRadians(-10);
                modelPlayer.bipedRightArm.rotateAngleZ = (float) Math.toRadians(90);
                modelPlayer.bipedRightArm.offsetY = 0.1f;
                modelPlayer.bipedRightArmwear.offsetY = 0.1f;

            }
            else if(attack.getSwingTypes() == SwingTypes.BY_UP_RIGHT)
            {
                modelPlayer.bipedRightArm.rotateAngleX = (float) Math.toRadians(-90 + (Math.sin(-control.getAttack().getPosition().y) * 130));
                modelPlayer.bipedRightArm.rotateAngleY = (float) Math.toRadians( 0);
                modelPlayer.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0);
                modelPlayer.bipedRightArm.offsetY = 0.1f;
                modelPlayer.bipedRightArmwear.offsetY = 0.1f;

            }
            return;
        }*/

        Animation animation = AnimationUtils.getPlayerHandledAnimation(e.getEntityPlayer());
        animation.renderAnimation(e);


        float swingProgress = player.prevSwingProgress + (player.swingProgress - player.prevSwingProgress) * e.getPartialTicks();
        swingProgress *= 2;
        //System.out.println(swingProgress);
        EnumAttackType attackType = EnumAttackType.getAttackType(e.getEntityPlayer());
        if (attackType == EnumAttackType.THRUST && !player.isActiveItemStackBlocking())
        {
            modelPlayer.bipedRightArm.rotateAngleX = 0.2f + (player.swingProgress > 1.5 ? -(-1.5f - swingProgress) : -swingProgress);
            modelPlayer.bipedRightArm.rotateAngleY = 0;
            modelPlayer.bipedRightArm.rotateAngleZ = 0;
        }
        ItemStack activeStack = e.getEntityPlayer().getActiveItemStack();
        if (activeStack.getItem().getItemUseAction(activeStack) == EnumAction.EAT || activeStack.getItem().getItemUseAction(activeStack) == EnumAction.DRINK)
        {
            e.getModelPlayer().bipedRightArm.rotateAngleZ -= 0.4;
            e.getModelPlayer().bipedRightArm.rotateAngleY -= 0.7;
            e.getModelPlayer().bipedRightArm.rotateAngleX -= 0.65;
            e.getModelPlayer().bipedRightArm.rotateAngleX += e.getEntityPlayer().getItemInUseCount() * 0.01;

        }

        ArmPoses poses = Keldaria.getInstance().getArmPoses();
        if (poses.hasPose(e.getEntityPlayer()))
        {
            Arms arms = poses.getPose(e.getEntityPlayer());
            arms.getRightArm().apply(e.getEntityPlayer(), modelPlayer.bipedRightArm, true);
            arms.getLeftArm().apply(e.getEntityPlayer(), modelPlayer.bipedLeftArm, false);
        }

        if (mc.player == e.getEntity())
        {
            if (mc.currentScreen instanceof GuiFrame.APIGuiScreen)
            {
                GuiFrame screen = ((GuiFrame.APIGuiScreen) mc.currentScreen).getFrame();
                if (screen instanceof GuiArmsChooser)
                {
                    Arms arms = ((GuiArmsChooser) screen).getToAdd();
                    arms.getRightArm().apply(mc.player, modelPlayer.bipedRightArm, true);
                    arms.getLeftArm().apply(mc.player, modelPlayer.bipedLeftArm, false);
                }
            }
        }


    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            this.timer++;
            if (this.timer > 20)
            {
                this.timer = 0;
                this.frame++;
                if (this.frame > 3) this.frame = 0;
            }
        }

    }

    @SubscribeEvent
    public void onPlayerRenderEvent(RenderLivingEvent.Pre e)
    {


        if (e.getEntity() instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer player = (AbstractClientPlayer) e.getEntity();
            EntityLivingBase entity = MorphingManager.getMorphAsEntity(player, true);
            if (entity != null)
            {
                GlStateManager.pushMatrix();
                //GlStateManager.rotate(-entityIn.rotationYaw, 0, 1,  0);
                GlStateManager.translate(e.getX(), e.getY(), e.getZ());
                //mc.getTextureManager().bindTexture(new ResourceLocation("textures/entity/sheep/sheep.png"));
                entity.setPosition(player.posX, player.posY, player.posZ);
                entity.prevPosX = player.posX;
                entity.prevPosY = player.posY;
                entity.prevPosZ = player.posZ;
                e.setCanceled(true);

                entity.renderYawOffset = player.renderYawOffset;
                entity.prevRenderYawOffset = player.prevRenderYawOffset;
                entity.prevRotationYaw = player.prevRotationYaw;
                entity.rotationYaw = player.rotationYaw;
                entity.prevRotationYawHead = player.prevRotationYawHead;
                entity.rotationYawHead = player.rotationYawHead;
                entity.prevRotationPitch = player.prevRotationPitch;
                entity.rotationPitch = player.rotationPitch;
                entity.limbSwing = player.limbSwing;
                entity.prevLimbSwingAmount = player.prevLimbSwingAmount;
                entity.limbSwingAmount = player.limbSwingAmount;
                entity.prevCameraPitch = player.prevCameraPitch;
                entity.cameraPitch = player.cameraPitch;
                entity.prevSwingProgress = player.prevSwingProgress;
                entity.swingProgress = player.swingProgress;
                entity.ticksExisted = player.ticksExisted;
                entity.onGround = player.onGround;
                Render render = mc.getRenderManager().getEntityRenderObject(entity);
                Render renderP = e.getRenderer();
                double scale = PlayerSizes.get(player);
                float playerShadowBase = 0.5f;
                float entityShadowBase = 0.5f;
                float entityRenderShadowBase = 1F;
                if (render != null)
                {
                    entityShadowBase = ObfuscationReflectionHelper.getPrivateValue(Render.class, render, 2);
                    ObfuscationReflectionHelper.setPrivateValue(Render.class, renderP, (float) (entityShadowBase * scale), 2);
                    entityRenderShadowBase = ObfuscationReflectionHelper.getPrivateValue(Render.class, render, 3);
                    ObfuscationReflectionHelper.setPrivateValue(Render.class, render, 0, 3);
                }
                GlStateManager.pushMatrix();
                GlStateManager.scale(scale, scale, scale);
                mc.getRenderManager().renderEntity(entity, 0, 0, 0, player.rotationYaw, e.getPartialRenderTick(), true);
                if (render != null)
                {
                    //ObfuscationReflectionHelper.setPrivateValue(Render.class, renderP, playerShadowBase, 2);
                    ObfuscationReflectionHelper.setPrivateValue(Render.class, render, entityRenderShadowBase, 3);
                }
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
            }

        }


        if (e.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            if (MorphingManager.isMorphed(player, true))
            {
                e.setCanceled(true);
                return;
            }
            GL11.glPushMatrix();
            if (e.getRenderer().getMainModel() instanceof ModelPlayer)
            {


                ModelPlayer modelPlayer = (ModelPlayer) e.getRenderer().getMainModel();


                modelPlayer.bipedHead.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.HEAD);
                modelPlayer.bipedHeadwear.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.HEAD);

                modelPlayer.bipedRightArm.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.RIGHT_ARM);
                modelPlayer.bipedRightArmwear.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.RIGHT_ARM);

                modelPlayer.bipedLeftArm.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.LEFT_ARM);
                modelPlayer.bipedLeftArmwear.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.LEFT_ARM);

                modelPlayer.bipedRightLeg.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.RIGHT_LEG);
                modelPlayer.bipedRightLegwear.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.RIGHT_LEG);

                modelPlayer.bipedLeftLeg.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.LEFT_LEG);
                modelPlayer.bipedLeftLegwear.showModel = !AmputationManager.isAmputated(player, AmputationManager.Member.LEFT_LEG);


            }
            double scale = PlayerSizes.get((EntityPlayer) e.getEntity());
            GL11.glScaled(scale, scale, scale);
            GL11.glTranslated(e.getX() / scale - e.getX(), e.getY() / scale - e.getY(), e.getZ() / scale - e.getZ());
        }

    }

    @SubscribeEvent
    public void onEntityRenderNametag(RenderLivingEvent.Specials.Pre e)
    {
        if (e.getEntity() instanceof EntityPlayer)
        {
            e.setCanceled(!this.mc.player.isCreative());
        }
    }

    @SubscribeEvent
    public void onPlayerRenderEvent(RenderLivingEvent.Post<EntityPlayer> e)
    {
        if (e.getEntity() instanceof EntityPlayer)
        {
            GL11.glPopMatrix();
            ObfuscationReflectionHelper.setPrivateValue(Render.class, e.getRenderer(), 0.5f, 2);

        }


    }

    public static final ResourceLocation CHAT_BUBBLE = new ResourceLocation(Keldaria.MOD_ID, "textures/chatbubbles/all.png");

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Post event)
    {

        if (mc.gameSettings.hideGUI) return;

        boolean flag = ChatBubblesManager.getInstance().isWriting(event.getEntityPlayer().getName());
        if (flag)
        {
            final float factor = 0.01f;
            double scale = PlayerSizes.get(event.getEntityPlayer());
            Animation animation = AnimationUtils.getPlayerHandledAnimation(event.getEntityPlayer());

            float size = (float) scale;

            GlStateManager.pushMatrix();
            GlStateManager.translate(event.getX(), event.getY() + ((event.getEntityPlayer().isSneaking() ? animation.getSneakingHeight(size) : animation.getHeight(size))), event.getZ());
            GlStateManager.glNormal3f(1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.scale(-factor * scale, -factor * scale, -factor * scale);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            mc.getRenderManager().renderEngine.bindTexture(CHAT_BUBBLE);

            GuiScreen.drawModalRectWithCustomSizedTexture(-25, -58, 0.0f, (this.frame * 50), 50, 50, 50, (float) (4 * 50));
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

    }

    public static final ResourceLocation COMPASS_TEXTURE_BG = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/compass_bg.png");
    public static final ResourceLocation COMPASS_TEXTURE = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/compass.png");

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event)
    {
        /* Cancel armor render */
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR)
        {
            event.setCanceled(true);
        }


        /* Cancel debug render */
        if (event.getType() == RenderGameOverlayEvent.ElementType.DEBUG)
        {
            if (event.isCancelable())
            {
                event.setCanceled(!Helpers.isDebugEnable() && !mc.player.isCreative());
                if (event.isCanceled())
                {
                    RenderHelpers.drawScaledString(mc.fontRenderer, "§cKeldaria", 2, 2, 2, Color.WHITE.getRGB(), true);
                    mc.fontRenderer.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), 4, mc.fontRenderer.FONT_HEIGHT * 2, Color.WHITE.getRGB());
                }
            }
        }

        /* Cancel crosshair render */
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            if (event.isCancelable())
            {
                if (!Helpers.isDebugEnable())
                {
                    double skill = WeaponStat.getAttackStat(mc.player) != null ? WeaponStat.getAttackStat(mc.player).getLevel(mc.player) : 0;
                    if (skill < 15)
                    {
                        event.setCanceled(!mc.player.isCreative());
                    }
                }
            }

        }

        if (
                event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR ||
                        event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT ||
                        event.getType() == RenderGameOverlayEvent.ElementType.JUMPBAR ||
                        event.getType() == RenderGameOverlayEvent.ElementType.AIR ||
                        event.getType() == RenderGameOverlayEvent.ElementType.HEALTH ||
                        event.getType() == RenderGameOverlayEvent.ElementType.FOOD ||
                        event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
        {
            if (event.isCancelable())
            {
                if (!(mc.player.isCreative()) && !mc.player.isSpectator())
                {
                    event.setCanceled(true);
                }
            }
        }


    }

    public static final ResourceLocation HUD = new ResourceLocation("keldaria", "textures/gui/hud.png");
    public static final ResourceLocation HELM_BLUR = new ResourceLocation("keldaria", "textures/gui/helmblur.png");

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Text event)
    {
        renderHotbar(event.getResolution());
    }

    /**
     * Render Custom HUD
     */
    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.getResolution();

        /* Render Helm*/
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET)
        {
            Accessories acc = Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player);
            if (!Helpers.isDebugEnable() && (acc != null && acc.getArmor().isWearingHelm()))
            {
                ScaledResolution scaledRes = event.getResolution();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableAlpha();
                this.mc.getTextureManager().bindTexture(HELM_BLUR);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(0.0D, (double) scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
                bufferbuilder.pos((double) scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
                bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
                tessellator.draw();
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


            }
        }

        GlStateManager.pushMatrix();

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
        {
            renderHotbar(event.getResolution());
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {

            //renderHotbar(event.getResolution());

            if (mc.currentScreen == null && (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow || mc.player.getHeldItemOffhand().getItem() instanceof ItemBow) && Keldaria.getInstance().getSyncedAccessories() != null && Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player) != null && Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player).getInventory() != null)
            {
                GlStateManager.pushMatrix();
                ItemStack quiver = Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player).getInventory().getAccessory(EntityEquipmentSlot.CHEST);
                if (quiver.getItem() instanceof ItemQuiver)
                {
                    Gui.drawRect(0, resolution.getScaledHeight() - 22 - mc.fontRenderer.FONT_HEIGHT, 80, resolution.getScaledHeight() - 2, new Color(0, 0, 0, 100).getRGB());
                    mc.fontRenderer.drawStringWithShadow("Carquois: [" + ClientProxy.KEY_CHEST_ACCESSORY.getDisplayName() + "]", 1, resolution.getScaledHeight() - 20 - mc.fontRenderer.FONT_HEIGHT, Color.WHITE.getRGB());
                    InventoryQuiver inventoryQuiver = new InventoryQuiver(mc.player, quiver);
                    for (int i = 0; i < inventoryQuiver.getSizeInventory(); i++)
                    {
                        GuiAPIClientHelper.drawItemStack(inventoryQuiver.getStackInSlot(i), 1 + 16 * i, resolution.getScaledHeight() - 20);
                    }
                }
                GlStateManager.popMatrix();
            }
            if (mc.currentScreen == null && (mc.player.getHeldItemMainhand().getItem() == KeldariaItems.COMPASS || mc.player.getHeldItemOffhand().getItem() == KeldariaItems.COMPASS))
            {
                float yaw = mc.player.rotationYaw;
                while (yaw > 360) yaw -= 360;
                while (yaw < 0) yaw += 360;
                GlStateManager.pushMatrix();
                if (mc.player.getHeldItemMainhand().getItem() == KeldariaItems.COMPASS)
                    GlStateManager.translate(resolution.getScaledWidth() - (100 * 1.3), 100 + resolution.getScaledHeight() - mc.player.getCooledAttackStrength(0) * 100, 0);
                else
                    GlStateManager.translate(0, 100 + resolution.getScaledHeight() - mc.player.getCooledAttackStrength(0) * 100, 0);
                GlStateManager.scale(1.3, 1.3, 1.3);
                RenderHelpers.drawImage(0, -100, COMPASS_TEXTURE_BG, 100, 100);
                GlStateManager.pushMatrix();
                GlStateManager.translate(50, -50, 0);
                GlStateManager.rotate(-yaw, 0, 0, 1);
                GlStateManager.rotate(180, 0, 0, 1);
                RenderHelpers.drawImage(-50, -50, COMPASS_TEXTURE, 100, 100);
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();

            }
            RayTraceResult result = mc.objectMouseOver;


            {
                Entity entity = MixinClientHooks.getOverItem(event.getPartialTicks());
                this.itemEntityOverMouse = entity;

                if (entity instanceof EntityItem /*&&
                        entity.getDistance(mc.player) < mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() / 2.2*/)
                {
                    EntityItem item = (EntityItem) entity;
                    RenderHelpers.drawCenteredString(
                            mc.fontRenderer, "[Ramasser: §f" + item.getItem().getDisplayName() + "§r]",
                            event.getResolution().getScaledWidth() / 2, event.getResolution().getScaledHeight() / 2,
                            Color.GRAY.getRGB()
                    );
                }

            }

            if (Helpers.isDebugEnable() && result != null)
            {
                Entity entity = result.entityHit;
                if (entity instanceof EntityLivingBase)
                {
                    List<String> infos = Lists.newArrayList();
                    EntityLivingBase living = (EntityLivingBase) entity;
                    {
                        int life = (int) Helpers.getPercent(living.getHealth(), living.getMaxHealth());
                        infos.add("§c♥ " + (life) + "%");
                    }
                    infos.add(" ");
                    infos.add(" ");
                    int maxWidth = 0;
                    for (String info : infos)
                    {
                        maxWidth = Math.max(maxWidth, mc.fontRenderer.getStringWidth(info) + 2);
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(resolution.getScaledWidth() / 2 - maxWidth / 2, resolution.getScaledHeight() / 2 - ((infos.size() * mc.fontRenderer.FONT_HEIGHT) / 2), 0);
                    GlStateManager.scale(1.5, 1.5, 1.5);
                    //Gui.drawRect(30, 0,30 + maxWidth + 4, mc.fontRenderer.FONT_HEIGHT * infos.size(), new Color(0, 0, 0, 100).getRGB());
                    for (int i = 0; i < infos.size(); i++)
                    {
                        mc.fontRenderer.drawStringWithShadow(infos.get(i), 0, i * infos.size(), Color.WHITE.getRGB());
                    }
                    GlStateManager.popMatrix();

                }
            }
            if (result != null)
            {
                if (result.entityHit instanceof EntityKeldAnimal)
                {
                    EntityKeldAnimal animal = (EntityKeldAnimal) result.entityHit;
                    List<String> infos = Lists.newArrayList();
                    infos.add(String.format("§n%s", animal.getName()));
                    infos.add(" ");
                    infos.addAll(animal.getHoverInfos(mc.player));

                    int maxWidth = 0;
                    for (String info : infos)
                        maxWidth = Math.max(maxWidth, mc.fontRenderer.getStringWidth(info) + 10);
                    Gui.drawRect((int) (resolution.getScaledWidth() - maxWidth - 2), resolution.getScaledHeight() / 2 - 60, (int) (resolution.getScaledWidth()), resolution.getScaledHeight() / 2 + 10, new Color(0, 0, 0, 100).getRGB());
                    for (int i = 0; i < infos.size(); i++)
                        mc.fontRenderer.drawStringWithShadow(infos.get(i), resolution.getScaledWidth() - maxWidth, resolution.getScaledHeight() / 2 - 55 + (i * mc.fontRenderer.FONT_HEIGHT), Color.WHITE.getRGB());
                    
                } else if (result.entityHit instanceof AbstractHorse || (mc.player.isRiding() && mc.player.getRidingEntity() instanceof AbstractHorse))
                {
                    AbstractHorse horse = result.entityHit instanceof AbstractHorse ? (AbstractHorse) result.entityHit : (AbstractHorse) mc.player.getRidingEntity();
                    ILivingHorse livingHorse = (ILivingHorse) horse;
                    List<String> infos = Lists.newArrayList();
                    infos.add(String.format("§n%s", horse.getName()));
                    infos.add(" ");
                    infos.add(String.format("§6Faim: §e%s", (int) livingHorse.getFood() + "%"));
                    infos.add(String.format("§1Soif: §9%s", (int) livingHorse.getThirst() + "%"));
                    infos.add(String.format("§3Propreté: §b%s", (int) livingHorse.getCleaniless() + "%"));
                    AnimalGender gender = livingHorse.getGender();
                    infos.add(String.format("§4Sexe: §c%s", gender.getSymbol() + " " + (gender == AnimalGender.MALE ? "Mâle" : "Femelle")));

                    if (mc.player.isRidingSameEntity(horse))
                    {
                        infos.add(String.format("§4Santé:§c %s", (int) Helpers.getPercent(horse.getHealth(), horse.getMaxHealth()) + "%"));
                    }

                    int maxWidth = 0;
                    for (String info : infos)
                    {
                        maxWidth = Math.max(maxWidth, mc.fontRenderer.getStringWidth(info) + 2);
                    }
                    Gui.drawRect((int) (resolution.getScaledWidth() - maxWidth - 2), resolution.getScaledHeight() / 2 - 60, (int) (resolution.getScaledWidth()), resolution.getScaledHeight() / 2 + 10, new Color(0, 0, 0, 100).getRGB());
                    for (int i = 0; i < infos.size(); i++)
                    {
                        mc.fontRenderer.drawStringWithShadow(infos.get(i), resolution.getScaledWidth() - maxWidth, resolution.getScaledHeight() / 2 - 55 + (i * mc.fontRenderer.FONT_HEIGHT), Color.WHITE.getRGB());
                    }
                }
            }
            if (EnumJob.PEASANT.has(mc.player))
            {
                //RayTraceResult result = mc.player.rayTrace(mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue(), event.getPartialTicks());
                if (result != null)
                {
                    BlockPos pos = result.getBlockPos();
                    if (pos != null)
                    {
                        TileEntity te = mc.world.getTileEntity(pos);
                        if (te instanceof TileEntityFloweringPot)
                        {
                            TileEntityFloweringPot pot = (TileEntityFloweringPot) te;
                            if (pot.isActive())
                            {
                                double growTime = pot.getFlowerSeed().getGrowTime();
                                long diff = (System.currentTimeMillis() - pot.getFlowerPlantedTime());
                                int percent = (int) Math.min((diff / growTime) * 100, 100);
                                String str = pot.getFlowerSeed().getFormattedName() + "... " + percent + "%";
                                Gui.drawRect((int) (resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(str) / 1.5), resolution.getScaledHeight() / 2 - 60, (int) (resolution.getScaledWidth() / 2 + mc.fontRenderer.getStringWidth(str) / 1.5), resolution.getScaledHeight() / 2 + 20, new Color(0, 0, 0, 100).getRGB());
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(resolution.getScaledWidth() / 2 - 24, resolution.getScaledHeight() / 2 - 60, 1);
                                GlStateManager.scale(3, 3, 3);
                                RenderHelpers.renderItemStack(new ItemStack(pot.getFlowerSeed().getItem()), 0, 0, event.getPartialTicks(), true);
                                GlStateManager.popMatrix();
                                mc.fontRenderer.drawStringWithShadow(str, resolution.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(str) / 2, resolution.getScaledHeight() / 2 - mc.fontRenderer.FONT_HEIGHT / 2, Color.WHITE.getRGB());

                            }
                        }
                    }
                }
            }
            final int[] y = new int[]{0};
            PopMessages.getMessagesToDisplay().forEach(string ->
            {
                Gui.drawRect(0, y[0], 0 + mc.fontRenderer.getStringWidth(string), y[0] + mc.fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 100).getRGB());
                mc.fontRenderer.drawStringWithShadow(string, 0, y[0], Color.WHITE.getRGB());
                y[0] += mc.fontRenderer.FONT_HEIGHT;
            });
            /**
             * See affutage
             */
            if (SharpeningHelpers.canBeSharped(mc.player.getHeldItemMainhand()))
            {
                ItemStack sword = mc.player.getHeldItemMainhand();
                if (SharpeningHelpers.hasSharpness(sword))
                {
                    int sharpeningValue = SharpeningHelpers.getSharpness(sword);
                    String color = sharpeningValue < 500 ? "§4" : sharpeningValue < 1000 ? "§c" : sharpeningValue < 1500 ? "§6" : sharpeningValue < 2000 ? "§a" : "§2";
                    String total = color + "Affûtage: " + (sharpeningValue * 100 / SharpeningHelpers.MAX_SHARPENING) + "%";
                    mc.fontRenderer.drawStringWithShadow(total, event.getResolution().getScaledWidth() - mc.fontRenderer.getStringWidth(total) - 10, event.getResolution().getScaledHeight() - mc.fontRenderer.FONT_HEIGHT * 2, Color.WHITE.getRGB());
                }
            }
        }

        GlStateManager.popMatrix();


        GlStateManager.color(1, 1, 1, 0);
    }


    public void renderHotbar(ScaledResolution res)
    {
        GlStateManager.pushMatrix();


        //GlStateManager.translate(10, 0, 0);

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        boolean creative = mc.player.isCreative() || mc.player.isSpectator();

        GlStateManager.translate(res.getScaledWidth() / 2, res.getScaledHeight(), 0);
        GlStateManager.scale(0.8, 0.8, 1);
        GlStateManager.translate(-178 / 2, -57, 0);
        GlStateManager.translate(0, 0, -200);


        if (!creative)
        {
            mc.getTextureManager().bindTexture(HUD);

            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 74, 29, 29, 256, 256);

            GlStateManager.pushMatrix();
            GlStateManager.translate(16.5, 0, 0);
            if (mc.player != null)
            {
                GuiInventory.drawEntityOnScreen(0, 54, 25, 1, 1, mc.player);
                //GlStateManager.disableColorMaterial();
            }
            GlStateManager.popMatrix();
            mc.getTextureManager().bindTexture(HUD);

            GlStateManager.translate(0, 0, 100);

            if (KeldariaClientHandler.grabMoment > 0 || KeldariaClientHandler.grabMoment < -1)
            {
                Gui.drawModalRectWithCustomSizedTexture(30, -2, 0, 105, 141, 17, 256, 256);
            }

            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 211, 56, 256, 256);

            Gui.drawModalRectWithCustomSizedTexture(5 + mc.player.inventory.currentItem * 19, 35, 0, 56, 18, 18, 256, 256);

            {
                int barSize = 54;
                int barHeight = 5;

                int health = Math.min(Helpers.crossMult(mc.player.getHealth(), mc.player.getMaxHealth(), barSize), barSize);
                RenderHelpers.fillRect(44, 26, health, 3, new Color(150, 0, 0).getRGB());

                int hunger = Helpers.crossMult(mc.player.getFoodStats().getFoodLevel(), 20, barSize);
                RenderHelpers.fillRect(44, 18, hunger, 3, Color.decode("#602A17".toLowerCase()).getRGB());

                Thirst thirstData = Thirst.getPersonnalThirst();
                int thirst = Helpers.crossMult(thirstData.getThirstLevel(), 20, barSize);
                RenderHelpers.fillRect(44, 10, thirst, 3, Color.decode("#114468".toLowerCase()).getRGB());

                int air = Math.max(Helpers.crossMult(mc.player.getAir(), 300, barSize), 0);
                RenderHelpers.fillRect(119, 10, air, 3, Color.decode("#58ACBC".toLowerCase()).getRGB());

                if (KeldariaClientHandler.grabMoment > 0)
                {
                    RenderHelpers.fillRect(34, 1, (int) (KeldariaClientHandler.grabMoment * 133 / KeldariaClientHandler.defaultGrabMoment), 3, Color.decode("#E5C662").getRGB());
                } else if (KeldariaClientHandler.grabMoment < -1)
                {
                    RenderHelpers.fillRect(34, 1, (int) ((KeldariaClientHandler.defaultGrabMoment - KeldariaClientHandler.grabMoment) * 133 / KeldariaClientHandler.defaultGrabMoment), 3, Color.decode("#E5C662").getRGB());
                }
//417263
            }

            for (int i = 0; i < 9; i++)
            {
                GuiAPIClientHelper.drawItemStack(mc.player.inventory.mainInventory.get(i), 6 + (i * 19), 36);
            }

            GlStateManager.pushMatrix();
            //GlStateManager.translate(res.getScaledWidth() / 2 + 100, res.getScaledHeight() - 25, 0);
            GlStateManager.translate(180, 20, 0);
            GlStateManager.scale(0.8, 0.8, 0.8);
            EnumAttackType attackType = EnumAttackType.getAttackType(mc.player);
            RenderHelpers.drawImage(0, 0, attackType == EnumAttackType.HIT ? GuiArmorInventory.HIT_TEXTURE : attackType == EnumAttackType.SHARP ? GuiArmorInventory.SHARP_TEXTURE : GuiArmorInventory.THRUST_TEXTURE, 32, 32);
            //Gui.drawRect(-16 - 5, -16 - 5, 16 + 5, 16 + 5, new Color(0, 0, 0, 100).getRGB());
            String strr = "" + attackType.getFormattedName();
            RenderHelpers.drawScaledString(mc.fontRenderer, strr, 0, 32, (float) 0.8, Color.WHITE.getRGB(), true);
            GlStateManager.popMatrix();

        }


        Gui.drawRect(6, -10, 6, -8, new Color(0, 0, 0, 0).getRGB());


        GlStateManager.popMatrix();


    }

    /**
     * Used for replace vanilla skin by the CustomSkin
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderPlayer(RenderPlayerEvent.Pre e)
    {

        try
        {
            Minecraft.getMinecraft().player.setSneaking(true);
            if (!(e.getEntityPlayer() instanceof AbstractClientPlayer)) return;
            AbstractClientPlayer player = (AbstractClientPlayer) e.getEntityPlayer();
            if (Minecraft.getMinecraft().getConnection() == null) return;
            NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID());
            Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, "playerTextures", "field_187107_a");
            if (playerTextures != null)
            {
                playerTextures.put(MinecraftProfileTexture.Type.SKIN, CustomSkins.getSkin(player));
                ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, playerTextures, "playerTextures", "field_187107_a");
            }
        } catch (Exception ignored)
        {
        }
    }

    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.PostText event)
    {
        if (event.getStack().getItem() instanceof ItemCard)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(event.getX() - 60, event.getY() - 10, 1);
            renderToolTipImage(ImageCache.getAsResourceLocation(ItemCard.getCardLink(event.getStack()), ImageCache.getBlank()));
            GlStateManager.popMatrix();
        }
        if (event.getStack().getItem() instanceof ItemClothe || event.getStack().getItem() instanceof ItemArmor || event.getStack().getItem() instanceof ItemArmorPart || event.getStack().getItem() instanceof ItemAccessory)
        {
            if (ItemClothe.isClothValid(event.getStack()))
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(event.getX() - 60, event.getY() - 10, 1);
                renderToolTipImage(ImageCache.getAsResourceLocation(ItemClothe.getClothURL(event.getStack()), ImageCache.getBlank()));
                GlStateManager.popMatrix();
            }

        }

    }

    public static void renderToolTipImage(ResourceLocation loc)
    {
        try
        {
            Gui.drawRect(-4, -4, 54, 54, 0xF0100010);
            Gui.drawRect(-4, -4, 54, -3, 0x505000FF);
            Gui.drawRect(-4, -5, 54, -4, 0xF0100010);
            Gui.drawRect(-4, -3, -3, 54, 0x505000FF);
            Gui.drawRect(-5, -4, -4, 54, 0xF0100010);
            Gui.drawRect(-3, 53, 54, 54, 0x505000FF);
            Gui.drawRect(-4, 54, 54, 55, 0xF0100010);
            Gui.drawRect(53, -3, 54, 53, 0x505000FF);
            Gui.drawRect(54, -4, 55, 53, 0xF0100010);

            RenderHelpers.drawImage(0, 0, loc, 50, 50);
        } catch (Exception ignored)
        {
        }
    }


    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemFood)
        {
            if (FoodQuality.isDefined(stack))
            {
                event.getToolTip().add("§7Qualité: §f" + FoodQuality.getQuality(stack).getFormattedName());
            }
            event.getToolTip().add("§7Périmé à §f" + ExpiredFoods.getRot(stack).getRotPercent(stack) + "%");
            event.getToolTip().add("§7Etat: " + (ExpiredFoods.isExpired(stack) ? "§cPérimé" : "§aValable"));
        } else if (SharpeningHelpers.canBeSharped(stack))
        {
            event.getToolTip().add("§bAffûtage: " + ((float) (SharpeningHelpers.getSharpness(stack) * 100 / SharpeningHelpers.MAX_SHARPENING)) + "%");
        } else if (stack.getItem() == KeldariaItems.FLOUR)
        {
            event.getToolTip().add("§6200g§e de farine");
        } else if (stack.getItem() == KeldariaItems.SUNFLOWER_OIL)
        {
            event.getToolTip().add("§6500ml§e d'huile");
        } else if (stack.getItem() == KeldariaItems.COMPASS)
        {
            event.getToolTip().add("§6Une boussole permettant de ne pas perdre le Nord.");
            event.getToolTip().add("§7Pour l'utiliser, prenez la dans votre main.");
        } else if (stack.getItem() == KeldariaItems.PURSE)
        {
            event.getToolTip().add("§7La bourse permet de stocker des écus en quantité.");
        } else if (stack.getItem() == KeldariaItems.KEYRING)
        {

            event.getToolTip().add("§7Le cadenas permet de stocker des clefs, et d'ouvrir");
            event.getToolTip().add("§7une porte fermée en un simple click si la clef de");
            event.getToolTip().add("§7la porte en question est contenue dedans.");
        } else if (stack.getItem() == KeldariaItems.DICE)
        {
            event.getToolTip().add("§6Droppez le dé pour l'utiliser.");
        } else if (stack.getItem() == KeldariaItems.QUIVER)
        {
            event.getToolTip().add("§7Ce carquois contient: ");
            event.getToolTip().add("§f" + (ItemQuiver.isEmpty(mc.player, stack) ? "Vide" : ItemQuiver.getArrowCount(mc.player, stack) + " flèches"));
        } else if (stack.getItem() instanceof ItemWineBottle || stack.getItem() instanceof ItemWineGlass)
        {
            Wine wine = Wine.getWine(stack);

            event.getToolTip().add("§7Qualité: §f" + wine.getQualityString() + "§7 (" + wine.getQuality() + "/100)");
            event.getToolTip().add("§7Créé depuis: §f" + wine.getDaysSinceCreation() + " jours");
        } else if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool)
        {

        }

        IKnownRecipes recipes = stack.getCapability(KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES, null);
        if (recipes != null && recipes.getKnownKeys().size() > 0)
        {
            event.getToolTip().add("§eCe livre contient des recettes.");
            event.getToolTip().add("§7(Pour y avoir accès, mettez");
            event.getToolTip().add("§7cet item dans votre deuxième");
            event.getToolTip().add("§7main en ouvrant le menu de craft)");
        }

        if (CommandLore.isSealed(event.getItemStack()))
        {
            event.getToolTip().add("§eLe lore est scellé par §c" + CommandLore.getSealOwner(event.getItemStack()));
        }
    }

    private float fogAlpha = 0F;

    @SubscribeEvent
    public void onRenderFog(EntityViewRenderEvent.FogDensity event)
    {

        EntityPlayer player = mc.player;
        try
        {
            if (Keldaria.getInstance().getSyncedAccessories() != null && Keldaria.getInstance().getSyncedAccessories().getAccessories(player) != null && !Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player).getInventory().canSee())
            {
                GlStateManager.setFog(GlStateManager.FogMode.EXP);
                event.setDensity(10000);

                event.setCanceled(true);
            } else /*if (mc.world.isRaining())*/
            {
                /*boolean isRainingAtPlayer = player.world.isRainingAt(new BlockPos(player.posX, player.posY + 2, player.posZ));
                GlStateManager.setFog(GlStateManager.FogMode.EXP);

                float needed = 0;
                if (mc.world.isThundering())
                {
                    needed = isRainingAtPlayer ? 0.1f : 0.04f;
                    //event.setDensity(isRainingAtPlayer ? 0.2f : 0.04f);
                } else
                {
                    needed = isRainingAtPlayer ? 0.01f : 0.005f;
                    //event.setDensity(isRainingAtPlayer ? 0.02f : 0.01f);
                }
                //this.fogAlpha = Math.max(0.1f)

                if(this.fogAlpha != needed)
                {
                    if(this.fogAlpha > needed)
                        this.fogAlpha -= 0.0001f;
                    else
                        this.fogAlpha += 0.0001f;
                }
                event.setDensity(this.fogAlpha);

                event.setCanceled(true);*/


                RPZone zone = Zones.getZone(player);
                Atmosphere atmosphere = zone.getAtmosphere(mc.world);
                double density = atmosphere.getFogDensity();
                if (density != -1)
                {
                    float needed = (float) density;

                    if (this.fogAlpha != needed)
                    {
                        if (this.fogAlpha > needed)
                            this.fogAlpha -= 0.0001f;
                        else
                            this.fogAlpha += 0.0001f;
                    }

                    GlStateManager.setFog(GlStateManager.FogMode.EXP);

                    event.setDensity(this.fogAlpha);
                    event.setCanceled(true);
                } else
                {
                    if (this.fogAlpha > 0)
                    {
                        this.fogAlpha -= 0.0001f;
                        GlStateManager.setFog(GlStateManager.FogMode.EXP);

                        event.setDensity(this.fogAlpha);
                        event.setCanceled(true);
                    }
                }
            }

        } catch (Exception ignored)
        {
        }
    }

    @SubscribeEvent
    public void onFovModifier(EntityViewRenderEvent.FOVModifier event)
    {
    }


    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event)
    {
        ItemStack active = Minecraft.getMinecraft().player.getActiveItemStack();
        if (mc.player.isActiveItemStackBlocking())
        {
            if (active.getItem() != Items.SHIELD || /*SkinNBTHelper.stackHasSkinData(active) || */ItemTextures.hasTexture(active))
            {
                if (event.getHand() == EnumHand.MAIN_HAND && mc.player.getActiveHand() == EnumHand.MAIN_HAND)
                {
                    GlStateManager.rotate(mc.player.getPrimaryHand() == EnumHandSide.RIGHT ? 30 : -30, 0, 1, 0);
                    GlStateManager.rotate((mc.player.getPrimaryHand() == EnumHandSide.RIGHT ? -mc.player.rotationPitch : mc.player.rotationPitch) / 3, 0, 0, 1);
                } else if (event.getHand() == EnumHand.OFF_HAND && mc.player.getActiveHand() == EnumHand.OFF_HAND)
                {
                    GlStateManager.rotate(mc.player.getPrimaryHand() == EnumHandSide.RIGHT ? -30 : 30, 0, 1, 0);
                    GlStateManager.rotate((mc.player.getPrimaryHand() == EnumHandSide.RIGHT ? mc.player.rotationPitch : -mc.player.rotationPitch) / 3, 0, 0, 1);
                }
            }
        } else
        {
            if (event.getHand() == EnumHand.MAIN_HAND)
            {
                if (EnumAttackType.getAttackType(mc.player) == EnumAttackType.THRUST)
                {
                    GlStateManager.rotate(-90, 1, 0, 0);
                    GlStateManager.translate(0, 1, 0);
                    GlStateManager.translate(0, 0, 0.2);

                }
            }

        }
        if (event.getItemStack().getItem() == KeldariaItems.COMPASS)
        {
            event.setCanceled(true);
        }
        //event.setCanceled(true);
    }


    private float defaultGammaSetting;

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        boolean millicieMode = ClientDatabases.getPersonalPlayerData().getInteger("IsMillicieMode") == 1;
        if (event.phase == TickEvent.Phase.START && !millicieMode)
        {
            defaultGammaSetting = mc.gameSettings.gammaSetting;
            mc.gameSettings.gammaSetting = -0.46f;
        } else
        {
            mc.gameSettings.gammaSetting = defaultGammaSetting;
        }

    }

    private Vec3d actualFogColor;

    @SubscribeEvent
    public void onFogColors(EntityViewRenderEvent.FogColors event)
    {
        try
        {
            if (!Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player).getInventory().canSee())
            {
                event.setRed((float) 0);
                event.setGreen((float) 0);
                event.setBlue((float) 0);
            } else if (SkyColor.isFogColorCustom(true))
            {
                Vec3d fogColor = SkyColor.getFogColor(true);
                event.setRed((float) fogColor.x);
                event.setGreen((float) fogColor.y);
                event.setBlue((float) fogColor.z);
            } else
            {
                if (actualFogColor == null)
                {
                    actualFogColor = new Vec3d(event.getRed(), event.getGreen(), event.getBlue());
                }
                RPZone zone = Zones.getZone(mc.player);
                Atmosphere atmosphere = zone.getAtmosphere(mc.world);
                Atmosphere.RGB fogColor = atmosphere.getFogColor();

                Vec3d needed = new Vec3d(event.getRed(), event.getGreen(), event.getBlue());

                if (fogColor.hasColor())
                {
                    if (fogColor.isAdditive())
                    {
                        needed = needed.add(fogColor.asVec());
                        //needed = new Vec3d(event.getRed() + fogColor.getRed(), event.getGreen() + fogColor.getGreen(), event.getBlue() + fogColor.getBlue());
                    } else
                    {
                        needed = fogColor.asVec();
                    }
                }

                this.actualFogColor = Helpers.processNormalizeColor(this.actualFogColor, needed, 0.001);

                event.setRed((float) this.actualFogColor.x);
                event.setGreen((float) this.actualFogColor.y);
                event.setBlue((float) this.actualFogColor.z);
            }
        } catch (Exception ignored)
        {
        }
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickEmpty event)
    {
    }


    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        Entity rve = mc.getRenderViewEntity();
        if (rve != null && mc.gameSettings.showDebugInfo)
        {
            final double interpX = rve.prevPosX + (rve.posX - rve.prevPosX) * event.getPartialTicks();
            final double interpY = rve.prevPosY + (rve.posY - rve.prevPosY) * event.getPartialTicks();
            final double interpZ = rve.prevPosZ + (rve.posZ - rve.prevPosZ) * event.getPartialTicks();

            DatabaseReadOnly db = ContainmentSystem.getReadDB(mc.player.world, Points.KEY);
            for (String chunkParts : db.getAllEntryNames())
            {
                String[] parts = chunkParts.split("/");
                if (parts.length == 2)
                {
                    int x = Helpers.parseOrZero(parts[0]) * 16;
                    int z = Helpers.parseOrZero(parts[1]) * 16;
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x - interpX + 8, (rve.prevPosY + (rve.posY - rve.prevPosY) * event.getPartialTicks()) - interpY + 8, z - interpZ + 8);
                    GlStateManager.rotate(-rve.rotationYaw, 0, 1, 0);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(-1, -1, -1);
                    GlStateManager.scale(0.3, 0.3, 0.3);
                    String text = db.getInteger(chunkParts) + " chunks";

                    mc.fontRenderer.drawStringWithShadow(text, -mc.fontRenderer.getStringWidth(text) / 2, +16, Color.WHITE.getRGB());

                    GlStateManager.popMatrix();

                    GlStateManager.scale(10, 10, 10);
                    mc.getItemRenderer().renderItem(mc.player, new ItemStack(Items.EMERALD), ItemCameraTransforms.TransformType.FIXED);

                    GlStateManager.popMatrix();
                }
            }

        }
    }


}