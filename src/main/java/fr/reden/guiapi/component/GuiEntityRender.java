package fr.reden.guiapi.component;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;

public class GuiEntityRender extends GuiComponent {
	
	protected EntityLivingBase entity;
	protected int paddingTop;
	protected int paddingBottom;
	
	public GuiEntityRender(int x, int y, int width, int height) {
		this(x, y, width, height, null);
	}
	
	public GuiEntityRender(int x, int y, int width, int height, EntityLivingBase entity) {
		super(x, y, width, height);
		setEntity(entity);
		int padding = (int) (0.125 * getHeight());
		setPaddingTop(padding);
		setPaddingBottom(padding);
	}
	
    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        if(entity != null) {
            int scale = (int) ((getHeight() - (paddingBottom + paddingTop)) / entity.height);
            int x = getScreenX() + getWidth() / 2;
            int y = getScreenY() + getHeight() - paddingBottom;
            int mX = getScreenX() + getWidth() / 2 - mouseX;
            int mY = (int) (y - mouseY - entity.getEyeHeight() * scale);
            GuiInventory.drawEntityOnScreen(x, y, scale, mX, mY, entity);
        }

        super.drawForeground(mouseX, mouseY, partialTicks);
    }

    public GuiEntityRender setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        return this;
    }

    public GuiEntityRender setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        return this;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public GuiEntityRender setEntity(EntityLivingBase entity) {
        this.entity = entity;
        return this;
    }

}
