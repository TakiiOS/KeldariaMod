/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.panel;

import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.component.panel.container.GuiContainer;
import fr.reden.guiapi.event.listeners.IKeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class GuiFrame extends GuiPanel implements IKeyboardListener {
	
	/**The instance of the GuiScreen linked to this GuiFrame**/
	protected final APIGuiScreen guiScreen;
	
	public static ScaledResolution resolution = new ScaledResolution(mc);
	public static final Framebuffer worldRenderBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
	
	protected boolean pauseGame = true;
	protected boolean allowUserInputs = false;
	protected boolean enableRepeatEvents = true;
	protected boolean escapeQuit = true;
	
	public static long lastClickTime;
	public static int mouseX, mouseY;
	public static int mouseButton;
	public static int lastMouseX, lastMouseY;
	public static int lastPressedX, lastPressedY;
	public static List<String> hoveringText;
    public static APIGuiScreen lastOpenedGuiScreen;
	
	public GuiFrame() {
		this(0,0,0,0);
		setRelativeWidth(1);
		setRelativeHeight(1);
	}
	
	public GuiFrame(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.guiScreen = new APIGuiScreen(this);
		setFocused(true);
		addKeyboardListener(this);

	}
	
	@Override
	public boolean isFocused() {
		return true;
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode)
	{
		if(keyCode == 1 && doesEscapeQuit()) {
			mc.displayGuiScreen(null);
		}
	}

	public boolean isAllowingUserInputs() {
		return allowUserInputs;
	}

	public void setAllowUserInputs(boolean allowUserInputs) {
		this.allowUserInputs = allowUserInputs;
	}

	public GuiFrame enableRepeatEvents(boolean enableRepeatEvents) {
		this.enableRepeatEvents = enableRepeatEvents;
		return this;
	}
	
	public static boolean doubleClick() {
		return Minecraft.getSystemTime() - GuiFrame.lastClickTime <= 500;
	}
	
	public static boolean press() {
		return Minecraft.getSystemTime() - GuiFrame.lastClickTime <= 500;
	}
	
	/**
	 * Instance of the GuiScreen linked to this GuiFrame
	 */
	
	public class APIGuiScreen extends GuiScreen {
		
		protected final GuiFrame frame;

		APIGuiScreen(GuiFrame frame) {
			this.frame = frame;
			frame.guiOpen();
		}
		
		@Override
		public void setWorldAndResolution(Minecraft mc, int width, int height) {
			super.setWorldAndResolution(mc, width, height);
			resolution = new ScaledResolution(mc);
			frame.resize(width, height);
			worldRenderBuffer.createBindFramebuffer(width * resolution.getScaleFactor(), height * resolution.getScaleFactor());
		}
		
		/**
		 * Store the Minecraft's game render in the buffer
		 */
		private void updateWorldRenderBuffer()
		{
			Framebuffer mcBuffer = mc.getFramebuffer();
			worldRenderBuffer.bindFramebuffer(true);
			mcBuffer.bindFramebufferTexture();
			Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), resolution.getScaledWidth(), resolution.getScaledHeight());
			mcBuffer.unbindFramebufferTexture();
			mcBuffer.bindFramebuffer(true);
		}
		
		@Override
		public void updateScreen()
		{
			frame.tick();
		}
		
		@Override
		public void initGui() {
			flushComponentsQueue();
			flushRemovedComponents();
			Keyboard.enableRepeatEvents(enableRepeatEvents);
		}
		
		@Override
		public void onGuiClosed() {
			enableRepeatEvents(false);
			frame.setVisible(false);
			frame.guiClose();
            lastOpenedGuiScreen = this;
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			//updateWorldRenderBuffer();
			hoveringText = null;

			allowUserInput = frame.isAllowingUserInputs();


			frame.setHovered(false);
			
			GuiFrame.mouseX = mouseX;
			GuiFrame.mouseY = mouseY;
			if (mouseX != lastMouseX || mouseY != lastMouseY) {
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				frame.mouseMoved(mouseX, mouseY, true);
			}
			
			frame.render(mouseX, mouseY, partialTicks);
			
			if(hoveringText != null && !hoveringText.isEmpty())
				GuiAPIClientHelper.drawHoveringText(hoveringText, mouseX, mouseY);

			
		}
		
		@Override
		public void handleMouseInput() throws IOException
		{
			super.handleMouseInput();
			frame.mouseWheel(Mouse.getEventDWheel());
		}
		
		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			frame.mouseClicked(mouseX, mouseY, mouseButton, true);
			GuiFrame.mouseButton = mouseButton;
			lastClickTime = Minecraft.getSystemTime();
			lastPressedX = mouseX;
			lastPressedY = mouseY;
		}
		@Override
		protected void mouseReleased(int mouseX, int mouseY, int state)
		{
			frame.mouseReleased(mouseX, mouseY, mouseButton);
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode)
		{
			frame.keyTyped(typedChar, keyCode);
		}
		
		@Override
		public boolean doesGuiPauseGame() {
			return frame.doesPauseGame();
		}
		
		public GuiFrame getFrame() {
			return frame;
		}
		
	}

	public class APIGuiContainer extends net.minecraft.client.gui.inventory.GuiContainer
	{

		private APIGuiScreen delegate;
		private GuiContainer container;

		APIGuiContainer(APIGuiScreen screen, GuiContainer frame)
		{
			super(frame.inventorySlots);
			this.delegate = screen;
			this.container = frame;

		}

		@Override
		public void setWorldAndResolution(Minecraft mc, int width, int height) {
delegate.setWorldAndResolution(mc, width, height);		}

		/**
		 * Store the Minecraft's game render in the buffer
		 */
		private void updateWorldRenderBuffer()
		{
			delegate.updateWorldRenderBuffer();
		}

		@Override
		public void updateScreen()
		{
			delegate.updateScreen();
		}

		@Override
		public void initGui() {
			delegate.initGui();
		}

		@Override
		public void onGuiClosed() {
			delegate.onGuiClosed();
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			delegate.drawScreen(mouseX, mouseY, partialTicks);
			renderHoveredToolTip(mouseX, mouseX);

		}

		@Override
		public void handleMouseInput() throws IOException
		{
			delegate.handleMouseInput();
		}

		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			delegate.mouseClicked(mouseX, mouseY, mouseButton);
		}
		@Override
		protected void mouseReleased(int mouseX, int mouseY, int state)
		{
			delegate.mouseReleased(mouseX, mouseY, state);
		}

		@Override
		protected void keyTyped(char typedChar, int keyCode)
		{
			delegate.keyTyped(typedChar, keyCode);
		}

		@Override
		public boolean doesGuiPauseGame() {
			return delegate.doesGuiPauseGame();
		}

		public GuiFrame getFrame() {
			return container;
		}

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
		{

		}
	}
	
	public boolean doesPauseGame() {
		return pauseGame;
	}
	
	public GuiFrame setPauseGame(boolean pauseGame) {
		this.pauseGame = pauseGame;
		return this;
	}
	
	public boolean doesEscapeQuit() {
		return escapeQuit;
	}
	
	public GuiFrame setEscapeQuit(boolean escapeQuit) {
		this.escapeQuit = escapeQuit;
		return this;
	}
	
	public APIGuiScreen getGuiScreen() {
		return guiScreen;
	}
	
}
