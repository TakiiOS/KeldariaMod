/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerPurse;
import fr.nathanael2611.keldaria.mod.inventory.InventoryPurse;
import fr.nathanael2611.keldaria.mod.registry.KeldariaItems;
import fr.reden.guiapi.GuiAPIClientHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiPurse extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/containers/purse.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPurse purseInv;

    public GuiPurse(EntityPlayer player, InventoryPurse purse)
    {
        super(new ContainerPurse(purse, player.inventory));
        this.purseInv = purse;

    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        GuiAPIClientHelper.drawItemStack(new ItemStack(KeldariaItems.PURSE), i, j);
        fontRenderer.drawString("§8Bourse", i + 18, j + 6, Color.BLACK.getRGB(), false);

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
       // GuiInventory.drawEntityOnScreen(width / 2, j + 75, 30, (float)(width / 2) - mouseX, (float)(j + 75 - 50) - mouseY, this.mc.player);
    }

}
