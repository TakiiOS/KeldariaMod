package fr.reden.guiapi.component.panel;

import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.event.listeners.IFocusListener;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiComboBox extends GuiPanel {
	
	protected GuiComboBoxButton guiComboBoxButton;
	
	protected List<String> entries = new ArrayList<String>();
	protected List<GuiEntryButton> entriesButton = new ArrayList<GuiEntryButton>();
	
	protected boolean developed = false;
	
	protected int selectedEntry = -1;
	
	protected String defaultText = "";
	
	public GuiComboBox(int x, int y, int width, int height, String defaultText, List<String> entries) {
		super(x, y, width, height);
		this.defaultText = defaultText;
		
		setBackgroundColor(new Color(0,0,0,0).getRGB());
		
		guiComboBoxButton = new GuiComboBoxButton(defaultText);
		add(guiComboBoxButton);
		
		setEntries(entries);
		setSelectedEntry(-1);
	}
	
	public class GuiComboBoxButton extends GuiButton implements IFocusListener {
		
		public GuiComboBoxButton(String defaultText) {
			super(0, 0, 0, 0, defaultText, true);
			setRelativeWidth(1);
			setRelativeHeight(1);
			addFocusListener(this);
		}
		
		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			super.onMouseClicked(mouseX, mouseY, mouseButton);
			
			if(!isDeveloped()) {
				developComboBox();
			} else {
				retractComboBox();
			}
		}
		
		@Override public void onFocus() {}
		
		@Override
		public void onFocusLoose() {
			retractComboBox();
		}
	}
	
	/**
	 * Unroll the combo box
	 */
	public void developComboBox()
	{
		if(!isDeveloped()) {
			int comboBoxHeight;
			
			if (guiComboBoxButton.getVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
				comboBoxHeight = (int) (guiComboBoxButton.getParent().getHeight() * guiComboBoxButton.getRelativeHeight());
			} else {
				comboBoxHeight = guiComboBoxButton.getHeight();
			}
			
			setHeight(comboBoxHeight + sumEntriesButtonHeight());
			guiComboBoxButton.setHeight(comboBoxHeight);
			guiComboBoxButton.setVerticalSize(GuiConstants.ENUM_SIZE.ABSOLUTE);
			
			for (GuiEntryButton entryButton : entriesButton) {
				entryButton.setY(comboBoxHeight + 15 * entryButton.getEntryId());
				entryButton.setVisible(true);
			}
			
			developed = true;
		}
	}
	
	/**
	 * Repack the combo box
	 */
	public void retractComboBox()
	{
		if(isDeveloped()) {
			setHeight(getHeight() - sumEntriesButtonHeight());
			guiComboBoxButton.setRelativeHeight(1);
			
			for(GuiEntryButton entryButton : entriesButton) {
				entryButton.setVisible(false);
			}
			
			developed = false;
		}
	}
	
	public GuiComboBox setSelectedEntry(int n)
	{
		if(n >= entries.size() || n < 0) {
			selectedEntry = -1;
			guiComboBoxButton.setText(defaultText);
		} else {
			selectedEntry = n;
			guiComboBoxButton.setText(getEntryButton(n).getText());
		}
		return this;
	}
	
	public GuiComboBox setEntries(List<String> entries)
	{
		for(String entry : this.entries) {
			removeEntry(entry);
		}
		
		if(entries != null) {
			for (String entry : entries) {
				addEntry(entry);
			}
		}
		return this;
	}
	
	public void addEntry(String entry) {
		entries.add(entry);
		updateEntriesButtons();
	}
	
	public void removeEntry(String entry) {
		entries.remove(entry);
		updateEntriesButtons();
	}
	
	protected void updateEntriesButtons()
	{
		removeEntriesButtons();
		
		for(String entry : entries) {
			GuiEntryButton entryButton = getNewEntryButton(entries.indexOf(entry), entry);
			entryButton.setRelativeWidth(1);
			entriesButton.add(entryButton);
			
			if(!isDeveloped())
				entryButton.setVisible(false);
			
			add(entryButton);
		}
	}
	
	public GuiEntryButton getEntryButton(int n) {
		
		for(GuiEntryButton entryButton : entriesButton) {
			if(entryButton.getEntryId() == n) {
				return entryButton;
			}
		}
		
		return null;
	}
	
	protected void removeEntriesButtons()
	{
		for(GuiButton entry : entriesButton) {
			remove(entry);
		}
		
		entriesButton.clear();
	}
	
	protected int sumEntriesButtonHeight() {
		
		int height = 0;
		
		for(GuiEntryButton entryButton : entriesButton) {
			height += entryButton.getHeight();
		}
		
		return height;
		
	}
	
	public List<String> getEntries() {
		return entries;
	}
	
	public int getSelectedEntry() {
		return selectedEntry;
	}
	
	public boolean isDeveloped() {
		return developed;
	}
	
	public GuiEntryButton getNewEntryButton(int n, String entry) {
		return new GuiBasicEntryButton(n, entry);
	}
	
	public class GuiBasicEntryButton extends GuiEntryButton {
		
		public GuiBasicEntryButton(int n, String entryName) {
			super(n, entryName);
			setRelativeWidth(1);
			setHeight(15);
			setBackgroundColor(new Color(112, 112, 112, 255).getRGB());
			setBackgroundSrcBlend(GL11.GL_DST_COLOR);
			setBackgroundDstBlend(GL11.GL_ZERO);
			setForegroundColor(new Color(198, 198, 198,255).getRGB());
		}
	}
	
	public abstract class GuiEntryButton extends GuiButton {
		
		protected int entryId;
		
		public GuiEntryButton(int n, String entryName) {
			super(0, 0, 0, 0, entryName);
			setRelativeWidth(1);
			this.entryId = n;
		}
		
		public int getEntryId() {
			return entryId;
		}
		
		public GuiEntryButton setRelativeHeight(float height) {
			System.out.println("Can't set a relative height to an entry button.");
			return this;
		}
		
		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
			super.onMouseClicked(mouseX, mouseY, mouseButton);
			setSelectedEntry(getEntryId());
			retractComboBox();
		}
		
		@Override
		public int getRenderMinX() {
			return getScreenX();
		}
		
		@Override
		public int getRenderMaxX() {
			return getScreenX() + getWidth();
		}
		
		@Override
		public int getRenderMinY() {
			return getScreenY();
		}
		
		@Override
		public int getRenderMaxY() {
			return getScreenY() + getHeight();
		}
		
	}
	
}
