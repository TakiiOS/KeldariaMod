/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.animation.AnimationUtils;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiAnimationButton;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.animation.PacketHandleAnimationRequest;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class GuiAnimationChooser extends GuiScreen
{

    @Override
    public void initGui()
    {
        super.initGui();
        int theWidth = 0;
        for(int i = 0; i < AnimationUtils.animations.size(); i ++)
        {
            theWidth += 77;
            if(theWidth + 76 > width-10)
            {
                break;
            }

        }

        int baseX = (width-theWidth)/2;
        int posX  = baseX;
        int posY  = 10;

        for(Map.Entry<String, Animation> animation : AnimationUtils.animations.entrySet())
        {
            if (animation.getValue().isInAnimationMenu())
            {
                buttonList.add(new GuiAnimationButton(0, posX, posY, animation.getValue()));
                posX += 77;
                if (posX + 76 > width - 10)
                {
                    posX = baseX;
                    posY += 77;
                }
            }
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if(!Keyboard.isKeyDown(ClientProxy.KEY_ANIMATION.getKeyCode()))
        {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }

        for(GuiButton button : buttonList)
        {
            if(button instanceof GuiAnimationButton)
            {
                if(button.isMouseOver())
                {
                    GuiAnimationButton btn = (GuiAnimationButton)button;
                    drawHoveringText(Arrays.asList(btn.getAnimation().getDescription()), mouseX, mouseY, fontRenderer);
                }
            }
        }
    }



    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for(GuiButton button : buttonList){
            if(button instanceof GuiAnimationButton){
                if(button.isMouseOver()){
                    GuiAnimationButton btn = (GuiAnimationButton)button;
                    handleAnimation(btn.getAnimation());
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button instanceof GuiAnimationButton){
            Minecraft.getMinecraft().displayGuiScreen(null);
            GuiAnimationButton btn = (GuiAnimationButton)button;
            handleAnimation(btn.getAnimation());
        }
    }

    protected void handleAnimation(Animation animation){
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketHandleAnimationRequest(animation.getName()));
    }
}
