/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiButtonIngameMenu;
import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.features.cleanliness.CleanilessManager;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiCustomIngameMenu extends GuiScreen
{

    @Override
    public void initGui()
    {
        super.initGui();
        int yB = ( height / 2 ) - 40;
        this.buttonList.add(new GuiButtonIngameMenu(0, 0, yB + 80, 100, 20, "Déconnexion"));
        GuiButton lanButton;
        this.buttonList.add(lanButton = new GuiButtonIngameMenu(1, 0, yB, 100, 20, "Open to LAN"));
        lanButton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
        this.buttonList.add(new GuiButtonIngameMenu(2, 0, yB + 20, 100, 20, "Options"));
        this.buttonList.add(new GuiButtonIngameMenu(3, 0, yB + 40, 100, 20, "Back to RP§f"));
    }

    private int progress = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(progress+30 < height) progress+=30;
        else progress = height;
        this.drawGradientRect(0, 0, width, progress, Color.BLACK.getRGB(), new Color(0, 0, 0, 0).getRGB());
        {
            int haha = 0;
            if(progress == height) haha += 50;
            int scale = progress / 4;
            if(scale > 50) scale = 50;
            GuiInventory.drawEntityOnScreen(width / 2, progress / 2 + haha, scale, -(mouseX - width / 2), -(mouseY - height / 2), Minecraft.getMinecraft().player);
        }
        this.drawCenteredString(fontRenderer, ClientDatabases.getDatabase("roleplayinfos").getString("date"), width / 2, 10, Color.WHITE.getRGB());
        RenderHelpers.drawScaledString(this.fontRenderer, this.mc.player.getName(), 5, 5, 2.0f, Color.WHITE.getRGB(), true);
        RenderHelpers.drawScaledString(this.fontRenderer, RolePlayNames.getName(this.mc.player), 5, (int) (5 + (this.fontRenderer.FONT_HEIGHT * 2.0f)), 1.3f, Color.GRAY.getRGB(), true);
        RenderHelpers.drawScaledString(this.fontRenderer, "Propreté: " + CleanilessManager.getCleanilessValue(this.mc.player) + "%", 5, (int) (6 + (this.fontRenderer.FONT_HEIGHT * 3.0f)), 1.3f, Color.GRAY.getRGB(), true);

        KeldariaDate.KeldariaBirthday birthday = KeldariaDate.KeldariaBirthday.getBirthDay(mc.player);

        RenderHelpers.drawScaledString(this.fontRenderer, "Anniversaire: " +
                        (birthday.isValid() ? birthday.toString() : "§c ╳ Non défini")
                , 5, (int) (7 + (this.fontRenderer.FONT_HEIGHT * 4.0f)), 1.3f, Color.GRAY.getRGB(), true);
        RenderHelpers.drawScaledString(this.fontRenderer, "Âge: " +
                        (birthday.isValid() ? birthday.getAge(KeldariaDate.getKyrgonDate()) + " ans" : "§c ╳ Non défini")
                , 5, (int) (8 + (this.fontRenderer.FONT_HEIGHT * 5.0f)), 1.3f, Color.GRAY.getRGB(), true);
        RenderHelpers.drawScaledString(this.fontRenderer, "Pour définir l'anniversaire: /anniversaire"
                , 5, (int) (9 + (this.fontRenderer.FONT_HEIGHT * 6.0f)), 1f, Color.LIGHT_GRAY.getRGB(), true);



        super.drawScreen(mouseX, mouseY, partialTicks);
    }



    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        int id = button.id;
        if(id == 0)
        {
            boolean flag = this.mc.isIntegratedServerRunning();
            boolean flag1 = this.mc.isConnectedToRealms();
            button.enabled = false;
            this.mc.world.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);

            if (flag)
            {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
            else if (flag1)
            {
                RealmsBridge realmsbridge = new RealmsBridge();
                realmsbridge.switchToRealms(new GuiMainMenu());
            }
            else
            {
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            }
        }
        else if(id == 1)
        {
            this.mc.displayGuiScreen(new GuiShareToLan(this));
        }
        else if(id == 2)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        else if(id == 3)
        {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
    }
}