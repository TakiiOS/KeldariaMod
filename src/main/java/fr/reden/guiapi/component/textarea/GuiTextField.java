package fr.reden.guiapi.component.textarea;

import fr.reden.guiapi.GuiConstants;

public class GuiTextField extends GuiTextArea {
	
	public GuiTextField(int x, int y, int width, int height) {
		super(x, y, width, height);
		setMaxTextLength(50);
	}
	
	public int getPaddingTop() {
		int height = getVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE ? (int) (getRelativeHeight() * getParent().getHeight()) : getHeight();
    	return (height - mc.fontRenderer.FONT_HEIGHT) / 2;
	}
	
	public int getPaddingBottom() {
		int height = getVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE ? (int) (getRelativeHeight() * getParent().getHeight()) : getHeight();
		return (height - mc.fontRenderer.FONT_HEIGHT) / 2 + 1;
	}
	
	public int getPaddingLeft() {
    	return 4;
	}
	
	public int getPaddingRight() {
    	return 4;
	}
	
	public int getMaxLineLength() {
		return Integer.MAX_VALUE;
	}

}
