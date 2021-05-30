/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mojang.authlib.GameProfile;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.api.RenderBlockEvent;
import fr.nathanael2611.keldaria.mod.clothe.Clothes;
import fr.nathanael2611.keldaria.mod.features.SkyColor;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.item.ItemClothe;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import fr.nathanael2611.keldaria.mod.server.zone.Atmosphere;
import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MixinClientHooks
{

    public static Entity getOverItem(float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        Entity pointed = null;
        Entity realPointed = null;

        if (entity != null)
        {
            if (mc.world != null)
            {
                //mc.profiler.startSection("pick");
                //mc.pointedEntity = null;
                double d0 = (double) mc.playerController.getBlockReachDistance();
                RayTraceResult objectMouseOver = entity.rayTrace(d0, partialTicks);
                Vec3d vec3d = entity.getPositionEyes(partialTicks);
                boolean flag = false;
                int i = 3;
                double d1 = d0;

                if (mc.playerController.extendedReach())
                {
                    d1 = 6.0D;
                    d0 = d1;
                } else
                {
                    if (d0 > 3.0D)
                    {
                        flag = true;
                    }
                }

                if (objectMouseOver != null)
                {
                    d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
                }

                Vec3d vec3d1 = entity.getLook(1.0F);
                Vec3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                pointed = null;
                Vec3d vec3d3 = null;
                float f = 1.0F;
                List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(2, 1, 2).expand(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ instanceof EntityItem;
                    }
                }));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {

                    Entity entity1 = list.get(j);
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.15, 0.15, 0.15).grow((double) entity1.getCollisionBorderSize());
                    RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.contains(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            pointed = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    } else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    pointed = entity1;
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            } else
                            {
                                pointed = entity1;
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }

                if (pointed != null && flag && vec3d.distanceTo(vec3d3) > 3.0D)
                {
                    pointed = null;
                    objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null, new BlockPos(vec3d3));
                }

                if (pointed != null && (d2 < d1 || objectMouseOver == null))
                {
                    if (pointed instanceof EntityItem)
                    {
                        realPointed = pointed;
                    }
                }
            }
        }
        return realPointed;
    }

    public static void applyBobbing(float partialTicks, Minecraft mc)
    {
        /*if (mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            Entity walkingEntity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.getRenderViewEntity();
            EntityPlayer entityplayer = (EntityPlayer)mc.getRenderViewEntity();
            float f = walkingEntity.distanceWalkedModified - walkingEntity.prevDistanceWalkedModified;
            float f1 = -(walkingEntity.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float)Math.PI) * f2 * (walkingEntity.isSprinting() ? 1.2F : 0.8), -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * (walkingEntity.isSprinting() ? 12.0F : 6), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * (walkingEntity.isSprinting() ? 20.0F : 10), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3 * (walkingEntity.isSprinting() ? 6 : 3), 1.0F, 0.0F, 0.0F);
            if(entityplayer.isSprinting()) GlStateManager.rotate((entityplayer.moveStrafing * f) * 5, 0, 0, 1);
            if(entityplayer.moveStrafing > 0)
            {

            }
        }*/
        if (mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        }
    }

    public static boolean doesCancelArmorRender(ItemStack stack, EntityPlayer player, EntityEquipmentSlot slotIn)
    {
        Clothes clothes = Keldaria.getInstance().getClothesManager().getClothes(player.getName());
        if (slotIn == EntityEquipmentSlot.HEAD && (ItemClothe.isClothValid(stack) || clothes.getArmorOverrides()[0]))
        {
            return true;
        } else if (slotIn == EntityEquipmentSlot.CHEST && (ItemClothe.isClothValid(stack) || clothes.getArmorOverrides()[1]))
        {
            return true;
        } else if (slotIn == EntityEquipmentSlot.LEGS && (ItemClothe.isClothValid(stack) || clothes.getArmorOverrides()[2]))
        {
            return true;
        } else
        {
            return slotIn == EntityEquipmentSlot.FEET && (ItemClothe.isClothValid(stack) || clothes.getArmorOverrides()[3]);
        }


    }

    public static void clickMouse(Minecraft mc)
    {

    }


    public static boolean renderBlock(BlockRendererDispatcher dispatcher, BlockModelRenderer renderer, BlockFluidRenderer fluidRenderer, IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, boolean callEvent)
    {
        try
        {
            EnumBlockRenderType enumblockrendertype = state.getRenderType();

            if (enumblockrendertype == EnumBlockRenderType.INVISIBLE)
            {
                return false;
            } else
            {
                if (blockAccess.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
                {
                    try
                    {
                        state = state.getActualState(blockAccess, pos);
                    } catch (Exception var8)
                    {
                        ;
                    }
                }

                switch (enumblockrendertype)
                {
                    case MODEL:
                        RenderBlockEvent event = new RenderBlockEvent(dispatcher, pos, blockAccess, state, bufferBuilderIn, renderer, fluidRenderer);
                        MinecraftForge.EVENT_BUS.post(event);
                        IBakedModel model = dispatcher.getModelForState(event.getState());
                        state = state.getBlock().getExtendedState(state, blockAccess, pos);
                        /* Keldaria Start */
                        if (callEvent)
                        {
                            if (event.isCanceled())
                            {
                                return true;
                            }
                            return renderer.renderModel(blockAccess, model, event.getState(), pos, bufferBuilderIn, true);
                        }
                        /* Keldaria End */
                        return renderer.renderModel(blockAccess, model, state, pos, bufferBuilderIn, true);
                    case ENTITYBLOCK_ANIMATED:
                        return false;
                    case LIQUID:
                        return fluidRenderer.renderFluid(blockAccess, state, pos, bufferBuilderIn);
                    default:
                        return false;
                }

            }
        } catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
    }

    public static long lastJump = 0;
    public static long noJumpFor = 0;

    public static void processMovementInputs(MovementInputFromOptions movementInput, GameSettings gameSettings)
    {
        movementInput.moveStrafe = 0.0F;
        movementInput.moveForward = 0.0F;

        if (gameSettings.keyBindForward.isKeyDown())
        {
            ++movementInput.moveForward;
            movementInput.forwardKeyDown = true;
        } else
        {
            movementInput.forwardKeyDown = false;
        }

        if (gameSettings.keyBindBack.isKeyDown())
        {
            --movementInput.moveForward;
            movementInput.backKeyDown = true;
        } else
        {
            movementInput.backKeyDown = false;
        }

        if (gameSettings.keyBindLeft.isKeyDown())
        {
            ++movementInput.moveStrafe;
            movementInput.leftKeyDown = true;
        } else
        {
            movementInput.leftKeyDown = false;
        }

        if (gameSettings.keyBindRight.isKeyDown())
        {
            --movementInput.moveStrafe;
            movementInput.rightKeyDown = true;
        } else
        {
            movementInput.rightKeyDown = false;
        }

        EntityPlayerSP p = Minecraft.getMinecraft().player;

        if ((Helpers.isDebugEnable() || p.isCreative()))
        {
            movementInput.jump = gameSettings.keyBindJump.isKeyDown();
        } else
        {
            double motX = p.motionX < 0 ? -p.motionX : p.motionX;
            double motZ = p.motionZ < 0 ? -p.motionZ : p.motionZ;
            if (((motX + motZ < 0.1) || System.currentTimeMillis() - lastJump > noJumpFor))
            {
                movementInput.jump = gameSettings.keyBindJump.isKeyDown();
                noJumpFor = 0;
            } else
            {
                //movementInput.jump = false;
            }
        }
        movementInput.sneak = gameSettings.keyBindSneak.isKeyDown();

        if (movementInput.sneak)
        {
            movementInput.moveStrafe = (float) ((double) movementInput.moveStrafe * 0.3D);
            movementInput.moveForward = (float) ((double) movementInput.moveForward * 0.3D);
        }
    }


    private static NetworkManager networkManager;

    public static void startGuiConnectingThread(String ip, int port, GuiScreen previous, GuiConnecting gui, NetworkManager net, AtomicInteger connectionId)
    {
        networkManager = net;
        (new Thread("Server Connector #" + connectionId.incrementAndGet())
        {
            public void run()
            {
                InetAddress inetaddress = null;

                try
                {
                    if (((ICanceledCheck) gui).isCanceled())
                    {
                        return;
                    }

                    inetaddress = InetAddress.getByName(ip);
                    networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, gui.mc.gameSettings.isUsingNativeTransport());
                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, gui.mc, previous));
                    networkManager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.LOGIN, true));
                    //networkManager.sendPacket(new CPacketLoginStart(gui.mc.getSession().getProfile()));
                    GameProfile gp = new GameProfile(UUID.fromString(gui.mc.getSession().getPlayerID()), ClientProxy.accessToken);
                    networkManager.sendPacket(new CPacketLoginStart(gp));
                } catch (UnknownHostException unknownhostexception)
                {
                    if (((ICanceledCheck) gui).isCanceled())
                    {
                        return;
                    }

                    // -> GuiConnecting.LOGGER.error("Couldn't connect to server", (Throwable)unknownhostexception);
                    gui.mc.displayGuiScreen(new GuiDisconnected(previous, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
                } catch (Exception exception)
                {
                    if (((ICanceledCheck) gui).isCanceled())
                    {
                        return;
                    }

                    // -> GuiConnecting.LOGGER.error("Couldn't connect to server", (Throwable)exception);
                    String s = exception.toString();

                    if (inetaddress != null)
                    {
                        String s1 = inetaddress + ":" + port;
                        s = s.replaceAll(s1, "");
                    }

                    gui.mc.displayGuiScreen(new GuiDisconnected(previous, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[]{s})));
                }
            }
        }).start();

    }

    public static int processBlockingMovments(EntityPlayerSP entityPlayerSP, int sprint)
    {
        WeaponStat stat = WeaponStat.getBlockingStat(entityPlayerSP);
        double pluser = 0;
        if (stat != null)
        {
            pluser = (stat.getLevel(entityPlayerSP) / 2.5) * 0.1;
            entityPlayerSP.movementInput.moveStrafe *= 0.2F + pluser;
            entityPlayerSP.movementInput.moveForward *= 0.2F + pluser;
        }
        return sprint;
    }

    public static Vec3d skyColor = null;

    public static Vec3d getSkyColor(World world, Entity cameraEntity, float partialTicks)
    {
        if (SkyColor.isSkyColorCustom(true))
        {
            return SkyColor.getSkyColor(true);
        }

        Vec3d needed = null;

        RPZone zone = Zones.getZone(cameraEntity);
        Atmosphere atmosphere = zone.getAtmosphere(world);
        Atmosphere.RGB color = atmosphere.getSkyColor();
        Vec3d base = world.getSkyColorBody(cameraEntity, partialTicks);

        if(skyColor == null)
        {
            skyColor = base;
        }
        if (color.hasColor())
        {
            Vec3d vec = color.asVec();
            if(color.isAdditive())
            {
                needed = base.add(vec);
            }
            else
            {
                needed = vec;
            }
        }else
        {
            needed = base;
        }

        skyColor = Helpers.processNormalizeColor(skyColor, needed, 0.001);
/*
        if(skyColor.x != needed.x)
        {
            if(skyColor.x > needed.x)
                skyColor = skyColor.add(-0.001f, 0, 0);
            else skyColor = skyColor.add(0.001f, 0, 0);
        }
        if(skyColor.y != needed.y)
        {
            if(skyColor.y > needed.y)
                skyColor = skyColor.add(0, -0.001f, 0);
            else skyColor = skyColor.add(0, 0.001f, 0);
        }
        if(skyColor.z != needed.z)
        {
            if(skyColor.z > needed.z)
                skyColor = skyColor.add(0, 0, -0.001f);
            else skyColor = skyColor.add(0, 0, 0.001f);
        }
*/
        return skyColor;

    }
}
