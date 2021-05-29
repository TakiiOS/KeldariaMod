package fr.reden.guiapi.gui.list.slot;

import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.component.textarea.GuiLabel;
import fr.reden.guiapi.gui.GuiList;

import java.awt.*;

public class GuiBasicSlot extends GuiSlot
{
	protected GuiLabel entryNameLabel;
	
	public GuiBasicSlot(GuiList list, int n, String entryName) {
		super(list, 0, 30 * n + list.getListPaddingTop(), 0, 25, n, entryName);
		
		setRelativeWidth(0.5f).setRelativeX(0.25f);
		
		setBackgroundColor(new Color(0,0,0,0.3f).getRGB());
		setBorderSize(1);
		setBorderPosition(GuiComponent.BORDER_POSITION.INTERNAL);
		setBorderColor(new Color(206, 206, 206,255).getRGB());
		
		entryNameLabel = new GuiLabel(0,8, 0,9, entryName);
		entryNameLabel.setRelativeWidth(1);
		entryNameLabel.setHorizontalTextAlignment(GuiConstants.HORIZONTAL_TEXT_ALIGNMENT.CENTER);
		
		add(entryNameLabel);
	}
	
	@Override
	public void onFocus() {
		super.onFocus();
		setBackgroundColor(new Color(0,0,0,0.6f).getRGB());
	}
	
	@Override
	public void onFocusLoose() {
		setBackgroundColor(new Color(0,0,0,0.3f).getRGB());
	}
}
