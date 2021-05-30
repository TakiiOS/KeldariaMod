/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.container.ContainerMill;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityMill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiMill extends GuiContainer
{

    private static final ResourceLocation textures = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/containers/mill.png");
    private TileEntityMill mill;
    private IInventory playerInv;

    public GuiMill(TileEntityMill mill, InventoryPlayer inventory)
    {
        super(new ContainerMill(mill, inventory));
        this.mill = mill;
        this.playerInv = inventory;
        this.allowUserInput = false;
        this.ySize = 170;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(textures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize); // on desine la texture, la fonction à pour argument point x de départ, point y de départ, vecteur u, vecteur v, largeur, hauteur)
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String tileName = "Moulin";
        this.fontRenderer.drawString(ChatFormatting.WHITE + tileName, (this.xSize - this.fontRenderer.getStringWidth(tileName)) / 2, -10, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(textures);
        if(mill.canGrind())
        {
            int i = this.mill.getGrindProgress();
            this.drawTexturedModalRect(k + 84, l + 24, 176, 0, 34, i);
        }
    }

}
