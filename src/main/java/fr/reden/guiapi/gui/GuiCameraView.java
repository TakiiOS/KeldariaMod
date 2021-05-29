package fr.reden.guiapi.gui;

import org.lwjgl.opengl.GL11;

import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.panel.GuiFrame;
import net.minecraft.client.gui.Gui;

public class GuiCameraView extends GuiComponent {
	
	public GuiCameraView(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTicks)
	{
		super.drawBackground(mouseX, mouseY, partialTicks);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, GuiFrame.worldRenderBuffer.framebufferTexture);
		Gui.drawModalRectWithCustomSizedTexture(getScreenX(), getScreenY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
	}
	
}
