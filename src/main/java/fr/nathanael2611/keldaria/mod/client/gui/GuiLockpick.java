/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.item.ItemLockpick;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketLockpickDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiLockpick extends GuiScreen
{

    private String pos;

    public GuiLockpick(String pos)
    {
        this.pos = pos;
    }

    boolean isLockpicking;
    boolean canLockpick = true;

    int plusValue = 0;

    @Override
    public void initGui()
    {
        super.initGui();
        isLockpicking = false;
        canLockpick = true;
        plusValue = -10;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(!(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemLockpick))
        {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        int x = width / 2 - 50;
        int y = height / 2 - 50;
        RenderHelpers.drawImage(x, y, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/lockpick/lock_bottom.png"), 100, 100);

        int xx = x + plusValue;
        if(isLockpicking)
        {
            if(plusValue<10) plusValue += 3;
            else
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketLockpickDoor(pos));
                isLockpicking = false;
                return;
            }
        }
        else
        {
            if(plusValue > -10)plusValue-=2;
        }
        int baseYPos = y+51;
        int maxYPos = baseYPos+10;
        int posY = mouseY;
        if(mouseY < baseYPos)
        {
            posY = baseYPos;
        }
        else if(mouseY > maxYPos)
        {
            posY = maxYPos;
        }
        RenderHelpers.drawImage(xx, posY, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/lockpick/lockpick.png"), 80, 15);
        RenderHelpers.drawImage(x, y, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/lockpick/lock_top.png"), 100, 100);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        isLockpicking = !isLockpicking;
    }
}
