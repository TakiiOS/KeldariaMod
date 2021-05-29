package fr.nathanael2611.keldaria.mod.client.gui.button;

import net.minecraft.client.Minecraft;

import java.awt.*;

public class GuiButtonWardrobe extends GuiButtonFullgood {

    private boolean isSelected = false;
    private String url;

    public GuiButtonWardrobe(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String url) {
        super(buttonId, x, y, widthIn, heightIn, buttonText, new Color(0, 0, 0, 0), Color.YELLOW);
        this.url = url;

    }




    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        alpha = 255;
        Color colorBackground = Color.BLACK;
        if(isSelected) colorBackground = Color.GREEN.darker().darker().darker().darker();
        else if(hovered) colorBackground = Color.WHITE;
        this.color = colorBackground;

        int colorX = Color.RED.getRGB();
        if(
                isDeleteButtonHovered(mouseX, mouseY)
        ) {
            colorX = Color.YELLOW.getRGB();
        }
        mc.fontRenderer.drawStringWithShadow("x", x + width - 10, y + 3, colorX);

        /*drawRect(
                x + 1,
                y + 1,
                x + width - 2,
                y + height - 2,
                colorBackground.getRGB()
        );*/

        //drawString(mc.fontRenderer, displayString,x, y, colorText.getRGB());
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int getTextColor() {
        Color colorText = Color.WHITE;
        if(isSelected) colorText = Color.GREEN;
        else if(hovered) colorText = Color.YELLOW;
        return colorText.getRGB();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
    }

    public boolean isDeleteButtonHovered(int mouseX, int mouseY) {
        return mouseX > x + width - 16 && mouseX < x + width
                && mouseY > y && mouseY < y + height;
    }

}
