/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component;

import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseExtraClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseMoveListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class GuiButton extends GuiComponent implements IMouseClickListener, IMouseMoveListener, IMouseExtraClickListener {

    public static final SoundEvent defaultClickButtonSound = SoundEvents.UI_BUTTON_CLICK;

    protected static final ResourceLocation widgetsTexture = new ResourceLocation("textures/gui/widgets.png");
    protected static final GuiTextureSprite buttonTexture = new GuiTextureSprite(widgetsTexture, 0, 66, 200, 20);
    protected static final GuiTextureSprite buttonHoveredTexture = new GuiTextureSprite(widgetsTexture, 0, 86, 200, 20);
    protected static final GuiTextureSprite buttonPressedTexture = buttonHoveredTexture;
    protected static final GuiTextureSprite buttonDisabledTexture = new GuiTextureSprite(widgetsTexture, 0, 46, 200, 20);

    protected GuiTextureSprite iconTexture, hoveredIconTexture, pressedIconTexture, disabledIconTexture;

    protected int iconWidth, iconHeight;
    protected float iconRelWidth = 1, iconRelHeight = 1;

    protected GuiConstants.ENUM_SIZE iconHorizontalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;
    protected GuiConstants.ENUM_SIZE iconVerticalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;

    protected GuiConstants.ENUM_ICON_POSITION iconPosition = GuiConstants.ENUM_ICON_POSITION.LEFT;

    protected SoundEvent clickButtonSound = defaultClickButtonSound;
    protected SoundEvent releaseButtonSound = null;
    protected SoundEvent hoverButtonSound = null;

    protected int iconPadding = 3;

    protected String text;
    protected int foregroundColor = 14737632;
    protected int hoveredForegroundColor = 16777120;
    protected int pressedForegroundColor = hoveredForegroundColor;
    protected int disabledForegroundColor = 10526880;

    public GuiButton(int x, int y, int width, int height) {
        this(x, y, width, height, "");
    }

    public GuiButton(int x, int y, int width, int height, String text) {
        this(x, y, width, height, text, true);
    }

    public GuiButton(int x, int y, int width, int height, String text, boolean backgroundTexture) {
        super(x, y, width, height);

        setText(text);

        if(backgroundTexture) {
            setTexture(buttonTexture);
            setHoveredTexture(buttonHoveredTexture);
            setPressedTexture(buttonPressedTexture);
            setDisabledTexture(buttonDisabledTexture);
        }

        addClickListener(this);
    }

    @Override
    public void drawTexturedBackground(int mouseX, int mouseY, float partialTicks)
    {
        GuiTextureSprite renderTexture = getRenderTexture();

        if(renderTexture != null) {

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(getBackgroundSrcBlend(), getBackgroundDstBlend());

            int borderSize = 3;
            renderTexture.drawSprite(getScreenX(), getScreenY(), borderSize, borderSize, 0, 0, borderSize, borderSize, borderSize, borderSize);
            renderTexture.drawSprite(getScreenX(), getScreenY() + getHeight() - borderSize, borderSize, borderSize, 0, renderTexture.getTextureHeight() - borderSize, borderSize, borderSize, borderSize, borderSize);
            renderTexture.drawSprite(getScreenX() + getWidth() - borderSize, getScreenY(), borderSize, borderSize, renderTexture.getTextureWidth() - borderSize, 0, borderSize, borderSize, borderSize, borderSize);
            renderTexture.drawSprite(getScreenX() + getWidth() - borderSize, getScreenY() + getHeight() - borderSize, borderSize, borderSize, renderTexture.getTextureWidth() - 3, renderTexture.getTextureHeight() - borderSize, borderSize, borderSize, borderSize, borderSize);

            if(getHeight() > borderSize * 2) {
                renderTexture.drawSprite(getScreenX(), getScreenY() + borderSize, borderSize, getHeight() - borderSize * 2, 0, borderSize, borderSize, renderTexture.getTextureHeight() - borderSize * 2, borderSize, getHeight() - borderSize * 2);
                renderTexture.drawSprite(getScreenX() + getWidth() - borderSize, getScreenY() + borderSize, borderSize, getHeight() - borderSize * 2, renderTexture.getTextureWidth() - borderSize, borderSize, borderSize, renderTexture.getTextureHeight() - borderSize * 2, borderSize, getHeight() - borderSize * 2);
            }

            if(getWidth() > borderSize * 2) {
                renderTexture.drawSprite(getScreenX() + borderSize, getScreenY(), getWidth() - borderSize * 2, borderSize, borderSize, 0, renderTexture.getTextureWidth() - borderSize * 2, borderSize, getWidth() - borderSize * 2, borderSize);
                renderTexture.drawSprite(getScreenX() + borderSize, getScreenY() + getHeight() - borderSize, getWidth() - borderSize * 2, borderSize, borderSize, renderTexture.getTextureHeight() - borderSize, renderTexture.getTextureWidth() - borderSize * 2, borderSize, getWidth() - borderSize * 2, borderSize);
            }

            if(getWidth() > borderSize * 2 || getHeight() > borderSize * 2) {
                renderTexture.drawSprite(getScreenX() + borderSize, getScreenY() + borderSize, renderTexture.getTextureWidth() - borderSize * 2, renderTexture.getTextureHeight() - borderSize * 2, borderSize, borderSize, renderTexture.getTextureWidth() - borderSize * 2, renderTexture.getTextureHeight() - borderSize * 2, getWidth() - borderSize * 2, getHeight() - borderSize * 2);
            }
            //renderTexture.drawSprite(getScreenX(), getScreenY(), getWidth(), getHeight());
            //renderTexture.drawSprite(getScreenX(), getScreenY(), getWidth(), getHeight(), 3, 3, renderTexture.getTextureWidth(), renderTexture.getTextureHeight(), getWidth(), getHeight());

            GlStateManager.disableBlend();

        }

        if(getRenderIcon() != null) {
            GuiTextureSprite renderIconTexture = getRenderIcon();
            int iconX = getIconX(renderIconTexture);
            int iconY = getIconY(renderIconTexture);
            renderIconTexture.drawSprite(getScreenX() + iconX, getScreenY() + iconY, getIconWidth(), getIconHeight());
        }

    }

    @Override
    public void updateComponentSize(int screenWidth, int screenHeight) {
        super.updateComponentSize(screenWidth, screenHeight);

        if(getIconHorizontalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            setIconWidth((int) (getWidth() * getIconRelativeWidth()));
        }

        if(getIconVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            setIconHeight((int) (getHeight() * getIconRelativeHeight()));
        }
    }

    protected int getIconX(GuiTextureSprite renderIconTexture)
    {
        if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.LEFT)
            return (getWidth() - mc.fontRenderer.getStringWidth(getText()) - getIconWidth() - getIconPadding()) / 2;
        else if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.RIGHT)
            return (getWidth() + mc.fontRenderer.getStringWidth(getText()) - getIconWidth() + getIconPadding()) / 2;
        else
            return (getWidth() - getIconWidth()) / 2;

    }

    protected int getIconY(GuiTextureSprite renderIconTexture)
    {
        if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.TOP)
            return (getHeight() - mc.fontRenderer.FONT_HEIGHT * 2 - getIconHeight() - getIconPadding()) / 2;
        else if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.BOTTOM)
            return (getHeight() - mc.fontRenderer.FONT_HEIGHT * 2 + getIconHeight() + getIconPadding()) / 2;
        else
            return (getHeight() - getIconHeight()) / 2;

    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        if(text != null && !text.isEmpty()) {

            int textX = (getWidth() - mc.fontRenderer.getStringWidth(getText())) / 2;
            int textY = (getHeight() - mc.fontRenderer.FONT_HEIGHT) / 2;

            if(getRenderIcon() != null) {
                GuiTextureSprite renderIconTexture = getRenderIcon();
                textX = getTextX(renderIconTexture);
                textY = getTextY(renderIconTexture);
            }

            drawString(mc.fontRenderer, text, getScreenX() + textX, getScreenY() + textY, getRenderForegroundColor());
        }

        super.drawForeground(mouseX, mouseY, partialTicks);
    }

    protected int getTextX(GuiTextureSprite renderIconTexture)
    {
        if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.LEFT)
            return getIconX(renderIconTexture) + getIconWidth() + getIconPadding();
        else if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.RIGHT)
            return (getWidth() - mc.fontRenderer.getStringWidth(getText()) - getIconWidth() - getIconPadding()) / 2;
        else
            return (getWidth() - mc.fontRenderer.getStringWidth(getText())) / 2;

    }

    protected int getTextY(GuiTextureSprite renderIconTexture)
    {
        if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.TOP)
            return getIconY(renderIconTexture) + getIconHeight() + getIconPadding();
        else if(getIconPosition() == GuiConstants.ENUM_ICON_POSITION.BOTTOM)
            return (getHeight() - getIconHeight() - getIconPadding()) / 2;
        else
            return (getHeight() - mc.fontRenderer.FONT_HEIGHT) / 2;

    }

    protected int getRenderForegroundColor()
    {
        int state = getState();

        switch (state) {
            case 0 : return foregroundColor;
            case 1 : return disabledForegroundColor;
            case 2 : return pressedForegroundColor;
            case 3 : return hoveredForegroundColor;
            default : return 14737632;
        }
    }

    protected GuiTextureSprite getRenderIcon() {

        int state = getState();

        switch (state) {
            case 0 : return iconTexture;
            case 1 : return disabledIconTexture != null ? disabledIconTexture : iconTexture;
            case 2 : return pressedIconTexture != null ? pressedIconTexture : iconTexture;
            case 3 : return hoveredIconTexture != null ? hoveredIconTexture : iconTexture;
            default : return iconTexture;
        }
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public GuiButton setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
        return this;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public GuiButton setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
        return this;
    }

    public float getIconRelativeWidth() {
        return iconRelWidth;
    }

    public GuiButton setIconRelativeWidth(float iconRelWidth) {

        setIconHorizontalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.iconRelWidth = MathHelper.clamp(iconRelWidth, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setIconWidth((int) (getIconRelativeWidth() * getParent().getWidth()));
        }

        return this;
    }

    public float getIconRelativeHeight() {
        return iconRelHeight;
    }

    public GuiButton setIconRelativeHeight(float iconRelHeight) {

        setIconVerticalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.iconRelHeight = MathHelper.clamp(iconRelHeight, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setIconHeight((int) (getIconRelativeHeight() * getParent().getHeight()));
        }

        return this;
    }

    public GuiConstants.ENUM_SIZE getIconHorizontalSize() {
        return iconHorizontalSize;
    }

    public GuiButton setIconHorizontalSize(GuiConstants.ENUM_SIZE iconHorizontalSize) {
        this.iconHorizontalSize = iconHorizontalSize;
        return this;
    }

    public GuiConstants.ENUM_SIZE getIconVerticalSize() {
        return iconVerticalSize;
    }

    public GuiButton setIconVerticalSize(GuiConstants.ENUM_SIZE iconVerticalSize) {
        this.iconVerticalSize = iconVerticalSize;
        return this;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            if(clickButtonSound != null)
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(clickButtonSound, 1.0F));
        }
    }

    @Override
    public void onMouseHover(int mouseX, int mouseY) {
        if(hoverButtonSound != null)
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(hoverButtonSound, 1.0F));
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(releaseButtonSound != null)
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(releaseButtonSound, 1.0F));
    }

    @Override
    public void onMouseDoubleClicked(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void onMousePressed(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {}

    @Override
    public void onMouseUnhover(int mouseX, int mouseY) {}

    public SoundEvent getClickButtonSound() {
        return clickButtonSound;
    }

    public GuiButton setClickButtonSound(SoundEvent clickButtonSound) {
        this.clickButtonSound = clickButtonSound;
        return this;
    }

    public SoundEvent getReleaseButtonSound() {
        return releaseButtonSound;
    }

    public GuiButton setReleaseButtonSound(SoundEvent releaseButtonSound) {
        this.releaseButtonSound = releaseButtonSound;
        return this;
    }

    public SoundEvent getHoverButtonSound() {
        return hoverButtonSound;
    }

    public GuiButton setHoverButtonSound(SoundEvent hoverButtonSound) {
        this.hoverButtonSound = hoverButtonSound;
        return this;
    }

    public GuiButton setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        return this;
    }

    public int getIconPadding() {
        return iconPadding;
    }

    public GuiButton setIconTexture(GuiTextureSprite iconTexture) {
        this.iconTexture = iconTexture;
        setIconWidth(iconTexture.getTextureWidth());
        setIconHeight(iconTexture.getTextureHeight());
        return this;
    }

    public GuiButton setHoveredIconTexture(GuiTextureSprite hoveredIconTexture) {
        this.hoveredIconTexture = hoveredIconTexture;
        setIconWidth(hoveredIconTexture.getTextureWidth());
        setIconHeight(hoveredIconTexture.getTextureHeight());
        return this;
    }

    public GuiButton setPressedIconTexture(GuiTextureSprite pressedIconTexture) {
        this.pressedIconTexture = pressedIconTexture;
        setIconWidth(pressedIconTexture.getTextureWidth());
        setIconHeight(pressedIconTexture.getTextureHeight());
        return this;
    }

    public GuiButton setDisabledIconTexture(GuiTextureSprite disabledIconTexture) {
        this.disabledIconTexture = disabledIconTexture;
        setIconWidth(disabledIconTexture.getTextureWidth());
        setIconHeight(disabledIconTexture.getTextureHeight());
        return this;
    }

    public GuiButton setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }

    public GuiButton setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    public GuiButton setHoveredForegroundColor(int hoveredForegroundColor) {
        this.hoveredForegroundColor = hoveredForegroundColor;
        return this;
    }

    public GuiButton setPressedForegroundColor(int pressedForegroundColor) {
        this.pressedForegroundColor = pressedForegroundColor;
        return this;
    }

    public GuiButton setDisabledForegroundColor(int disabledForegroundColor) {
        this.disabledForegroundColor = disabledForegroundColor;
        return this;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public int getHoveredForegroundColor() {
        return hoveredForegroundColor;
    }

    public int getPressedForegroundColor() {
        return pressedForegroundColor;
    }

    public int getDisabledForegroundColor() {
        return disabledForegroundColor;
    }

    public GuiConstants.ENUM_ICON_POSITION getIconPosition() {
        return iconPosition;
    }

    public GuiButton setIconPosition(GuiConstants.ENUM_ICON_POSITION iconPosition) {
        this.iconPosition = iconPosition;
        return this;
    }
}
