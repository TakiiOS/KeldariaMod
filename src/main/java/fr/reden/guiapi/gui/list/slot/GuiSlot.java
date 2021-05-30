/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.gui.list.slot;

import fr.reden.guiapi.component.panel.GuiPanel;
import fr.reden.guiapi.event.listeners.IFocusListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.gui.GuiList;

public abstract class GuiSlot extends GuiPanel implements IFocusListener, IMouseClickListener {
	
	protected final GuiList list;
	protected final String entryName;
	protected final int entryId;
	
	public GuiSlot(GuiList list, int x, int y, int width, int height, int entryId, String entryName)
	{
		super(x, y, width, height);
		this.list = list;
		this.entryId = entryId;
		this.entryName = entryName;
		setCanLooseFocus(false);
		addFocusListener(this);
		addClickListener(this);
	}
	
	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		list.updateFocus(entryId);
	}
	
	@Override
	public void onFocus() {
		list.setSelectedEntryId(entryId);
	}
	
	@Override
	public void onFocusLoose() {
		list.setSelectedEntryId(-1);
	}
	
	public String getEntryName() {
		return entryName;
	}
	
	public int getEntryId() {
		return entryId;
	}
	
}