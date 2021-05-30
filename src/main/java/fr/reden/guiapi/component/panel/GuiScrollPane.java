/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.panel;

import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.GuiSlider;
import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.event.listeners.IResizeListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseWheelListener;

import java.util.List;

public class GuiScrollPane extends GuiPanel implements IMouseWheelListener, IResizeListener {
	
	protected int lastScrollAmountX;
	protected int lastScrollAmountY;
	protected final GuiSlider xSlider;
	protected final GuiSlider ySlider;

    private boolean autoScroll;
    
    public GuiScrollPane(int x, int y, int width, int height) {
    	super(x, y, width, height);
    	
		xSlider = new GuiSlider(0, 0, 0, 6, true);
		xSlider.setRelativeX(0.1F);
		xSlider.setY(6, GuiConstants.ENUM_RELATIVE_Y.BOTTOM);
		xSlider.setRelativeWidth(0.8F);
		
		ySlider = new GuiSlider(0, 0, 6, 0, false);
	    ySlider.setX(6, GuiConstants.ENUM_RELATIVE_X.RIGHT);
	    ySlider.setRelativeY(0.1F);
	    ySlider.setRelativeHeight(0.8F);
	    
		add(xSlider);
		add(ySlider);
		xSlider.setZLevel(Integer.MAX_VALUE);
		ySlider.setZLevel(Integer.MAX_VALUE);
		
		addWheelListener(this);
		addResizeListener(this);
	}

    public void setAutoScroll(boolean autoScroll) 
    {
		this.autoScroll = autoScroll;
	}
    public boolean hasAutoScroll() 
    {
		return autoScroll;
	}
    
	@Override
	public void onMouseWheel(int dWheel) {
		if(isHovered()) {
			GuiSlider slider = xSlider.isHovered() ? xSlider : ySlider;
			slider.setValue(slider.getValue() + (dWheel / -120.0D * slider.getWheelStep()) * (slider.getMax() - slider.getMin()));
		}
	}
	
	@Override
	public void onResize(int width, int height) {
		updateSlidersVisibility();
	}
	
    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        if(-xSlider.getValue() != lastScrollAmountX || -ySlider.getValue() != lastScrollAmountY) {

            for(GuiComponent component : getChildComponents()) {
                if(component == xSlider || component == ySlider) continue;
                component.setOffsetX((int) -xSlider.getValue());
                component.setOffsetY((int) -ySlider.getValue());
            }

            lastScrollAmountX = (int) -xSlider.getValue();
            lastScrollAmountY = (int) -ySlider.getValue();
        }

        super.drawForeground(mouseX, mouseY, partialTicks);
    }

    /**
     * Hide the sliders if the maximum effective size is inferior to the rendered size
     * {@link #getMaxWidth()}, {@link #getMaxHeight()}
     */
	public void updateSlidersVisibility() {
        xSlider.setMax(getMaxWidth() - getWidth());
        ySlider.setMax(getMaxHeight() - getHeight());
        xSlider.setVisible(getMaxWidth() - getWidth() > 0);
        ySlider.setVisible(getMaxHeight() - getHeight() > 0);
	}

    /**
     * @return Return the maximum effective width by by summing the child components' width
     */
    protected int getMaxWidth()
    {
        int maxWidth = getWidth();

        for(GuiComponent component : getChildComponents())
        {
            int width = component.getX() + component.getWidth();
			
            if(width > maxWidth) {
                maxWidth = width;
            }
        }

        return maxWidth;
    }

    /**
     * @return Return the maximum effective height by by summing the child components' width
     */
    protected int getMaxHeight()
    {
        int maxHeight = getHeight();

        for(GuiComponent component : getChildComponents())
        {
            int height = component.getY() + component.getHeight();

            if(height > maxHeight) {
                maxHeight = height;
            }
        }

        return maxHeight;
    }

	@Override
	public void flushComponentsQueue()
	{
		int oldSize = getChildComponents().size();
		super.flushComponentsQueue();
		if(autoScroll && oldSize != getChildComponents().size())
		{
			xSlider.setValue(xSlider.getMax());
			ySlider.setValue(ySlider.getMax());
		}
	}
	public void scrollXBy(double d)
	{
		xSlider.setValue(xSlider.getValue()+d);
	}
	public void scrollYBy(double amount)
	{
		ySlider.setValue(ySlider.getValue()+amount);
	}
	public GuiSlider getxSlider() {
		return xSlider;
	}
	public GuiSlider getySlider() {
		return ySlider;
	}
}
