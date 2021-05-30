/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketChangeRolePlayName;
import fr.nathanael2611.keldaria.mod.server.RolePlayNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;


/**
 * TODO: Rwrite all class
 */

public class GuiRolePlayChat extends GuiScreen
{

    private static final ResourceLocation EXPLAIN_LOC = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/rpchat/explications_chat_rp.png");
    private static final ResourceLocation DEFINE_RPNAME_LOC = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/rpchat/define_rpname.png");

    // private GuiScrollbar scrollbar;
    private GuiTextField field;

    @Override
    public void initGui()
    {
        super.initGui();
        // this.scrollbar = new GuiScrollbar(0, -145, 0, 10, height, "", false);
        // this.buttonList.add(scrollbar);

        this.field = new GuiTextField(1, fontRenderer, width / 2 - 64, height / 2 + 5, 65 * 2, 20);
        this.field.setEnableBackgroundDrawing(false);
        this.field.setTextColor(Color.WHITE.brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter().getRGB());
        this.field.setText(RolePlayNames.getName(mc.player));
        GuiButton button = new GuiButton(2, width / 2 - 60, height / 2 + 30, 120, 20, "Sauvegarder");
        this.buttonList.add(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();
        // this.scrollbar.setSliderMaxValue(10);
        // RenderHelpers.drawImage(0, -(scrollbar.getPercentageValue() * 100 / 300), EXPLAIN_LOC, 150, 300);
        RenderHelpers.drawImage(width / 2 - 150 / 2, height / 2 - 150 / 2, DEFINE_RPNAME_LOC, 150, 150);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.field.drawTextBox();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.field.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        this.field.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if(button.id == 2)
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketChangeRolePlayName(this.field.getText()));
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}
