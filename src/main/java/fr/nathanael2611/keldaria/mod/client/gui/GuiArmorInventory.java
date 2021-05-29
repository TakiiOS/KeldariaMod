package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.asm.IKeldariaPlayer;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.container.ContainerAccessories;
import fr.nathanael2611.keldaria.mod.features.combat.EnumAttackType;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiArmorInventory extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/keldaria_inventory.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;

    public GuiArmorInventory(EntityPlayer player)
    {
        super(new ContainerAccessories(player.inventory, ((IKeldariaPlayer)player).getInventoryAccessories(), ((IKeldariaPlayer)player).getInventoryArmor(), ((IKeldariaPlayer)player).getInventoryCloths()));
        this.playerInventory = player.inventory;
        this.width = 176;
        this.height = 214;
        this.xSize = 176;
        this.ySize = 214;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {

    }

    public static final ResourceLocation HIT_TEXTURE = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/hit.png");
    public static final ResourceLocation THRUST_TEXTURE = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/thrust.png");
    public static final ResourceLocation SHARP_TEXTURE = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/sharp.png");


    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GuiInventory.drawEntityOnScreen(width / 2 - 35, j + 100, 35, (float)(width / 2 - 35) - mouseX, (float)(j + 75 - 50) - mouseY, this.mc.player);
        //String str= "Accessoires & Armure";
        //fontRenderer.drawString(str, width / 2- fontRenderer.getStringWidth(str) / 2, height / 2 - ySize / 2 + 2, Color.DARK_GRAY.getRGB());

        {
            int x = width / 2 - xSize / 2 - 60;
            int y = height / 2 - ySize / 2;
            InventoryArmor armor = Keldaria.getInstance().getSyncedAccessories().getAccessories(mc.player).getArmor();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(1.3, 1.3, 1.3);
            RenderHelpers.drawImage(0, 0, SHARP_TEXTURE, 16, 16);
            mc.fontRenderer.drawStringWithShadow(armor.getProtectionPercent(EnumAttackType.SHARP) + "%", 18, (16 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2), Color.WHITE.getRGB());
            RenderHelpers.drawImage(0, 0 + 20, THRUST_TEXTURE, 16, 16);
            mc.fontRenderer.drawStringWithShadow(armor.getProtectionPercent(EnumAttackType.THRUST) + "%",  18, (16 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2) + 20, Color.WHITE.getRGB());
            RenderHelpers.drawImage(0, 0 + 40, HIT_TEXTURE, 16, 16);
            mc.fontRenderer.drawStringWithShadow(armor.getProtectionPercent(EnumAttackType.HIT) + "%", 18, (16 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2) + 40, Color.WHITE.getRGB());
            GlStateManager.popMatrix();

            /*

             */
        }

    }

}
