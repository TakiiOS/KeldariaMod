/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.event.listeners;

import fr.reden.guiapi.component.GuiResizableButton;

public interface IResizableButtonListener {
	
	void onButtonUpdated(GuiResizableButton.ENUM_RESIZE_SIDE enumResizeSide);
	
}
