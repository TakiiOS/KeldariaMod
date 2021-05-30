/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.panel;

import java.util.HashMap;

import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.event.listeners.IGuiCloseListener;
import fr.reden.guiapi.event.listeners.IGuiOpenListener;
import fr.reden.guiapi.event.listeners.IKeyboardListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseExtraClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseMoveListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseWheelListener;
import net.minecraft.util.math.MathHelper;

public class GuiTabbedPane extends GuiPanel implements IGuiOpenListener, IGuiCloseListener, IKeyboardListener, IMouseWheelListener, IMouseClickListener, IMouseExtraClickListener, IMouseMoveListener {
	
	protected HashMap<String, GuiTabbedPaneButton> tabsButtons = new HashMap<String, GuiTabbedPaneButton>();
	protected HashMap<String, GuiPanel> tabsContainers = new HashMap<String, GuiPanel>();
	
	public GuiTabbedPane(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		addOpenListener(this);
		addCloseListener(this);
		addKeyboardListener(this);
		addWheelListener(this);
		addClickListener(this);
		addExtraClickListener(this);
		addMoveListener(this);
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks)
	{
		super.drawBackground(mouseX, mouseY, partialTicks);
		
		for(String str : tabsContainers.keySet()) {
			GuiPanel tabContainer = tabsContainers.get(str);
			tabContainer.setOffsetY(10);
			tabContainer.render(mouseX, mouseY, partialTicks);
		}
	}
	
	public void addTab(String tabName, GuiPanel tabContainer)
	{
		int width = mc.fontRenderer.getStringWidth(tabName) + 6;
		
		int x = 0;
		
		for(String str : tabsContainers.keySet()) {
			x += mc.fontRenderer.getStringWidth(str) + 6;
		}
		
		GuiTabbedPaneButton tabButton = new GuiTabbedPaneButton(x, 0, width, 10);
		tabButton.setText(tabName);
		add(tabButton);
		tabsButtons.put(tabName, tabButton);
		
		tabContainer.setParent(this);
		tabsContainers.put(tabName, tabContainer);
		
		selectTab(tabName);
	}
	
	public GuiPanel getTabContainer(String tabName)
	{
		if(tabsContainers.containsKey(tabName)) {
			return tabsContainers.get(tabName);
		}  else {
			return null;
		}
	}
	
	public GuiTabbedPaneButton getTabButton(String tabName)
	{
		if(tabsButtons.containsKey(tabName)) {
			return tabsButtons.get(tabName);
		}  else {
			return null;
		}
	}
	
	public void selectTab(String tabName)
	{
		for(String str : tabsContainers.keySet()) {
			if(str.equals(tabName)) {
				tabsContainers.get(str).setVisible(true);
				tabsButtons.get(str).setForegroundColor(tabsButtons.get(str).getHoveredForegroundColor());
			} else {
				tabsContainers.get(str).setVisible(false);
				tabsButtons.get(str).setForegroundColor(14737632);
			}
		}
	}
	
	public void selectTab(int tabIndex) {
		tabIndex = MathHelper.clamp(tabIndex, 0, tabsContainers.size());
		selectTab((String) tabsContainers.keySet().toArray()[tabIndex]);
	}
	
	protected class GuiTabbedPaneButton extends GuiButton {
		
		protected GuiTabbedPaneButton(int x, int y, int width, int height) {
			super(x, y, width, height);
		}
		
		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
			super.onMouseClicked(mouseX, mouseY, mouseButton);
			selectTab(getText());
		}
		
	}
	
	@Override
	public void onGuiOpen()
	{
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.guiOpen();
		}
		
		for(String str : tabsButtons.keySet()) {
			GuiTabbedPaneButton button = tabsButtons.get(str);
			button.guiOpen();
		}
	}
	
	@Override
	public void onGuiClose() {
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.guiClose();
		}
		
		for(String str : tabsButtons.keySet()) {
			GuiTabbedPaneButton button = tabsButtons.get(str);
			button.guiClose();
		}
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public void onMouseWheel(int dWheel) {
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.mouseWheel(dWheel);
		}
	}
	
	@Override
	public void onMouseMoved(int mouseX, int mouseY) {
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.mouseMoved(mouseX, mouseY, true);
		}
		
		for(String str : tabsButtons.keySet()) {
			GuiTabbedPaneButton button = tabsButtons.get(str);
			button.mouseMoved(mouseX, mouseY, true);
		}
	}
	
	@Override
	public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.mouseReleased(mouseX, mouseY, mouseButton);
		}
		
		for(String str : tabsButtons.keySet()) {
			GuiTabbedPaneButton button = tabsButtons.get(str);
			button.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (String str : tabsContainers.keySet()) {
			GuiPanel container = tabsContainers.get(str);
			container.mouseClicked(mouseX, mouseY, mouseButton, true);
		}
		
		for (String str : tabsButtons.keySet()) {
			GuiTabbedPaneButton button = tabsButtons.get(str);
			button.mouseClicked(mouseX, mouseY, mouseButton, true);
		}
	}
	
	@Override public void onMouseHover(int mouseX, int mouseY) {}
	
	@Override public void onMouseUnhover(int mouseX, int mouseY) {}
	
	@Override public void onMouseDoubleClicked(int mouseX, int mouseY, int mouseButton) {}
	
	@Override public void onMousePressed(int mouseX, int mouseY, int mouseButton) {}
	
}
