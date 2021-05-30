/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.event.listeners.mouse;

public interface IMouseMoveListener {
	
	void onMouseMoved(int mouseX, int mouseY);
	void onMouseHover(int mouseX, int mouseY);
	void onMouseUnhover(int mouseX, int mouseY);
	
}
