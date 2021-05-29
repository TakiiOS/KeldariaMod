package fr.reden.guiapi.component;

import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.component.panel.GuiPanel;
import fr.reden.guiapi.GuiConstants;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class GuiCheckBox extends GuiPanel {
    
    protected int checkBoxBorderColor;
    
    protected String text;
    protected GuiCheckBoxButton checkButton;
    protected boolean isChecked;
    
    protected int checkBoxColor;
    protected int textColor;
	
	public GuiCheckBox(int x, int y, int width) {
		this(x, y, width, "");
	}
	
	public GuiCheckBox(int x, int y, int width, String text) {
		super(x, y, width, 11);
		add(checkButton = new GuiCheckBoxButton(0, 0, 11, 11));
		setBackgroundColor(new Color(0,0,0,0).getRGB());
		setCheckBoxColor(-16777216);
		setCheckBoxBorderColor(-6250336);
		setText(text);
		textColor = Color.WHITE.getRGB();
	}

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        
        if(text != null && !text.isEmpty()) {
            String str = mc.fontRenderer.trimStringToWidth(text, getWidth() - (checkButton.getWidth() + 2));

            if(str.length() < text.length()) {
                str = mc.fontRenderer.trimStringToWidth(text, getWidth() - (checkButton.getWidth() + 8)) + "...";
            }

            mc.fontRenderer.drawString(str, getScreenX() + checkButton.getWidth() + 2, getScreenY() + 2, textColor);
            //drawString(mc.fontRenderer, str, getScreenX() + checkButton.getWidth() + 2, getScreenY() + 2, textColor);
        }
        
        super.drawForeground(mouseX, mouseY, partialTicks);
    }

    public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
    
    public class GuiCheckBoxButton extends GuiButton {

        protected GuiCheckBoxButton(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
            setChecked(!isChecked);
            super.onMouseClicked(mouseX, mouseY, mouseButton);
        }
        
        @Override
        public void drawBackground(int mouseX, int mouseY, float partialTicks) {
            GuiAPIClientHelper.drawBorderedRectangle(getScreenX(), getScreenY(), getScreenX() + getWidth(), getScreenY() + getHeight(), 1, checkBoxColor, checkBoxBorderColor);
        }

        @Override
        public void drawForeground(int mouseX, int mouseY, float partialTicks) {
            if (isChecked())
                drawCenteredString(mc.fontRenderer, "x", getScreenX() + getWidth() / 2 + 1, getScreenY() + 1, 14737632);
        }

    }

    public boolean isChecked() {
        return isChecked;
    }

    public GuiCheckBox setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        return this;
    }

    public GuiCheckBox setText(String text) {
        this.text = text;
        return this;
    }

    public GuiCheckBox setCheckBoxBorderColor(int checkBoxBorderColor) {
        this.checkBoxBorderColor = checkBoxBorderColor;
        return this;
    }

    public GuiCheckBox setCheckBoxColor(int checkBoxColor) {
        this.checkBoxColor = checkBoxColor;
        return this;
    }
    
    public GuiCheckBoxButton getCheckButton() {
		return checkButton;
	}
}
