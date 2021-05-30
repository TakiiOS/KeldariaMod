/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.textarea;

import java.awt.Color;
import java.util.List;

import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.GuiConstants;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLabel extends GuiTextArea {
	
	/** Text horizontal alignment, relative to the GuiLabel {@link fr.reden.guiapi.GuiConstants.HORIZONTAL_TEXT_ALIGNMENT} **/
	protected GuiConstants.HORIZONTAL_TEXT_ALIGNMENT horizontalTextAlignment = GuiConstants.HORIZONTAL_TEXT_ALIGNMENT.LEFT;
	/** Text horizontal alingment, relative to the GuiLabel {@link fr.reden.guiapi.GuiConstants.VERTICAL_TEXT_ALIGNMENT} **/
	protected GuiConstants.VERTICAL_TEXT_ALIGNMENT verticalTextAlignment = GuiConstants.VERTICAL_TEXT_ALIGNMENT.TOP;
	
	protected int paddingLeft = 0, paddingRight = 0, paddingTop = 0, paddingBottom = 0;
	
	public GuiLabel(int x, int y, int width, int height) {
		this(x, y, width, height, "");
	}
	
	public GuiLabel(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		setEditable(false);
		setBackgroundColor(new Color(0, 0, 0, 0).getRGB());
		setBorderSize(0);
		setText(text);
	}

	//=====ADDED BY AYMERICRED=====
	private boolean shadowed = true;
	public GuiLabel setShadowed(boolean b)
	{
		shadowed = b;
		return this;
	}
	public boolean isShadowed()
	{
		return shadowed;
	}
	//=============================

    @Override
    protected void drawTextLines(List<String> lines) {
	
		GlStateManager.enableTexture2D();
		
        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
			
			if(horizontalTextAlignment == GuiConstants.HORIZONTAL_TEXT_ALIGNMENT.JUSTIFY) {
				
				String[] words = lines.get(i).split(" ");
				
				int lineLength = mc.fontRenderer.getStringWidth(line);
				int spacesLeft = getMaxLineLength() - lineLength;
				
				if(i == lines.size() - 1 || lines.get(i+1).trim().isEmpty())
					spacesLeft = 0;
				
				int x = 0;
				
				for(int j = 0; j < words.length; j++) {
					x = j == 0 ? 0 : x + mc.fontRenderer.getStringWidth(words[j-1] + " ") + spacesLeft / (words.length - 1);
					
					/*drawString(mc.fontRenderer, words[j],
							getScreenX() + getPaddingLeft() + x - getLineScrollOffsetX(),
							getScreenY() + getPaddingTop() + GuiAPIClientHelper.getRelativeTextY(i, lines.size(), getHeight() - (getPaddingTop() + getPaddingBottom()), verticalTextAlignment) - getLineScrollOffsetY(),
							isEnabled() ? getEnabledTextColor() : getDisabledTextColor());*/
					mc.fontRenderer.drawString(words[j],
							getScreenX() + getPaddingLeft() + x - getLineScrollOffsetX(),
							getScreenY() + getPaddingTop() + GuiAPIClientHelper.getRelativeTextY(i, lines.size(), getHeight() - (getPaddingTop() + getPaddingBottom()), verticalTextAlignment) - getLineScrollOffsetY(),
							isEnabled() ? getEnabledTextColor() : getDisabledTextColor(), 
							isShadowed());
				}
				
			} else {
				/*drawString(mc.fontRenderer, line,
						getScreenX() + getPaddingLeft() + GuiAPIClientHelper.getRelativeTextX(line, getWidth() - (getPaddingLeft() + getPaddingRight()), horizontalTextAlignment) - getLineScrollOffsetX(),
						getScreenY() + getPaddingTop() + GuiAPIClientHelper.getRelativeTextY(i, lines.size(), getHeight() - (getPaddingTop() + getPaddingBottom()), verticalTextAlignment) - getLineScrollOffsetY(),
						isEnabled() ? getEnabledTextColor() : getDisabledTextColor());*/
				mc.fontRenderer.drawString(line,
						getRenderMinX() /*Changed from getScreenX() for a shelou bug*/+ getPaddingLeft() + GuiAPIClientHelper.getRelativeTextX(line, getWidth() - (getPaddingLeft() + getPaddingRight()), horizontalTextAlignment) - getLineScrollOffsetX(),
						getScreenY() + getPaddingTop() + GuiAPIClientHelper.getRelativeTextY(i, lines.size(), getHeight() - (getPaddingTop() + getPaddingBottom()), verticalTextAlignment) - getLineScrollOffsetY(),
						isEnabled() ? getEnabledTextColor() : getDisabledTextColor(),
						isShadowed());
			}
        }
    }

    @Override
	public int getMaxLineLength() {
    	return getWidth() - (getPaddingLeft() + getPaddingRight());
	}
	
    public GuiLabel setHorizontalTextAlignment(GuiConstants.HORIZONTAL_TEXT_ALIGNMENT horizontalTextAlignment) {
        this.horizontalTextAlignment = horizontalTextAlignment;
        return this;
    }

    public GuiConstants.HORIZONTAL_TEXT_ALIGNMENT getHorizontalTextAlignment() {
        return horizontalTextAlignment;
    }

    public GuiLabel setVerticalTextAlignment(GuiConstants.VERTICAL_TEXT_ALIGNMENT verticalTextAlignment) {
        this.verticalTextAlignment = verticalTextAlignment;
        return this;
    }

    public GuiConstants.VERTICAL_TEXT_ALIGNMENT getVerticalTextAlignment() {
        return verticalTextAlignment;
    }
	
    public GuiLabel setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
		return this;
    }
	
	public GuiLabel setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
		return this;
	}
	
	public GuiLabel setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
		return this;
	}
	
	public GuiLabel setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
		return this;
	}
    
	public int getPaddingTop() {
		return paddingTop;
	}
	
	public int getPaddingBottom() {
		return paddingBottom;
	}
	
	public int getPaddingLeft() {
		return paddingLeft;
	}
	
	public int getPaddingRight() {
		return paddingRight;
	}
    
}
