package fr.nathanael2611.keldaria.mod.client.ren;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderPlayerDummy extends Render
{
    public RenderPlayerDummy(final RenderManager manager)
    {
        super(manager);
    }

    public void doRender(final Entity entity, final double x, final double y, final double z, final float yaw, final float ticks)
    {
        if (Minecraft.getMinecraft().getRenderManager() != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && REN.modes[REN.currentMode] % 100 >= 10)
        {
            final AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getMinecraft().player;
            if (player.isElytraFlying())
            {
                return;
            }
            final RenderPlayer playerRenderer = this.renderManager.getSkinMap().get(player.getSkinType());
            playerRenderer.getMainModel().bipedHead.isHidden = true;
            playerRenderer.getMainModel().bipedHeadwear.isHidden = true;
            final ItemStack tempStack = player.inventory.getCurrentItem();
            final ItemStack tempStack2 = player.getHeldItemOffhand();
            if (REN.modes[REN.currentMode] / 100 != 1 || (REN.customItemOverride && REN.modes[REN.currentMode] % 10 == 1))
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                player.inventory.offHandInventory.set(0,  ItemStack.EMPTY);
                playerRenderer.getMainModel().bipedLeftArm.isHidden = true;
                playerRenderer.getMainModel().bipedRightArm.isHidden = true;
            }
            final ItemStack helmetStack = (ItemStack) player.inventory.armorInventory.get(3);
            player.inventory.armorInventory.set(3,  ItemStack.EMPTY);
            final double[] d = new double[4];
            if (player.isPlayerSleeping())
            {
                d[0] = player.posX - entity.posX + x;
                d[1] = player.posY - entity.posY + y;
                d[2] = player.posZ - entity.posZ + z;
                d[3] = player.renderYawOffset;
            } else
            {
                final double renderOffset = player.prevRenderYawOffset - (player.prevRenderYawOffset - player.renderYawOffset) * ticks;
                d[0] = player.posX - entity.posX + x + REN.bodyOffset * Math.sin(Math.toRadians(renderOffset));
                d[1] = player.posY - entity.posY + y + 0.02;
                d[2] = player.posZ - entity.posZ + z - REN.bodyOffset * Math.cos(Math.toRadians(renderOffset));
                d[3] = renderOffset;
            }
            if (player.isSneaking())
            {
                d[1] = player.posY - entity.posY + 0.3;
            }
            playerRenderer.doRender(player, d[0], d[1], d[2], (float) d[3], ticks);
            player.inventory.armorInventory.set(3,  helmetStack);
            playerRenderer.getMainModel().bipedHead.isHidden = false;
            playerRenderer.getMainModel().bipedHeadwear.isHidden = false;
            if (REN.modes[REN.currentMode] / 100 != 1 || (REN.customItemOverride && REN.modes[REN.currentMode] % 10 == 1))
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, tempStack);
                player.inventory.offHandInventory.set(0,  tempStack2);
                playerRenderer.getMainModel().bipedLeftArm.isHidden = false;
                playerRenderer.getMainModel().bipedRightArm.isHidden = false;
            }
        }
    }

    public ResourceLocation getEntityTexture(final Entity entity)
    {
        return null;
    }
}