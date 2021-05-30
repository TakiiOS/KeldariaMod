/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.features.combatstats.WeaponStat;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.DecimalFormat;

public class GuiWeaponStats extends GuiFrame
{

    public static final ResourceLocation RES = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/combatstat/combatstat.png");

    public EnumAptitudes selectedAptitude;

    public GuiWeaponStats()
    {
        super(0, 0, 200, 166);
        setTexture(new GuiTextureSprite(RES, 256, 256, 0, 0, 200, 166));
        setBackgroundColor(0);
        GuiScrollPane pane = new GuiScrollPane(9, 21, 182, 136);
        pane.setBackgroundColor(0);
        int y = 0;
        for (WeaponStat value : WeaponStat.values())
        {
            pane.add(new GuiWeaponStatBar(value, 0, y));
            y += 11*2;
        }
        add(pane);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);
        //GuiScreen.drawRect(getScreenX() + 96, getScreenY() +20, getScreenX() + 96 + 96, getScreenY() + 20 + 138, Color.decode("#130f40").getRGB());
        mc.fontRenderer.drawStringWithShadow("Compétences armes:", getScreenX() + 10, getScreenY() + 6, Color.GRAY.getRGB());
        if(mc.fontRenderer != null && selectedAptitude != null)
        {

            mc.fontRenderer.drawStringWithShadow(String.format("Infos %s:", selectedAptitude.getDisplayName()), getScreenX() + 100, getScreenY() + 6, Color.GRAY.getRGB());
            GlStateManager.pushMatrix();
            GlStateManager.translate(getScreenX() + 98, getScreenY() +23, 0);
            GlStateManager.scale(0.75, 0.75, 1.0f);
            mc.fontRenderer.drawSplitString(
                    this.selectedAptitude.getDescriptionFor(selectedAptitude.getPoints(mc.player)),
                    0, 0, (int) ((int) 70 * 1.75),
                    Color.WHITE.getRGB());
            GlStateManager.popMatrix();
        }

    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }

    public class GuiWeaponStatBar extends GuiComponent
    {

        public final GuiTextureSprite BAR = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/combatstat/combatstat.png"), 256, 256, 0, 166, 165, 11);
        public final GuiTextureSprite BAR_FILL = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/combat/combatstat/combatstat.png"), 256, 256, 0, 177, 165, 11);

        public final WeaponStat stat;

        public GuiWeaponStatBar(WeaponStat stat, int x, int y)
        {
            super(x, y, 182, 11 * 2);
            setBackgroundColor(0);
            this.stat = stat;
        }

        @Override
        public void drawForeground(int mouseX, int mouseY, float partialTicks)
        {
            super.drawForeground(mouseX, mouseY, partialTicks);
            //mc.fontRenderer.drawStringWithShadow(stat.getName(), getScreenX(), getScreenY() + 3, Color.WHITE.getRGB());
            ItemStack toRender = new ItemStack(this.stat.getWeaponItem());
            GuiAPIClientHelper.drawItemStack(toRender, getScreenX(), getScreenY());
            BAR_FILL.drawSprite(getScreenX() + 15, getScreenY() + 4, 166, 11, Helpers.crossMult(stat.getPercent(mc.player), 100, 166), 11);
            BAR.drawSprite(getScreenX() + 15, getScreenY() + 4, 166, 11);

            RenderHelpers.drawScaledString(mc.fontRenderer, new DecimalFormat("#.##").format(stat.getLevel(mc.player)) + "/20",
                    getScreenX() + 20, getScreenY() + 6, 0.65f, Color.WHITE.getRGB(), true  );


        }
    }



}
