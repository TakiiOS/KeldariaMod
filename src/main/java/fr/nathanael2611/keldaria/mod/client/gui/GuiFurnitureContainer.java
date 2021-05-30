/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.block.furniture.TileEntityChessPlate;
import fr.nathanael2611.keldaria.mod.container.ContainerFurnitureContainer;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityFurnitureContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiFurnitureContainer extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/furniture_container.png");
    /** The player inventory bound to this GUI. */

    private TileEntityFurnitureContainer te;

    public GuiFurnitureContainer(EntityPlayer player, TileEntityFurnitureContainer ring)
    {
        super(new ContainerFurnitureContainer(player.inventory, ring));
        this.te = ring;
        //this.ySize = 100 + (ring.getSizeInventory() / ring.getRowSize()) * 18;
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
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int n = 0;
        boolean startBlack = true;
        for (Slot inventorySlot : inventorySlots.inventorySlots)
        {
            if(!(inventorySlot.inventory instanceof InventoryPlayer) && te instanceof TileEntityChessPlate)
            {
                if(n % 2 == (startBlack ? 0 : 1))
                {
                    GlStateManager.color(
                            0,0,0, 0.5f
                    );
                }

                n ++;
                if(n == 8)
                {
                    n = 0;
                    startBlack = !startBlack;
                }
            }
            this.drawTexturedModalRect(i + inventorySlot.xPos - 1, j + inventorySlot.yPos - 1, 0, 166,18, 18);
            GlStateManager.color(1, 1, 1,1);
        }
       // GuiInventory.drawEntityOnScreen(width / 2, j + 75, 30, (float)(width / 2) - mouseX, (float)(j + 75 - 50) - mouseY, this.mc.player);
    }

}
