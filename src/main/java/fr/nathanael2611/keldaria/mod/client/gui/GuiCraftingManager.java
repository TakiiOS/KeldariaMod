/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiButtonCraft;
import fr.nathanael2611.keldaria.mod.crafting.CraftEntry;
import fr.nathanael2611.keldaria.mod.crafting.PlayerCrafts;
import fr.nathanael2611.keldaria.mod.crafting.PossibleWith;
import fr.nathanael2611.keldaria.mod.features.ability.EnumAptitudes;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketCraft;
import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import fr.reden.guiapi.component.textarea.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GuiCraftingManager extends GuiFrame
{

    public static final GuiTextureSprite GUI_TEXTURES = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/crafting_gui.png"), 200, 166, 0, 0, 200, 166);

    private PlayerCrafts crafts;
    public CraftEntry selectedCraft = null;

    public GuiCraftingManager(PlayerCrafts crafts)
    {
        super(0, 0, 200, 200);
        this.crafts = crafts;
        this.setBackgroundColor(0);
        this.setTexture(GUI_TEXTURES);
        this.setPauseGame(false);
        //this.setBackgroundColor(new Color(0, 0, 0, 100).getRGB());

        final boolean[] buttonNeedRefresh = {true};

        GuiScrollPane buttonScrollPane = new GuiScrollPane(9, 21, 61, 170 - 20);
        buttonScrollPane.setBackgroundColor(0);

        String[] searchedWorld = new String[]{""};

        buttonScrollPane.addTickListener(() ->
        {
            if (buttonNeedRefresh[0])
            {

                buttonScrollPane.removeAllChilds();

                int y = 0;
                int x = 0;
                for (CraftEntry craftEntry : crafts.getEntries())
                {
                    if (craftEntry.getResult().getDisplayName().toLowerCase().contains(searchedWorld[0].toLowerCase()))
                    {
                        GuiButtonCraft btn = new GuiButtonCraft(x, y, 15, 15, craftEntry, this);
                        btn.setHoveringText(btn.entry.getResult().getTooltip(mc.player, ITooltipFlag.TooltipFlags.ADVANCED));
                        buttonScrollPane.add(btn);
                        x += 15;
                        if (x >= 60)
                        {
                            y += 15;
                            x = 0;
                        }
                    }
                }
                buttonNeedRefresh[0] = false;
            }
        });

        GuiTextField searchField = new GuiTextField(9, 170, 61, 20);
        searchField.addKeyboardListener((typedChar, keyCode) ->
        {
            searchedWorld[0] = searchField.getText();
            buttonNeedRefresh[0] = true;
        });
        add(searchField);

        add(buttonScrollPane);

        GuiButton buyButton = new GuiButton(1000, 1000, 39, 12, "Craft");
        buyButton.addTickListener(() ->
        {
            buyButton.setX(selectedCraft != null ? 80 : 1000);
            buyButton.setY(selectedCraft != null ? 180 : 1000);
            if (selectedCraft != null)
            {
                buyButton.setEnabled(selectedCraft.canCraft(mc.player));
            }
        });
        buyButton.addClickListener((mouseX, mouseY, button) ->
        {
            if (this.selectedCraft != null)
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketCraft(this.crafts.getManagerName(), this.selectedCraft.getKey()));
            }
        });
        add(buyButton);

        GuiComponent component = new GuiComponent(81, 142, 37, 37)
        {
        };
        component.setBackgroundColor(0);
        component.addTickListener(() ->
        {
            component.setHoveringText(selectedCraft != null ? selectedCraft.getResult().getTooltip(mc.player, ITooltipFlag.TooltipFlags.ADVANCED) : Lists.newArrayList());
        });
        add(component);

    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawForeground(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1);

        AtomicReference<ItemStack> stackToRender = new AtomicReference<>();
        stackToRender.set(ItemStack.EMPTY);

        RenderHelpers.drawScaledString(mc.fontRenderer, "§nListe des crafts", 5, 8, 0.7f, Color.DARK_GRAY.getRGB(), false);

        if (selectedCraft != null)
        {
            RenderHelpers.drawScaledString(mc.fontRenderer, "§nCoût de: " + this.selectedCraft.getResult().getDisplayName(), 74, 8, 0.7f, Color.DARK_GRAY.getRGB(), false);


            GlStateManager.pushMatrix();
            GlStateManager.pushMatrix();
            RenderHelpers.drawScaledString(mc.fontRenderer, "§lCompétences requises: ", 78, 24, 0.7f, Color.WHITE.getRGB(), true);
            GlStateManager.translate(0, -5, 0);
            List<PossibleWith> skillPossibilities = this.selectedCraft.getSkillPossibilities();
            if (skillPossibilities.size() > 0)
            {
                int y = 0;
                for (PossibleWith with : skillPossibilities)
                {
                    String str = y != 0 ? "ou " : "";
                    if (with.isJobNeed())
                    {
                        RenderHelpers.drawScaledString(mc.fontRenderer, "§7" + str + "§8" + with.getJob().getFormattedName() + "§7 niveau §8" + with.getLevel().getFormattedName(), 78, y + mc.fontRenderer.FONT_HEIGHT * 4, 0.7f, Color.WHITE.getRGB(), true);
                    } else if (with.isComplementNeed())
                    {
                        RenderHelpers.drawScaledString(mc.fontRenderer, "§7" + str + "§8" + with.getComplement().getFormattedName(), 78, y + mc.fontRenderer.FONT_HEIGHT * 4, 0.7f, Color.WHITE.getRGB(), true);

                    }
                    y += mc.fontRenderer.FONT_HEIGHT;
                }
                RenderHelpers.drawScaledString(mc.fontRenderer, this.selectedCraft.haveSkills(this.mc.player) ? "§aVous avez les compétences!" : "§cVous n'avez pas les compétences!", 78, mc.fontRenderer.FONT_HEIGHT * 4 + y, 0.7f, Color.WHITE.getRGB(), true);
                //TODO
            }

            GlStateManager.popMatrix();
            GlStateManager.translate(0, 35, 0);
            {

                RenderHelpers.drawScaledString(mc.fontRenderer, "§lRessources:", 78, 24, 0.7f, Color.WHITE.getRGB(), true);

                AtomicInteger xx = new AtomicInteger();
                this.selectedCraft.getIngredients().forEach((item, count) ->
                {
                    ItemStack s = new ItemStack(item.getItem(), count, item.isNeedSameMetadata() ? item.getMeta() : 0);
                    int placeX = 78 + xx.get();
                    RenderHelpers.renderItemStack(s, placeX, 12 + mc.fontRenderer.FONT_HEIGHT * 2, partialTicks, false);
                    xx.set(xx.get() + 20);
                    int pluser = 56;
                    if (mouseX < x + placeX + 20 && mouseX > x + placeX && mouseY < (y + pluser) + mc.fontRenderer.FONT_HEIGHT * 2 + 5 && mouseY > y + pluser + mc.fontRenderer.FONT_HEIGHT)
                        stackToRender.set(s);
                });

                RenderHelpers.drawScaledString(mc.fontRenderer, this.selectedCraft.haveIngredients(this.mc.player) ? "§aVous avez les ressources!" : "§cVous n'avez pas les ressources!", 78, 20 + mc.fontRenderer.FONT_HEIGHT * 3, 0.65f, Color.WHITE.getRGB(), true);
                RenderHelpers.drawScaledString(mc.fontRenderer, "§lAptitudes requises:", 78, 26 + mc.fontRenderer.FONT_HEIGHT * 3, 0.7f, Color.WHITE.getRGB(), true);

                if (this.selectedCraft.getRequiredAptitudes().size() > 0)
                {
                    int y = 0;
                    for (EnumAptitudes aptitude : this.selectedCraft.getRequiredAptitudes().keySet())
                    {
                        RenderHelpers.drawScaledString(mc.fontRenderer, "§8" + aptitude.getDisplayName() + "§7 niveau §8" + this.selectedCraft.getRequiredAptitudes().get(aptitude), 78, 26 + mc.fontRenderer.FONT_HEIGHT * 4 + y, 0.7f, Color.WHITE.getRGB(), true);
                        y += mc.fontRenderer.FONT_HEIGHT;
                    }

                    RenderHelpers.drawScaledString(mc.fontRenderer, this.selectedCraft.haveAptitudes(this.mc.player) ? "§aVous avez les aptitudes!" : "§cVous n'avez pas les aptitudes!", 78, 26 + mc.fontRenderer.FONT_HEIGHT * 4 + y, 0.65f, Color.WHITE.getRGB(), true);
                } else
                {
                    RenderHelpers.drawScaledString(mc.fontRenderer, "§7Aucune", 78, 26 + mc.fontRenderer.FONT_HEIGHT * 4, 0.7f, Color.WHITE.getRGB(), true);

                }

            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(124, 138, 0);
            GlStateManager.scale(0.65, 0.65, 1.0f);
            if (mc.fontRenderer != null)
                mc.fontRenderer.drawSplitString(this.selectedCraft.getDescription(), 0, 0, (int) (62 * 1.65), Color.DARK_GRAY.getRGB());
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(81, 142, 1);
            GlStateManager.scale(2.3, 2.3, 2.3);
            RenderHelpers.renderItemStack(this.selectedCraft.getResult(), 0, 0, partialTicks, false);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();

        if (stackToRender.get() != ItemStack.EMPTY)
        {
            GuiAPIClientHelper.drawHoveringText(stackToRender.get().getTooltip(mc.player, ITooltipFlag.TooltipFlags.ADVANCED), mouseX, mouseY);
        }
    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }
}