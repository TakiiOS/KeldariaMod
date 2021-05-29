package fr.reden.guiapi.event.listeners.mouse;

public interface IMouseMoveListener {
	
	void onMouseMoved(int mouseX, int mouseY);
	void onMouseHover(int mouseX, int mouseY);
	void onMouseUnhover(int mouseX, int mouseY);
	
}
