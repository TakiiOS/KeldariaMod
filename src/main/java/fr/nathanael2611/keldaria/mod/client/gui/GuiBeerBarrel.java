/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerBeerBarrel;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityBeerBarrel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBeerBarrel extends GuiContainer
{

    private static final ResourceLocation textures = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/containers/beer_barrel.png");
    private TileEntityBeerBarrel beerBarrel;
    private InventoryPlayer inventoryPlayer;

    public GuiBeerBarrel(TileEntityBeerBarrel beerBarrel, InventoryPlayer inventoryPlayer)
    {
        super(new ContainerBeerBarrel(beerBarrel, inventoryPlayer));
        this.beerBarrel = beerBarrel;
        this.inventoryPlayer = inventoryPlayer;
        this.allowUserInput = false;
        this.ySize = 170;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int x, int y)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(textures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String tileName = "Fermentation Barrel";
        this.fontRenderer.drawStringWithShadow(ChatFormatting.WHITE + tileName, (this.xSize - this.fontRenderer.getStringWidth(tileName)) / 2, -10, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(textures);
        if(this.beerBarrel.canFerment())
        {
            int i = this.beerBarrel.getFermentationProgress();
            this.drawTexturedModalRect(k + 78, l + 41, 178, 15, 16, i);
        }
        this.renderHoveredToolTip(mouseX, mouseY);
    }

}