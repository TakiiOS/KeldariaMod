package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerKeyring;
import fr.nathanael2611.keldaria.mod.container.ContainerShipChest;
import fr.nathanael2611.keldaria.mod.inventory.InventoryKeyRing;
import fr.nathanael2611.keldaria.mod.inventory.InventoryShipChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiShipChest extends GuiContainer
{

    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/keyring.png");
    /** The player inventory bound to this GUI. */
    private final InventoryShipChest playerInventory;

    public GuiShipChest(EntityPlayer player, InventoryShipChest ring)
    {
        super(new ContainerShipChest(ring, player.inventory));
        this.playerInventory = ring;

        this.ySize = 192;
        this.xSize = 176;

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

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/ship_chest.png"));
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        // GuiInventory.drawEntityOnScreen(width / 2, j + 75, 30, (float)(width / 2) - mouseX, (float)(j + 75 - 50) - mouseY, this.mc.player);
    }}
