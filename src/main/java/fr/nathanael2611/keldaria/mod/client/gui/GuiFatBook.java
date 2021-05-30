/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.ImageCache;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.features.writable.FatBook;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.textarea.GuiTextField;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.regex.Pattern;

public class GuiFatBook extends GuiFrame
{
    protected Pattern regexPattern = Pattern.compile("(?s).*");

    private FatBook book;

    private int pageIndex = 0;

    public GuiFatBook(FatBook fatBook)
    {
        super(0, 0, 200, 280);
        GuiTextureSprite GUI_TEXTURES = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/fat_book.png"), 200, 280, 0, 0, 200, 280);
        setBackgroundColor(0);
        setTexture(GUI_TEXTURES);
        setPauseGame(false);
        this.book = fatBook;

        GuiButton leftButton = new GuiButton(6, 275 - 10, 20, 10, "<");
        leftButton.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            turnPage(-1);
        });
        add(leftButton);
        GuiButton rightButton = new GuiButton(6 + 20, 275 - 10, 20, 10, ">");
        rightButton.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            turnPage(1);
        });
        add(rightButton);

        GuiTextField titleField = new GuiTextField(8 + 40, 275 - 14, 100, 14);
        GuiButton signButton = new GuiButton(8 + 40 + 105, 275 - 14, 40, 14, "Sign");
        if(!book.isSigned())
        {
            signButton.addClickListener((mouseX, mouseY, mouseButton) ->
            {
                mc.player.sendChatMessage("/signfatbook " + titleField.getText());
                mc.displayGuiScreen(null);
            });
            add(titleField);
            add(signButton);
        }

        addKeyboardListener((typedChar, keyCode) ->
        {
            if(!book.isSigned() && !titleField.isFocused())
            {
                if (keyCode == Keyboard.KEY_BACK && this.book.getPage(pageIndex).length() > 0)
                {
                    if (this.book.getPage(pageIndex).length() == 1)
                    {
                        this.book.setPage(pageIndex, "");
                    } else
                    {
                        if(GuiScreen.isCtrlKeyDown())
                        {
                            String[] worlds = this.book.getPage(pageIndex).split(" ");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < worlds.length - 1; i++)
                            {
                                builder.append(worlds[i]).append(" ");
                            }
                            this.book.setPage(pageIndex, builder.toString());
                        }
                        else
                        {
                            this.book.setPage(pageIndex, this.book.getPage(pageIndex).substring(0, this.book.getPage(pageIndex).length() - 1));
                        }
                    }
                } else if (keyCode == Keyboard.KEY_RETURN && this.book.getPage(pageIndex).replace("\n", "                     ").length() < 800)
                {
                    this.book.setPage(pageIndex, this.book.getPage(pageIndex) + "\n");
                }
                else if(GuiScreen.isCtrlKeyDown() && keyCode == 47)
                {
                    String clipboard = GuiScreen.getClipboardString();
                    if(this.book.getPage(pageIndex).length() + clipboard.length() <= 800)
                    {
                        this.book.setPage(pageIndex, this.book.getPage(pageIndex) + clipboard);
                    }
                }
                else if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && isEnabled() && book.getPage(pageIndex).length() < 800)
                {
                    if (regexPattern.matcher(this.book.getPage(pageIndex) + typedChar).matches())
                    {
                        this.book.setPage(pageIndex, this.book.getPage(pageIndex) + String.valueOf(typedChar));
                    }
                }
            }
        });

        addCloseListener(() ->
        {
            this.book.updateToServer();
        });

    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(getScreenX(), getScreenY(), 1);

        String pageText = this.book.getPage(pageIndex);

        if(pageText.startsWith("[img=") && pageText.endsWith("]"))
        {
            String url = pageText.substring("[img=".length());
            url = url.substring(0, url.length() - 1);
            ResourceLocation image = ImageCache.getAsResourceLocation(url, ImageCache.getBlank());
            if(image != null)
            {

                RenderHelpers.drawImage(6, 5, image, 189, 248);
            }

        }
        else
        {
            mc.fontRenderer.drawSplitString(pageText + (!book.isSigned() ? "_" : ""), 6, 6, 200 - 5*2, Color.BLACK.getRGB());
        }

        mc.fontRenderer.drawString("§lPage §r" + (1 + pageIndex), 5, 255, Color.BLACK.getRGB());
        GlStateManager.popMatrix();
        super.drawForeground(mouseX, mouseY, partialTicks);


    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }

    public void turnPage(int index)
    {
        this.pageIndex = Math.max(0, Math.min(this.pageIndex + index, 99));
    }
}
