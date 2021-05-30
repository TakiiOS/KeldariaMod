/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component;

import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiPanel;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import fr.reden.guiapi.event.ComponentKeyboardEvent;
import fr.reden.guiapi.event.ComponentMouseEvent;
import fr.reden.guiapi.event.ComponentRenderEvent;
import fr.reden.guiapi.event.ComponentStateEvent;
import fr.reden.guiapi.event.listeners.*;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseExtraClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseMoveListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseWheelListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiComponent extends Gui implements Comparable<GuiComponent> {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected GuiPanel parent;

    protected int x, y;
    protected int width, height;

    protected float relX = 0, relY = 0;
    protected float relWidth = 1, relHeight = 1;

    /**
     * Component parent's relative alignment, ABSOLUTE if not relative
     * {@link fr.reden.guiapi.GuiConstants.ENUM_POSITION}
     **/
    protected GuiConstants.ENUM_POSITION horizontalAlignment = GuiConstants.ENUM_POSITION.ABSOLUTE;

    protected GuiConstants.ENUM_RELATIVE_X relativeX = GuiConstants.ENUM_RELATIVE_X.LEFT;

    /**
     * Component parent's relative alignment, ABSOLUTE if not relative
     * {@link fr.reden.guiapi.GuiConstants.ENUM_POSITION}
     **/
    protected GuiConstants.ENUM_POSITION verticalAlignment = GuiConstants.ENUM_POSITION.ABSOLUTE;

    protected GuiConstants.ENUM_RELATIVE_Y relativeY = GuiConstants.ENUM_RELATIVE_Y.TOP;

    protected GuiConstants.ENUM_SIZE horizontalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;

    protected GuiConstants.ENUM_SIZE verticalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;

    /**Offset values of the component, used for GuiScrollPane for example**/
    protected int offsetX, offsetY;

    /**
     * The render zLevel, use to sort the render pipeline, by default the components
     * are rendered in the ordered in which they had been added to their parent.
     **/
    protected int zLevel = 0;

    protected boolean enabled, visible;
    protected boolean hovered, pressed;

    protected boolean focused, canLooseFocus;

    /**
     * hoveredBackgroundColor : -1 to use normal backgroundColor
     */
    private int backgroundColor = Color.WHITE.getRGB(), hoveredBackgroundColor = -1;

    /**Blend function for the textured background**/
    protected int backgroundSrcBlend = GL11.GL_ONE, backgroundDstBlend = GL11.GL_ONE_MINUS_SRC_ALPHA;

    protected boolean repeatBackgroundX = false, repeatBackgroundY = false;

    protected int borderSize = 0;
    protected int borderColor = Color.DARK_GRAY.getRGB();

    public enum BORDER_POSITION {INTERNAL, EXTERNAL }
    protected BORDER_POSITION borderPosition = BORDER_POSITION.EXTERNAL;

    /**Text to display when the component is hovered**/
    protected List<String> hoveringText = new ArrayList<String>();

    protected GuiTextureSprite texture, hoveredTexture, pressedTexture, disabledTexture;
    protected int textureWidth, textureHeight;
    protected float textureRelWidth = 1, textureRelHeight = 1;

    protected GuiConstants.ENUM_SIZE textureHorizontalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;
    protected GuiConstants.ENUM_SIZE textureVerticalSize = GuiConstants.ENUM_SIZE.ABSOLUTE;

    protected final List<IMouseClickListener> clickListeners = new ArrayList<IMouseClickListener>();
    protected final List<IMouseExtraClickListener> extraClickListeners = new ArrayList<IMouseExtraClickListener>();
    protected final List<IMouseMoveListener> moveListeners = new ArrayList<IMouseMoveListener>();
    protected final List<IMouseWheelListener> wheelListeners = new ArrayList<IMouseWheelListener>();
    protected final List<IKeyboardListener> keyboardListeners = new ArrayList<IKeyboardListener>();
    protected final List<ITickListener> tickListeners = new ArrayList<ITickListener>();
    protected final List<IGuiOpenListener> openListeners = new ArrayList<IGuiOpenListener>();
    protected final List<IGuiCloseListener> closeListeners = new ArrayList<IGuiCloseListener>();
    protected final List<IResizeListener> resizeListeners = new ArrayList<IResizeListener>();
    protected final List<IFocusListener> focusListeners = new ArrayList<IFocusListener>();

    public GuiComponent(int x, int y, int width, int height) {
        setEnabled(true);
        setVisible(true);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setCanLooseFocus(true);
    }

    public void updateComponentSize(int screenWidth, int screenHeight) {

        if(getHorizontalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            int parentWidth = getParent() != null ? getParent().getWidth() : screenWidth;
            setWidth((int) (parentWidth * getRelativeWidth()));
        }
        
        if(getVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            int parentHeight = getParent() != null ? getParent().getHeight() : screenHeight;
            setHeight((int) (parentHeight * getRelativeHeight()));
        }

        if(getTextureHorizontalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            setTextureWidth((int) (getWidth() * getTextureRelativeWidth()));
        }

        if(getTextureVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE) {
            setTextureHeight((int) (getHeight() * getTextureRelativeHeight()));
        }

    }

    /**
     * Update the x and y coordinates
     */
    public void updateComponentPosition(int screenWidth, int screenHeight)
    {
        int parentWidth = getParent() != null ? getParent().getWidth() : screenWidth;
        int parentHeight = getParent() != null ? getParent().getHeight() : screenHeight;

        if(getHorizontalAlignment() == GuiConstants.ENUM_POSITION.RELATIVE) {
            setX((int) (parentWidth * getRelativeX()));
        }

        if(getVerticalAlignment() == GuiConstants.ENUM_POSITION.RELATIVE) {
            setY((int) (parentHeight * getRelativeY()));
        }
    }

    /**
     * Used to sort the render pipeline depending on the {@code zLevel}
     */
    @Override
    public int compareTo(GuiComponent other) {
        return getZLevel() == other.getZLevel() ? 0 : getZLevel() > other.getZLevel() ? 1 : -1;
    }

    public final void render(int mouseX, int mouseY, float partialTicks)
    {
        if(isVisible() && !MinecraftForge.EVENT_BUS.post(new ComponentRenderEvent.ComponentRenderAllEvent(this))) {

            bindLayerBounds();

            //if (isVisible()) {
                if (!MinecraftForge.EVENT_BUS.post(new ComponentRenderEvent.ComponentRenderBackgroundEvent(this)))
                    drawBackground(mouseX, mouseY, partialTicks);
                if (!MinecraftForge.EVENT_BUS.post(new ComponentRenderEvent.ComponentRenderForegroundEvent(this)))
                    drawForeground(mouseX, mouseY, partialTicks);
            //}

            unbindLayerBounds();

        }
    }

    public void drawBackground(int mouseX, int mouseY, float partialTicks)
    {
        bindLayerBounds();

        if(getBorderSize() > 0) {
            if(getBorderPosition() == BORDER_POSITION.EXTERNAL) {
                GuiAPIClientHelper.glScissor(getRenderMinX() - getBorderSize(), getRenderMinY() - getBorderSize(), getRenderMaxX() - getRenderMinX() + getBorderSize() * 2, getRenderMaxY() - getRenderMinY() + getBorderSize() * 2);
                GuiAPIClientHelper.drawBorderedRectangle(getScreenX() - getBorderSize(), getScreenY() - getBorderSize(), getScreenX() + getWidth() + getBorderSize(), getScreenY() + getHeight() + getBorderSize(), getBorderSize(), getBackgroundColorForRender(), getBorderColor());
            } else {
                GuiAPIClientHelper.drawBorderedRectangle(getScreenX(), getScreenY(), getScreenX() + getWidth(), getScreenY() + getHeight(), getBorderSize(), getBackgroundColorForRender(), getBorderColor());
            }
        } else {
            GuiScreen.drawRect(getScreenX(), getScreenY(), getScreenX() + getWidth(), getScreenY() + getHeight(), getBackgroundColorForRender());
        }

        GlStateManager.color(1, 1, 1, 1);
        drawTexturedBackground(mouseX, mouseY, partialTicks);
    }

    public void drawTexturedBackground(int mouseX, int mouseY, float partialTicks)
    {
        GuiTextureSprite renderTexture = getRenderTexture();

        if (renderTexture != null) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(getBackgroundSrcBlend(), getBackgroundDstBlend());
            //renderTexture.drawSprite(getScreenX(), getScreenY(), getTextureWidth(), getTextureHeight(), isRepeatBackgroundX() ? getWidth() : getTextureWidth(), isRepeatBackgroundY() ? getHeight() : getTextureHeight());
            renderTexture.drawSprite(getScreenX(), getScreenY(), getWidth(), getHeight());
            GlStateManager.disableBlend();
        }
    }

    public void drawForeground(int mouseX, int mouseY, float partialTicks) {

        if(isHovered() && !hoveringText.isEmpty()) {
            GuiFrame.hoveringText = hoveringText;
        }

    }

    /**
     * Bind the scissor test to render only the child component's part in this component boundaries.
     */
    protected void bindLayerBounds() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GuiAPIClientHelper.glScissor(getRenderMinX(), getRenderMinY(), getRenderMaxX() - getRenderMinX(), getRenderMaxY() - getRenderMinY());
    }

    protected void unbindLayerBounds() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public int getRenderMinX() {
        return getParent() != null ? Math.max(getScreenX(), getParent().getRenderMinX()) : getScreenX();
    }

    public int getRenderMinY() {
        return getParent() != null ? Math.max(getScreenY(), getParent().getRenderMinY()) : getScreenY();
    }

    public int getRenderMaxX() {
        return getParent() != null ? Math.min(getScreenX() + getWidth(), getParent().getRenderMaxX()) : getScreenX() + getWidth();
    }

    public int getRenderMaxY() {
        return getParent() != null ? Math.min(getScreenY() + getHeight(), getParent().getRenderMaxY()) : getScreenY() + getHeight();
    }

    public int getScreenX() {
        return getX() + (getParent() != null ? getParent().getScreenX() : 0) + getOffsetX();
    }

    public int getScreenY() {
        return getY() + (getParent() != null ? getParent().getScreenY() : 0) + getOffsetY();
    }

    /**
     * @return Return the {@link GuiTextureSprite} for render depending
     * on the component state. {@see #getState()}
     */
    protected GuiTextureSprite getRenderTexture()
    {
        int state = getState();

        switch(state) {
            case 0 : return getTexture();
            case 1 : return getDisabledTexture() != null ? getDisabledTexture() : getTexture();
            case 2 : return getPressedTexture() != null ? getPressedTexture() : getTexture();
            case 3 : return getHoveredTexture() != null ? getHoveredTexture() : getTexture();
            default: return getTexture();
        }
    }

    /**
     * @return Return the state of the component
     * 0: Normal/Enabled
     * 1: Disabled
     * 2: Pressed
     * 3: Hovered
     */
    public int getState()
    {
        if(!isEnabled()) return 1;
        else if(isPressed()) return 2;
        else if(isHovered()) return 3;
        else return 0;
    }

    public void resize(int screenWidth, int screenHeight) {
        updateComponentSize(screenWidth, screenHeight);
        updateComponentPosition(screenWidth, screenHeight);

        for(IResizeListener resizeListener : resizeListeners) {
            resizeListener.onResize(screenWidth, screenHeight);
        }

        if(this instanceof GuiPanel) {
            for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                component.resize(screenWidth, screenHeight);
            }
        }
    }

    public void tick()
    {
        if(isVisible() && !MinecraftForge.EVENT_BUS.post(new ComponentStateEvent.ComponentTickEvent(this))) {

            for(ITickListener tickListener : tickListeners) {
                tickListener.onTick();
            }

            if(isPressed() && GuiFrame.press() && !MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMousePressEvent(this, GuiFrame.mouseX, GuiFrame.mouseY, GuiFrame.mouseButton))) {
                for(IMouseExtraClickListener extraClickListener : extraClickListeners) {
                    extraClickListener.onMousePressed(GuiFrame.mouseX, GuiFrame.mouseY, GuiFrame.mouseButton);
                }
            }

            if(this instanceof GuiPanel) {

                ((GuiPanel) this).flushComponentsQueue();
                ((GuiPanel) this).flushRemovedComponents();

                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.tick();
                }
            }

        }
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        if(canInteract() && !MinecraftForge.EVENT_BUS.post(new ComponentKeyboardEvent.ComponentKeyTypeEvent(this, typedChar, keyCode)))
        {
            for (IKeyboardListener keyboardListener : keyboardListeners) {
                keyboardListener.onKeyTyped(typedChar, keyCode);
            }

            if(this instanceof GuiPanel) {
                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    /**
     * @param mouseX X position of the mouse
     * @param mouseY Y position of the mouse
     * @param canBeHovered Return false if another component took the priority (depending on zLevel)
     */
    public final void mouseMoved(int mouseX, int mouseY, boolean canBeHovered) {

        if(canInteract() && !MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseMoveEvent(this, GuiFrame.lastMouseX, GuiFrame.lastMouseY, mouseX, mouseY)))
        {
            for(IMouseMoveListener moveListener : moveListeners) {
                moveListener.onMouseMoved(mouseX, mouseY);
            }

            if (!MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseHoverEvent(this, mouseX, mouseY)))
            {
                boolean wasHovered = isHovered();

                setHovered(isMouseOver(mouseX, mouseY) && canBeHovered);

                if(isHovered() != wasHovered)
                {
                    for(IMouseMoveListener moveListener : moveListeners)
                    {
                        if(isHovered()) {
                            moveListener.onMouseHover(mouseX, mouseY);
                        } else {
                            moveListener.onMouseUnhover(mouseX, mouseY);
                        }
                    }
                }
            }

            if(this instanceof GuiPanel) {

                boolean canBeHovered1 = canBeHovered;

                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {

                    component.mouseMoved(mouseX, mouseY, canBeHovered1);

                    if(component.isHovered()) {
                        canBeHovered1 = false;
                    }
                }

            }
        }

    }

    /**
     * @param mouseX X position of the mouse
     * @param mouseY Y position of the mouse
     * @param mouseButton The pressed mouse button
     * @param canBePressed Return false if another component took the priority (depending on zLevel)
     */
    public final void mouseClicked(int mouseX, int mouseY, int mouseButton, boolean canBePressed)
    {
        if(canInteract() && !MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseClickEvent(this, mouseX, mouseY, mouseButton)))
        {
            if(isHovered() && canBePressed) {
                setFocused(true);
                setPressed(true);

                for(IFocusListener focusListener : focusListeners) {
                    focusListener.onFocus();
                }

                for(IMouseClickListener clickListener : clickListeners) {
                    clickListener.onMouseClicked(mouseX, mouseY, mouseButton);
                }

                if(GuiFrame.doubleClick()) {

                    if(!MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseDoubleClickEvent(this, GuiFrame.lastClickTime, mouseX, mouseY)))
                    {
                        for(IMouseExtraClickListener extraClickListener : extraClickListeners) {
                            extraClickListener.onMouseDoubleClicked(mouseX, mouseY, mouseButton);
                        }
                    }

                }

            } else {

                if(canLooseFocus()) {

                    setFocused(false);

                    for(IFocusListener focusListener : focusListeners) {
                        focusListener.onFocusLoose();
                    }
                }

                setPressed(false);
            }


            if(this instanceof GuiPanel) {

                boolean canBePressed1 = canBePressed;

                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {

                    component.mouseClicked(mouseX, mouseY, mouseButton, canBePressed1);

                    if(component.isPressed()) {
                        canBePressed1 = false;
                    }
                }
            }
        } else {

            if(canLooseFocus()) {
                setFocused(false);
            }

            setPressed(false);

        }
    }

    public final void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {
        if(!MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseReleaseEvent(this, mouseX, mouseY, mouseButton))) {
            setPressed(false);

            for(IMouseExtraClickListener extraClickListener : extraClickListeners) {
                extraClickListener.onMouseReleased(mouseX, mouseY, mouseButton);
            }

            if(this instanceof GuiPanel) {
                for (GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.mouseReleased(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    public final void mouseWheel(int dWheel)
    {
        if(dWheel != 0 && canInteract() && !MinecraftForge.EVENT_BUS.post(new ComponentMouseEvent.ComponentMouseWheelEvent(this, dWheel))) {
            if(isHovered()) {
                for (IMouseWheelListener wheelListener : wheelListeners) {
                    wheelListener.onMouseWheel(dWheel);
                }
            }

            if(this instanceof GuiPanel) {
                for (GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.mouseWheel(dWheel);
                }
            }
        }
    }

    public void guiOpen()
    {
        if(!MinecraftForge.EVENT_BUS.post(new ComponentStateEvent.ComponentOpenEvent(this))) {
            for(IGuiOpenListener openListener : openListeners) {
                openListener.onGuiOpen();
            }

            if(this instanceof GuiPanel) {
                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.guiOpen();
                }
            }
        }
    }

    public void guiClose() {
        if(!MinecraftForge.EVENT_BUS.post(new ComponentStateEvent.ComponentCloseEvent(this))) {
            for (IGuiCloseListener closeListener : closeListeners) {
                closeListener.onGuiClose();
            }

            if(this instanceof GuiPanel) {
                for(GuiComponent component : ((GuiPanel) this).getReversedChildComponents()) {
                    component.guiClose();
                }
            }
        }
    }


    public boolean isMouseOver(int mouseX, int mouseY)
    {
        return mouseX >= getMinHitboxX() && mouseX < getMaxHitboxX() && mouseY >= getMinHitboxY() && mouseY < getMaxHitboxY();
    }

    public boolean canInteract() {
        return isVisible() && isEnabled();
    }

    public boolean isVisible() {
        return visible && (getParent() == null || getParent().isVisible());
    }

    public boolean isEnabled() {
        return enabled && (getParent() == null || getParent().isEnabled());
    }

    public boolean isHovered() {
        return hovered && isVisible() && isEnabled();
    }

    public boolean isPressed() {
        return isVisible() && isEnabled() && pressed;
    }

    public boolean isFocused() {
        return isVisible() && isEnabled() && focused;
    }

    public final GuiComponent setFocused(boolean focused)
    {
        if(isFocused() != focused && !MinecraftForge.EVENT_BUS.post(new ComponentStateEvent.ComponentFocusEvent(this))) {
            this.focused = focused;

            if(this instanceof GuiPanel && !focused) {
                for(GuiComponent component : ((GuiPanel) this).getChildComponents()) {
                    if(component.canLooseFocus()) {
                        component.setFocused(false);
                    }
                }
            }
        }

        return this;
    }

    public boolean canLooseFocus() {
        return canLooseFocus;
    }

    public GuiComponent setCanLooseFocus(boolean canLooseFocus) {
        this.canLooseFocus = canLooseFocus;
        return this;
    }

    public GuiPanel getParent() {
        return parent;
    }

    public GuiComponent setParent(GuiPanel parent) {
        this.parent = parent;
        return this;
    }

    public GuiComponent setX(int x) {
        return setX(x, GuiConstants.ENUM_RELATIVE_X.LEFT);
    }

    public GuiComponent setX(int x, GuiConstants.ENUM_RELATIVE_X relativeX) {
        setEnumRelativeX(relativeX);
        this.x = x;
        return this;
    }

    public GuiComponent setY(int y) {
        return setY(y, GuiConstants.ENUM_RELATIVE_Y.TOP);
    }

    public GuiComponent setY(int y, GuiConstants.ENUM_RELATIVE_Y relativeY) {
        setEnumRelativeY(relativeY);
        this.y = y;
        return this;
    }

    public int getX() {

        if(getParent() != null && getHorizontalAlignment() == GuiConstants.ENUM_POSITION.ABSOLUTE) {
            if(getEnumRelativeX() == GuiConstants.ENUM_RELATIVE_X.RIGHT) {
                return getParent().getWidth() + x - getWidth();
            } else if(getEnumRelativeX() == GuiConstants.ENUM_RELATIVE_X.CENTER) {
                return getParent().getWidth() / 2 + x;
            }
        }

        return x;
    }

    public int getY() {

        if(getParent() != null && getVerticalAlignment() == GuiConstants.ENUM_POSITION.ABSOLUTE) {
            if(getEnumRelativeY() == GuiConstants.ENUM_RELATIVE_Y.BOTTOM) {
                return getParent().getHeight() + y - getHeight();
            } else if(getEnumRelativeY() == GuiConstants.ENUM_RELATIVE_Y.CENTER) {
                return getParent().getHeight() / 2 + y;
            }
        }

        return y;
    }

    public GuiComponent setRelativeX(float relX) {
        return setRelativeX(relX, GuiConstants.ENUM_RELATIVE_X.LEFT);
    }

    public GuiComponent setRelativeX(float relX, GuiConstants.ENUM_RELATIVE_X relativeX) {
        setHorizontalAlignment(GuiConstants.ENUM_POSITION.RELATIVE);

        if (relativeX == GuiConstants.ENUM_RELATIVE_X.RIGHT) {
            relX = 1 - relX;
        }

        this.relX = MathHelper.clamp(relX, 0, Float.MAX_VALUE);
        return this;
    }

    public GuiComponent setEnumRelativeX(GuiConstants.ENUM_RELATIVE_X relativeX) {
        this.relativeX = relativeX;
        return this;
    }

    public GuiConstants.ENUM_RELATIVE_X getEnumRelativeX() {
        return relativeX;
    }

    public float getRelativeX() {
        return relX;
    }

    public GuiComponent setRelativeY(float relY) {
        return setRelativeY(relY, GuiConstants.ENUM_RELATIVE_Y.TOP);
    }

    public GuiComponent setRelativeY(float relY, GuiConstants.ENUM_RELATIVE_Y relativeY) {
        setVerticalAlignment(GuiConstants.ENUM_POSITION.RELATIVE);

        if(relativeY == GuiConstants.ENUM_RELATIVE_Y.BOTTOM) {
            relY = 1 - relY;
        }

        this.relY = MathHelper.clamp(relY, 0, Float.MAX_VALUE);
        return this;
    }

    public GuiComponent setEnumRelativeY(GuiConstants.ENUM_RELATIVE_Y relativeY) {
        this.relativeY = relativeY;
        return this;
    }

    public GuiConstants.ENUM_RELATIVE_Y getEnumRelativeY() {
        return relativeY;
    }

    public float getRelativeY() {
        return relY;
    }

    public GuiComponent setHorizontalAlignment(GuiConstants.ENUM_POSITION horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public GuiConstants.ENUM_POSITION getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public GuiComponent setVerticalAlignment(GuiConstants.ENUM_POSITION verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    public GuiConstants.ENUM_POSITION getVerticalAlignment() {
        return verticalAlignment;
    }

    public GuiComponent setHorizontalSize(GuiConstants.ENUM_SIZE horizontalSize) {
        this.horizontalSize = horizontalSize;
        return this;
    }

    public GuiConstants.ENUM_SIZE getHorizontalSize() {
        return horizontalSize;
    }

    public GuiComponent setVerticalSize(GuiConstants.ENUM_SIZE verticalSize) {
        this.verticalSize = verticalSize;
        return this;
    }

    public GuiConstants.ENUM_SIZE getVerticalSize() {
        return verticalSize;
    }

    public GuiComponent setRelativeWidth(float relWidth) {
        setHorizontalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.relWidth = MathHelper.clamp(relWidth, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setWidth((int) (getRelativeWidth() * getParent().getWidth()));
        }

        return this;
    }

    public GuiComponent setRelativeHeight(float relHeight) {
        setVerticalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.relHeight = MathHelper.clamp(relHeight, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setHeight((int) (getRelativeHeight() * getParent().getHeight()));
        }

        return this;
    }

    public float getRelativeWidth() {
        return relWidth;
    }

    public float getRelativeHeight() {
        return relHeight;
    }

    public GuiComponent setWidth(int width) {

        this.width = width;
        updateComponentPosition(GuiFrame.resolution.getScaledWidth(), GuiFrame.resolution.getScaledHeight());

        if(getParent() instanceof GuiScrollPane) {
            ((GuiScrollPane) getParent()).updateSlidersVisibility();
        }

        return this;
    }

    public GuiComponent setHeight(int height) {

        this.height = height;
        updateComponentPosition(GuiFrame.resolution.getScaledWidth(), GuiFrame.resolution.getScaledHeight());

        if(getParent() instanceof GuiScrollPane) {
            ((GuiScrollPane) getParent()).updateSlidersVisibility();
        }

        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public GuiComponent setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public GuiComponent setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public int getZLevel() {
        return zLevel;
    }

    public GuiComponent setZLevel(int zLevel) {
        this.zLevel = zLevel;
        return this;
    }

    public int getBackgroundColorForRender()
    {
    	return isHovered() && hoveredBackgroundColor != -1 ? getHoveredBackgroundColor() : getBackgroundColor();
    }
    public int getBackgroundColor() {
        return backgroundColor;
    }
    public GuiComponent setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    public int getHoveredBackgroundColor()
    {
        return hoveredBackgroundColor;
    }
    /**
     * -1 to use normal backgroundColor
     */
    public GuiComponent setHoveredBackgroundColor(int backgroundColor)
    {
        this.hoveredBackgroundColor = backgroundColor;
        return this;
    }

    public GuiTextureSprite getTexture() {
        return texture;
    }

    public GuiComponent setTexture(GuiTextureSprite texture) {
        return setTexture(texture, false, false);
    }

    public GuiComponent setTexture(GuiTextureSprite texture, boolean repeatBackgroundX, boolean repeatBackgroundY) {
        this.texture = texture;
        this.repeatBackgroundX = repeatBackgroundX;
        this.repeatBackgroundY = repeatBackgroundY;
        if(texture != null)
        {
	        setTextureWidth(texture.getTextureWidth());
	        setTextureHeight(texture.getTextureHeight());
        }
        return this;
    }

    public GuiTextureSprite getHoveredTexture() {
        return hoveredTexture;
    }

    public GuiComponent setHoveredTexture(GuiTextureSprite hoveredTexture) {
        this.hoveredTexture = hoveredTexture;
        if(hoveredTexture != null)
        {
	        setTextureWidth(hoveredTexture.getTextureWidth());
	        setTextureHeight(hoveredTexture.getTextureHeight());
        }
        return this;
    }

    public GuiTextureSprite getPressedTexture() {
        return pressedTexture;
    }

    public GuiComponent setPressedTexture(GuiTextureSprite pressedTexture) {
        this.pressedTexture = pressedTexture;
        if(pressedTexture != null)
        {
	        setTextureWidth(pressedTexture.getTextureWidth());
	        setTextureHeight(pressedTexture.getTextureHeight());
        }
        return this;
    }

    public GuiTextureSprite getDisabledTexture() {
        return disabledTexture;
    }

    public GuiComponent setDisabledTexture(GuiTextureSprite disabledTexture) {
        this.disabledTexture = disabledTexture;
        return this;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public GuiComponent setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
        return this;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public GuiComponent setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
        return this;
    }

    public float getTextureRelativeWidth() {
        return textureRelWidth;
    }

    public GuiComponent setTextureRelativeWidth(float textureRelWidth) {
        setTextureHorizontalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.textureRelWidth = MathHelper.clamp(textureRelWidth, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setTextureWidth((int) (getTextureRelativeWidth() * getParent().getWidth()));
        }

        return this;
    }

    public float getTextureRelativeHeight() {
        return textureRelHeight;
    }

    public GuiComponent setTextureRelativeHeight(float textureRelHeight) {
        setTextureVerticalSize(GuiConstants.ENUM_SIZE.RELATIVE);
        this.textureRelHeight = MathHelper.clamp(textureRelHeight, 0, Float.MAX_VALUE);

        if(getParent() != null) {
            setTextureHeight((int) (getTextureRelativeHeight() * getParent().getHeight()));
        }

        return this;
    }

    public GuiConstants.ENUM_SIZE getTextureHorizontalSize() {
        return textureHorizontalSize;
    }

    public GuiComponent setTextureHorizontalSize(GuiConstants.ENUM_SIZE textureHorizontalSize) {
        this.textureHorizontalSize = textureHorizontalSize;
        return this;
    }

    public GuiConstants.ENUM_SIZE getTextureVerticalSize() {
        return textureVerticalSize;
    }

    public GuiComponent setTextureVerticalSize(GuiConstants.ENUM_SIZE textureVerticalSize) {
        this.textureVerticalSize = textureVerticalSize;
        return this;
    }

    public int getBackgroundSrcBlend() {
        return backgroundSrcBlend;
    }

    public GuiComponent setBackgroundSrcBlend(int backgroundSrcBlend) {
        this.backgroundSrcBlend = backgroundSrcBlend;
        return this;
    }

    public int getBackgroundDstBlend() {
        return backgroundDstBlend;
    }

    public GuiComponent setBackgroundDstBlend(int backgroundDstBlend) {
        this.backgroundDstBlend = backgroundDstBlend;
        return this;
    }

    public boolean isRepeatBackgroundX() {
        return repeatBackgroundX;
    }

    public boolean isRepeatBackgroundY() {
        return repeatBackgroundY;
    }

    public GuiComponent setBorderPosition(BORDER_POSITION borderPosition) {
        this.borderPosition = borderPosition;
        return this;
    }

    public BORDER_POSITION getBorderPosition() {
        return borderPosition;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public GuiComponent setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        return this;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public GuiComponent setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public GuiComponent setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled)
        	this.hovered = false;
        return this;
    }

    public GuiComponent setVisible(boolean visible) {
        this.visible = visible;

        if(!visible) {
            setPressed(false);
            setHovered(false);
        }
        return this;
    }

    public GuiComponent setHovered(boolean hovered) {
        this.hovered = hovered;
        return this;
    }

    public GuiComponent setPressed(boolean pressed) {
        this.pressed = pressed;
        return this;
    }

    public GuiComponent setHoveringText(List<String> hoveringText) {
        this.hoveringText = hoveringText;
        return this;
    }

    public GuiComponent addClickListener(IMouseClickListener clickListener) {
        this.clickListeners.add(clickListener);
        return this;
    }

    public GuiComponent addExtraClickListener(IMouseExtraClickListener extraClickListener) {
        this.extraClickListeners.add(extraClickListener);
        return this;
    }

    public GuiComponent addMoveListener(IMouseMoveListener moveListener) {
        this.moveListeners.add(moveListener);
        return this;
    }

    public GuiComponent addWheelListener(IMouseWheelListener wheelListener) {
        this.wheelListeners.add(wheelListener);
        return this;
    }

    public GuiComponent addKeyboardListener(IKeyboardListener keyboardListener) {
        this.keyboardListeners.add(keyboardListener);
        return this;
    }

    public GuiComponent addTickListener(ITickListener tickListener) {
        this.tickListeners.add(tickListener);
        return this;
    }

    public GuiComponent addOpenListener(IGuiOpenListener openListener) {
        this.openListeners.add(openListener);
        return this;
    }

    public GuiComponent addCloseListener(IGuiCloseListener closeListener) {
        this.closeListeners.add(closeListener);
        return this;
    }

    public GuiComponent addResizeListener(IResizeListener resizeListener) {
        this.resizeListeners.add(resizeListener);
        return this;
    }

    public GuiComponent addFocusListener(IFocusListener focusListener) {
        this.focusListeners.add(focusListener);
        return this;
    }

    public List<IMouseClickListener> getClickListeners() {
        return clickListeners;
    }

    public List<IMouseExtraClickListener> getExtraClickListeners() {
        return extraClickListeners;
    }

    public List<IMouseMoveListener> getMoveListeners() {
        return moveListeners;
    }

    public List<IMouseWheelListener> getWheelListeners() {
        return wheelListeners;
    }

    public List<IKeyboardListener> getKeyboardListeners() {
        return keyboardListeners;
    }

    public List<ITickListener> getTickListeners() {
        return tickListeners;
    }

    public List<IGuiOpenListener> getOpenListeners() {
        return openListeners;
    }

    public List<IGuiCloseListener> getCloseListeners() {
        return closeListeners;
    }

    public List<IResizeListener> getResizeListeners() {
        return resizeListeners;
    }

    public List<IFocusListener> getFocusListeners() {
        return focusListeners;
    }

    public int getMinHitboxX() {

        if(this instanceof GuiPanel) {

            int renderMinX = getRenderMinX();

            for(GuiComponent component : ((GuiPanel)this).getReversedChildComponents()) {
                if(component.isVisible() && component.getMinHitboxX() < renderMinX){
                    renderMinX = component.getMinHitboxX();
                }
            }

            return renderMinX;
        } else {
            return getRenderMinX();
        }

    }

    public int getMinHitboxY() {

        if(this instanceof GuiPanel) {

            int renderMinY = getRenderMinY();

            for(GuiComponent component : ((GuiPanel)this).getReversedChildComponents()) {
                if(component.isVisible() && component.getMinHitboxY() < renderMinY){
                    renderMinY = component.getMinHitboxY();
                }
            }

            return renderMinY;
        } else {
            return getRenderMinY();
        }

    }

    public int getMaxHitboxX() {

        if(this instanceof GuiPanel) {

            int renderMaxX = getRenderMaxX();

            for(GuiComponent component : ((GuiPanel)this).getReversedChildComponents()) {
                if(component.isVisible() && component.getMaxHitboxX() > renderMaxX){
                    renderMaxX = component.getMaxHitboxX();
                }
            }

            return renderMaxX;
        } else {
            return getRenderMaxX();
        }

    }

    public int getMaxHitboxY() {

        if(this instanceof GuiPanel) {

            int renderMaxY = getRenderMaxY();

            for(GuiComponent component : ((GuiPanel)this).getReversedChildComponents()) {
                if(component.isVisible() && component.getMaxHitboxY() > renderMaxY){
                    renderMaxY = component.getMaxHitboxY();
                }
            }

            return renderMaxY;
        } else {
            return getRenderMaxY();
        }

    }

}
