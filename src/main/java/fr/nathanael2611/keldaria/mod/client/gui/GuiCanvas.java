/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.features.canvas.CanvasContent;
import fr.nathanael2611.keldaria.mod.item.ItemCanvas;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.canvas.PacketSaveCanvas;
import fr.nathanael2611.keldaria.mod.util.math.Vector2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class GuiCanvas extends GuiScreen
{

    private static final ResourceLocation CANVAS_BACKGROUND = new ResourceLocation(Keldaria.MOD_ID.toLowerCase(), "textures/gui/canvas_background.png");

    private CanvasContent canvasContent;
    private static int brushSize = 1;
    private HashMap<String, Integer> pixelsPosCache = new HashMap<>();
    private Thread paintThread;
    private boolean inited = false;
    private boolean isSigned = false;
    private GuiTextField titleField = new GuiTextField(1, fontRenderer, 0, 0, 10, 10);
    private GuiButton signButton = new GuiButton(0, 0, 0, 0, 0, "Sign");
    private Vector2i mousePos;

    public GuiCanvas(NBTTagCompound canvasNBT)
    {
        this(canvasNBT, false);
    }

    public GuiCanvas(NBTTagCompound canvasNBT, boolean isSigned)
    {
        this.canvasContent = new CanvasContent(canvasNBT.getString(ItemCanvas.NBT_PIXELS_ID));
        this.isSigned = isSigned;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.pixelsPosCache.clear();
        this.inited = false;
        this.titleField = new GuiTextField(1, this.fontRenderer, 0, 0, 10, 10);
        if(!this.isSigned)
        {
            this.buttonList.add(this.signButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        signButton.enabled = this.titleField.getText().length() > 0;

        if(!this.isSigned)
        {
            brushSize += (Integer.compare(Mouse.getDWheel(), 0));
            if(brushSize < 1) brushSize = 1;
            this.drawCenteredString(this.fontRenderer, "Brush Size: " + brushSize, width / 2, 10, Color.WHITE.getRGB());
        }

        mousePos = new Vector2i(mouseX, mouseY);

        int canX = width / 2 - 64;
        int canY = height / 2 - 64;

        RenderHelpers.drawImage(canX - 2, canY - 2, CANVAS_BACKGROUND, 128 + 2 * 2, 128 + 2 * 2);

        int index = 0;
        int x = 0;
        int y = 0;
        final int width = 128;

        for(int i : canvasContent.getPixels())
        {

            Color color = new Color(0, 0, 0, 0);

            if(i == 1) color = Color.BLACK;

            if(x >= width)
            {
                x = 0;
                y ++;
            }

            if(!inited)
            {
                pixelsPosCache.put(new Vector2i(canX + x, canY + y).toString(), index);
            }
            drawRect(canX + x, canY + y, canX + x + 1, canY + y + 1, color.getRGB());

            x++;
            index ++;

        }

        if(!this.isSigned && paintThread == null) {
            paintThread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(!isInterrupted()) {
                        if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
                            String s = mousePos.toString();
                            if(pixelsPosCache.containsKey(s)) {
                                canvasContent.paintInRadius(Mouse.isButtonDown(0) ? CanvasContent.PAINT : CanvasContent.ERASE, pixelsPosCache, mousePos, brushSize);
                            }
                        }
                    }
                }
            };
            paintThread.start();
            inited = true;
        }

        if(!this.isSigned) {
            drawRect(mouseX - (brushSize / 2), mouseY - (brushSize / 2), mouseX + (brushSize / 2), mouseY + (brushSize / 2), new Color(0, 0, 0, 130).getRGB());
            titleField.x = canX;
            titleField.y = canY + 140;
            titleField.width = 98;
            titleField.height = 20;
            titleField.drawTextBox();

            signButton.x = canX + 100;
            signButton.y = canY + 140;
            signButton.width = 28;
            signButton.height = 20;

        }
    }

    @Override
    public void updateScreen()
    {
        if(!Mouse.isGrabbed()) Mouse.setGrabbed(false);
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.titleField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        this.titleField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        this.finish(false);
    }

    private void finish(boolean sign)
    {
        if(this.paintThread != null) this.paintThread.interrupt();
        PacketSaveCanvas packet = new PacketSaveCanvas(this.canvasContent, sign);
        packet.setTitle(this.titleField.getText());
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(packet);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if(button.id == 0)
        {
            this.finish(true);
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
}