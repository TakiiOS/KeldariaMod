/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component;

import fr.reden.guiapi.component.panel.GuiPanel;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseExtraClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseMoveListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiSlider extends GuiPanel implements IMouseClickListener, IMouseExtraClickListener 
{
    protected final List<ISliderListener> sliderListeners = new ArrayList<ISliderListener>();
	protected final GuiSliderButton sliderButton;
	protected final boolean horizontal;
	
	protected double min = 0, max = 1, value = 0;
	protected float step = 0.1F, wheelStep = 0.1F;
	
	public GuiSlider(int x, int y, int width, int height) {
		this(x, y, width, height, true);
	}
	
	public GuiSlider(int x, int y, int width, int height, boolean horizontal) {
		super(x, y, width, height);
		this.horizontal = horizontal;
		add(sliderButton = new GuiSliderButton(0, 0, 0, 0));
		sliderButton.setRelativeWidth(1F / (horizontal ? 5 : 1));
		sliderButton.setRelativeHeight(1F / (horizontal ? 1 : 5));
		setBackgroundColor(new Color(0, 0, 0, 0.5F).getRGB());
		
		addClickListener(this);
		addExtraClickListener(this);
	}

    public GuiSlider setValue(double value)
    {
        this.value = Math.round(value / step) * step;
        this.value = MathHelper.clamp(value, min, max);

        if(horizontal) {
            sliderButton.setX((int) (getRelativeValue() * (getWidth() - sliderButton.getWidth())));
            sliderButton.setY(0);
        } else {
            sliderButton.setX(0);
            sliderButton.setY((int) (getRelativeValue() * (getHeight() - sliderButton.getHeight())));
        }
        for(ISliderListener lis : sliderListeners)
        {
        	lis.onSliderChanged(value);
        }
        return this;
    }

    protected void changeValue(int mouseX, int mouseY)
    {
        double relValue;

        if (horizontal) {
            relValue = (mouseX - getScreenX() - sliderButton.getWidth() / 2) / (double) (getWidth() - sliderButton.getWidth());
        } else {
            relValue = (mouseY - getScreenY() - sliderButton.getHeight() / 2) / (double) (getHeight() - sliderButton.getHeight());
        }

        setValue(min + MathHelper.clamp(relValue, 0, 1) * (max - min));
    }
	
	@Override
    public void onMouseDoubleClicked(int mouseX, int mouseY, int mouseButton) {
		
		if(isHovered()) {
			changeValue(mouseX, mouseY);
			Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
		
    }
	
	@Override public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {}
    
	@Override public void onMousePressed(int mouseX, int mouseY, int mouseButton) {}
	
	@Override public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {}
	
	protected class GuiSliderButton extends GuiButton implements IMouseMoveListener {

        protected GuiSliderButton(int x, int y, int width, int height) {
            super(x, y, width, height);
            addMoveListener(this);
        }
		
        @Override
        public void onMouseMoved(int mouseX, int mouseY)
        {
	        if(isPressed()) {
		        changeValue(mouseX, mouseY);
	        }
        }
	
	    @Override public void onMouseHover(int mouseX, int mouseY) {}
	
	    @Override public void onMouseUnhover(int mouseX, int mouseY) {}
    }
	public static interface ISliderListener
	{
		public void onSliderChanged(double value);
	}
	public GuiSlider addSliderListener(ISliderListener lis)
	{
		sliderListeners.add(lis);
		return this;
	}

    public double getValue() {
        return value;
    }

    public double getRelativeValue() {
        return (this.value - min) / (max - min);
    }

    public GuiSlider setMin(double min) {
        this.min = min;
        setValue(value);
        return this;
    }

    public GuiSlider setMax(double max) {
        this.max = max;
        setValue(value);
        return this;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public GuiSlider setStep(float step) {
        this.step = step;
        return this;
    }

    public GuiSlider setWheelStep(float wheelStep) {
        this.wheelStep = wheelStep;
        return this;
    }
    
    public float getWheelStep() {
        return wheelStep;
    }

}
