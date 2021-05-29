package fr.nathanael2611.keldaria.mod.fight;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.fight.attacks.Attack;
import fr.nathanael2611.keldaria.mod.fight.attacks.weapons.SwingTypes;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketProcessAttack;
import fr.nathanael2611.keldaria.mod.util.math.mine.AABB;
import fr.nathanael2611.keldaria.mod.util.math.mine.Geometry;
import fr.nathanael2611.keldaria.mod.util.math.mine.OBB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ClientCombatEvents
{

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Post event)
    {

        FightControl<EntityPlayer> control = FightHandler.getFightControl(event.getEntityPlayer());
        if (control.isAttacking())
        {
            for (Attack.AttackPart part : control.getAttack().getParts())
            {
                if (part != null)
                {
                    OBB obb = part.getOBB(control.getAttack());
                    if (obb != null)
                    {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(event.getX() - event.getEntityPlayer().posX, event.getY() - event.getEntityPlayer().posY, event.getZ() - event.getEntityPlayer().posZ);
                        boolean red = false;
                        for (AxisAlignedBB collisionBox : event.getEntityPlayer().world.getCollisionBoxes(event.getEntityLiving(), event.getEntityLiving().getEntityBoundingBox().grow(3)))
                        {
                            if (Geometry.AABOBB(new AABB(collisionBox), obb))
                            {
                                red = true;
                            }
                        }
                        obb.draw(event.getPartialRenderTick(), red);
                        RenderHelpers.renderLine(obb.rotCenter, obb.rotCenter.add(0, 0.1, 0), 1, 0, 0);
                        RenderHelpers.renderLine(obb.rotCenter.add(0, 0.1, 0), obb.rotCenter.add(0.02, 0.1, 0), 0, 1, 0);

                        GlStateManager.popMatrix();
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {

        Minecraft mc = Minecraft.getMinecraft();

        Entity e = mc.getRenderViewEntity();
        if (e != null)
        {
            final double interpX = e.prevPosX + (e.posX - e.prevPosX) * event.getPartialTicks();
            final double interpY = e.prevPosY + (e.posY - e.prevPosY) * event.getPartialTicks();
            final double interpZ = e.prevPosZ + (e.posZ - e.prevPosZ) * event.getPartialTicks();

        }
    }


    public static final ResourceLocation RESOURCE = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/combat_icons.png");

    public int lastMouseValue = 0;
    public boolean pointRight = true;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event)
    {
        if (!isInCombatMode()) return;
        int actual = (int) Minecraft.getMinecraft().player.rotationYaw;
        if (actual > lastMouseValue)
        {
            pointRight = true;
        } else if (actual < lastMouseValue)
        {
            pointRight = false;
        }
        this.lastMouseValue = actual;

        ScaledResolution res = event.getResolution();
        GlStateManager.pushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1);

        Minecraft.getMinecraft().getTextureManager().bindTexture(RESOURCE);
        GlStateManager.translate(res.getScaledWidth() / 2, res.getScaledHeight() / 2 - 7, 0);
        GlStateManager.scale(2, 2, 2);
        if (!pointRight)
        {
            GlStateManager.translate(-4, 0, 0);
        }
        //RenderHelpers.drawImage(0, 0, RESOURCE, 40, 40);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, pointRight ? 5 : 0, 0, 5, 7, 256, 256);
        GlStateManager.popMatrix();
    }


    public static boolean isInCombatMode()
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        return FightHandler.isInFightMode(player);
    }

    private long lastInput = -1;

    @SubscribeEvent
    public void mouseEvent(MouseEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (FightHandler.isInFightMode(mc.player))
        {
            event.setCanceled(true);


            if ((event.getButton() != -1 || event.getDwheel() != 0) && System.currentTimeMillis() - lastInput > 700)
            {
                //***
                SwingTypes attack = null;
                if (event.getDwheel() > 0)
                {

                } else if (event.getDwheel() < 0)
                {
                    if (pointRight) attack = SwingTypes.BY_UP_RIGHT;
                    else attack = SwingTypes.BY_UP_LEFT;
                    System.out.println("cc");
                } else if (event.getButton() == 1)
                {
                } else if (event.getButton() == 0)
                {
                    if (pointRight) attack = SwingTypes.LATERAL_RIGHT;
                    else attack = SwingTypes.LATERAL_LEF;

                }
                if (attack != null)
                {
                    KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketProcessAttack(attack, mc.player.renderYawOffset));
                }

                //**
                lastInput = System.currentTimeMillis();

            }


        }
    }

}
