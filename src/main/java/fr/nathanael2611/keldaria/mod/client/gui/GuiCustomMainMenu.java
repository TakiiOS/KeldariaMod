/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiButtonIngameMenu;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GuiCustomMainMenu extends GuiScreen
{

    private static final int BACKGROUNDS = 8;
    private static int pluser = 0;
    private static int index = 1;

    private static final ResourceLocation KG_TITLE = new ResourceLocation(Keldaria.MOD_ID, "textures/content/kgl.png");

    public GuiCustomMainMenu()
    {
        FMLClientHandler.instance().setupServerList();
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButtonIngameMenu(10, 30, height - 10, 120, 10, "LocalServer"));

        this.buttonList.add(new GuiButtonIngameMenu(0, 30, height / 2 - 40, 120, 20, "Solo"));
        this.buttonList.add(new GuiButtonIngameMenu(1, 30, height / 2 - 20, 120, 20, "Rejoindre Keldaria"));
        this.buttonList.add(new GuiButtonIngameMenu(2, 30, height / 2 + 20, 120, 20, "Options"));
        this.buttonList.add(new GuiButtonIngameMenu(3, 30, height / 2 + 60, 120, 20, "Site"));

        this.buttonList.add(new GuiButtonIngameMenu(5, 30, height / 2 + 100, 120, 20, "§6Changer de compte"));

        this.buttonList.add(new GuiButtonIngameMenu(4, 30, height/2+80, 120, 20, "Discord"));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(keyCode == Keyboard.KEY_ESCAPE)
        {
            ClientProxy.accessToken = null;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        {
            pluser ++;
            if(pluser >= 80)
            {
                pluser = 0;
                index ++;
                if(index > BACKGROUNDS)
                {
                    index = 1;
                }
            }
        }

        RenderHelpers.drawImage(0 - 10 + (mouseX * 0.01), 0 - 10 + (mouseY * 0.01), new ResourceLocation(Keldaria.MOD_ID, "textures/gui/mainmenu/diapo/bg_" + index + ".png"), width + 20, height + 20);
        RenderHelpers.drawLinearGradientRect(0, 0, this.width, this.height, Color.BLACK.getRGB(), new Color(0, 0, 0, 0).getRGB(), 1);

        /* Mouse.setGrabbed(false); */

        super.drawScreen(mouseX, mouseY, partialTicks);

        for(int y = 0; y < height; y ++)
        {
            this.drawString(fontRenderer, "|", 30, y, Color.WHITE.getRGB());
        }

        RenderHelpers.drawImage(35, height / 2 - 40 - (411 * 0.23), KG_TITLE, 411 * 0.23, 411 * 0.23);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if(button.id==0)
        {
            this.mc.displayGuiScreen(new GuiWorldSelection(Minecraft.getMinecraft().currentScreen));
        }
        if(button.id==1)
        {
            this.connectToServer(Keldaria.SERVER_IP);
        }
        if(button.id==2)
        {
            this.mc.displayGuiScreen(new GuiOptions(this.mc.currentScreen, this.mc.gameSettings));
        }
        if(button.id==3)
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://keldaria.fr"));
            }
            catch(URISyntaxException | IOException e)
            {
                e.printStackTrace();
            }
        }
        if(button.id==4)
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://discord.gg/BfPdWaW"));
            }
            catch(URISyntaxException | IOException e)
            {
                e.printStackTrace();
            }
        }
        if(button.id == 5)
        {
            ClientProxy.accessToken = null;
        }
        if(button.id==10)
        {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if(Mouse.isGrabbed()) Mouse.setGrabbed(false);
    }

    private void connectToServer(String ip)
    {
        FMLClientHandler.instance().connectToServer(this, new ServerData("", ip, false));
    }
}
