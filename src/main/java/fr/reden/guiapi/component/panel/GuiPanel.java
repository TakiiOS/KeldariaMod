/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.panel;

import fr.reden.guiapi.component.GuiComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GuiPanel extends GuiComponent {
	
	protected List<GuiComponent> childComponents = new ArrayList<GuiComponent>();
	
	protected List<GuiComponent> queuedComponents = new ArrayList<GuiComponent>();
	protected List<GuiComponent> toRemoveComponents = new ArrayList<GuiComponent>();
	
	public GuiPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	/**
	 * Add a child component to this GuiPanel.
	 * The child component will be updated, rendered, etc,
	 * with its parent.
	 * @param component The child component
	 */
	public GuiPanel add(GuiComponent component) {
		component.setParent(this);
		queuedComponents.add(component);
		return this;
	}
	
	public GuiPanel remove(GuiComponent component) {
		toRemoveComponents.add(component);
		return this;
	}

    public void removeAllChilds()
    {
    	toRemoveComponents.addAll(childComponents);
    }
	
    public List<GuiComponent> getQueuedComponents()
    {
		return queuedComponents;
	}
	
	public void flushComponentsQueue()
	{
		Iterator<GuiComponent> queuedComponentsIterator = queuedComponents.iterator();
		
		while(queuedComponentsIterator.hasNext())
		{
			GuiComponent component = queuedComponentsIterator.next();
			component.updateComponentSize(GuiFrame.resolution.getScaledWidth(), GuiFrame.resolution.getScaledHeight());
			component.updateComponentPosition(GuiFrame.resolution.getScaledWidth(), GuiFrame.resolution.getScaledHeight());
			getChildComponents().add(component);
			
			if(this instanceof GuiScrollPane)
				((GuiScrollPane) this).updateSlidersVisibility();
			
			queuedComponentsIterator.remove();
			
			if(component instanceof GuiPanel) {
				((GuiPanel) component).flushComponentsQueue();
			}
		}
		
		Collections.sort(getChildComponents());
	}
	
	public void flushRemovedComponents()
	{
		Iterator<GuiComponent> toRemoveComponentsIterator = toRemoveComponents.iterator();
		
		while (toRemoveComponentsIterator.hasNext())
		{
			GuiComponent component = toRemoveComponentsIterator.next();
			getChildComponents().remove(component);
			
			toRemoveComponentsIterator.remove();
			
			if(component instanceof GuiPanel) {
				((GuiPanel) component).flushRemovedComponents();
			}
			
			if(this instanceof GuiScrollPane) {
				((GuiScrollPane) this).updateSlidersVisibility();
			}
		}
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY, float partialTicks)
	{
		for (GuiComponent component : getChildComponents()) {
			component.render(mouseX, mouseY, partialTicks);
		}
		
		super.drawForeground(mouseX, mouseY, partialTicks);
	}
	
	public List<GuiComponent> getChildComponents() {
		return childComponents;
	}
	
	public List<GuiComponent> getReversedChildComponents() {
		List<GuiComponent> components = new ArrayList<GuiComponent>();
		if(getChildComponents() != null) {
			components.addAll(getChildComponents());
			Collections.reverse(components);
		}
		return components;
	}
	
}
