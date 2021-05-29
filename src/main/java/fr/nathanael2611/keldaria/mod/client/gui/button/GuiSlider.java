package fr.nathanael2611.keldaria.mod.client.gui.button;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiSlider extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private final float minValue;
    private final float maxValue;
    private String name;
    private Runnable releaseAct;

    public GuiSlider(Runnable releaseAct, int buttonId, String name, int x, int y)
    {
        this(releaseAct, buttonId, name,x, y, 0, 0.0F, 1.0F);
    }

    public GuiSlider(Runnable releaseAct, int buttonId, String name, int x, int y, float def, float minValueIn, float maxValue)
    {
        super(buttonId, x, y, 150, 20, "");
        this.sliderValue = 1.0F;
        this.minValue = minValueIn;
        this.maxValue = maxValue;
        Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = Helpers.getPercent(def, maxValue - minValueIn) * 0.01f;
        this.name = name;
        this.displayString = name + ": " + (int) def;
        this.releaseAct = releaseAct;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }


    public double getSliderValue()
    {
        return (minValue + (this.sliderValue / 1 * (maxValue - minValue)));
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
                float f = this.sliderValue;
                //mc.gameSettings.setOptionFloatValue(this.options, f);
                this.sliderValue = f;
                this.displayString = this.name + ": " + (int) getSliderValue();
            }

            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
            //mc.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = this.name + ": " + this.sliderValue;
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
        this.releaseAct.run();
    }
}