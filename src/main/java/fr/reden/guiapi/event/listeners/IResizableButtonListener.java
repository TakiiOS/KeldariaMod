package fr.reden.guiapi.event.listeners;

import fr.reden.guiapi.component.GuiResizableButton;

public interface IResizableButtonListener {
	
	void onButtonUpdated(GuiResizableButton.ENUM_RESIZE_SIDE enumResizeSide);
	
}
