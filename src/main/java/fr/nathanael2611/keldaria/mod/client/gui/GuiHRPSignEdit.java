package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketUpdateHRPSign;
import fr.nathanael2611.keldaria.mod.tileentity.TileEntityHRPSign;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.GuiSlider;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.textarea.GuiTextArea;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class GuiHRPSignEdit extends GuiFrame
{
    public GuiHRPSignEdit(BlockPos signPos)
    {
        super(0, 0, 200, 166);
        setBackgroundColor(Color.BLACK.getRGB());
        TileEntity te = mc.world.getTileEntity(signPos);
        if(te instanceof TileEntityHRPSign)
        {
            TileEntityHRPSign sign = (TileEntityHRPSign) te;

            Color base = new Color(sign.getColor());

            GuiSlider red = new GuiSlider(10, 166 / 2 + 10, 180, 10);
            red.setBackgroundColor(Color.RED.darker().darker().getRGB());
            red.setMin(0);
            red.setMax(255);
            red.setValue(base.getRed());
            add(red);

            GuiSlider green = new GuiSlider(10, 166 / 2 + 20, 180, 10);
            green.setBackgroundColor(Color.GREEN.darker().darker().getRGB());
            green.setMin(0);
            green.setMax(255);
            green.setValue(base.getGreen());

            add(green);

            GuiSlider blue = new GuiSlider(10, 166 / 2 + 30, 180, 10);
            blue.setBackgroundColor(Color.BLUE.darker().darker().getRGB());
            blue.setMin(0);
            blue.setMax(255);
            blue.setValue(base.getBlue());
            add(blue);

            GuiTextArea textArea = new GuiTextArea(0, 0, 200, 166 / 2);
            textArea.setText(sign.getText());
            add(textArea);

            addTickListener(() -> {
                int color = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
                textArea.setEnabledTextColor(color);
            });

            GuiButton sendButton = new GuiButton(10, 166 - 30, 180, 20, "Terminer");
            sendButton.addClickListener((mouseX, mouseY, mouseButton) ->
            {
                int color = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketUpdateHRPSign(signPos, textArea.getText(), color));
                mc.displayGuiScreen(null);
            });
            add(sendButton);
        }

        setPauseGame(false);
    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX((screenWidth - this.getWidth()) / 2);
        this.setY((screenHeight - this.getHeight()) / 2);
        super.resize(screenWidth, screenHeight);
    }
}
