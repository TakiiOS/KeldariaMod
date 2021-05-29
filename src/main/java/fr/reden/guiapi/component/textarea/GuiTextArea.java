package fr.reden.guiapi.component.textarea;

import fr.reden.guiapi.GuiAPIClientHelper;
import fr.reden.guiapi.GuiConstants;
import fr.reden.guiapi.component.GuiComponent;
import fr.reden.guiapi.event.listeners.IFocusListener;
import fr.reden.guiapi.event.listeners.IKeyboardListener;
import fr.reden.guiapi.event.listeners.ITickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseClickListener;
import fr.reden.guiapi.event.listeners.mouse.IMouseMoveListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

import static fr.reden.guiapi.GuiAPIClientHelper.glScissor;

public class GuiTextArea extends GuiComponent implements ITickListener, IKeyboardListener, IMouseClickListener, IMouseMoveListener, IFocusListener {

    protected String text = "";
    protected String hintText = "";

    /**Used to allow only a certain type of character or pattern**/
    protected Pattern regexPattern = Pattern.compile("(?s).*");

    protected int maxTextLength;

    protected int cursorCounter;
    protected int cursorIndex;
    protected int selectionEndIndex;

    protected int lineScrollOffsetX;
    protected int lineScrollOffsetY;

    protected int enabledTextColor, disabledTextColor;

    protected boolean editable;

    public GuiTextArea(int x, int y, int width, int height) {
        super(x, y, width, height);
        setEditable(true);
        setBackgroundColor(-16777216);
        setBorderColor(-6250336);
        setBorderSize(1);
        setBorderPosition(BORDER_POSITION.INTERNAL);
        setEnabledTextColor(14737632);
        setDisabledTextColor(7368816);

        setMaxTextLength(140);
        cursorIndex = 0;
        selectionEndIndex = 0;
        lineScrollOffsetX = 0;
        lineScrollOffsetY = 0;

        addTickListener(this);
        addKeyboardListener(this);
        addClickListener(this);
        addMoveListener(this);
        addFocusListener(this);
    }

    @Override
    public void onTick()
    {
        this.cursorCounter++;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks)
    {
        glScissor(getRenderMinX() + getPaddingLeft(), getRenderMinY() + getPaddingTop(), (getRenderMaxX() - getRenderMinX()) - (getPaddingLeft() + getPaddingRight()), (getRenderMaxY() - getRenderMinY()) - (getPaddingTop() + getPaddingBottom()));
        drawTextLines(getRenderedTextLines());

        glScissor(getRenderMinX() + getBorderSize(), getRenderMinY() + getBorderSize(), getRenderMaxX() - getRenderMinX() - getBorderSize(), getRenderMaxY() - getRenderMinY() - getBorderSize());
        drawHintLines();

        if(isEditable() && isFocused()) {
            drawCursor();
            drawSelectedRegion();
        }

        super.drawForeground(mouseX, mouseY, partialTicks);
    }

    protected void drawTextLines(List<String> lines) {

        GlStateManager.enableTexture2D();

        for(int i = 0; i < lines.size(); i++) {
            drawString(mc.fontRenderer, lines.get(i), getScreenX() + getPaddingLeft() - getLineScrollOffsetX(), getScreenY() + i * 9 + getPaddingTop() - getLineScrollOffsetY(), isEnabled() ? getEnabledTextColor() : getDisabledTextColor());
        }
    }

    protected void drawHintLines() {

        GlStateManager.enableTexture2D();

        if(!isFocused() && text.isEmpty()) {
            List<String> hintTextLines = getHintTextLines();

            if(hintTextLines != null) {
                for (int i = 0; i < hintTextLines.size(); i++) {
                    drawString(mc.fontRenderer, TextFormatting.ITALIC + hintTextLines.get(i), getScreenX() + getPaddingLeft(), getScreenY() + i * 9 + getPaddingTop(), Color.GRAY.getRGB());
                }
            }
        }
    }

    @Override
    public void onFocus() {
        cursorCounter = 0;
    }

    @Override public void onFocusLoose() {}

    protected void drawCursor()
    {
        if(cursorCounter / 20 % 2 == 0 && isFocused()) {
            String line = getRenderedTextLines().get(getLine(cursorIndex));
            int cursorPosX = mc.fontRenderer.getStringWidth(line.substring(0, getPosition(cursorIndex))) - lineScrollOffsetX;
            int cursorPosY = getLine(cursorIndex) * 9 - lineScrollOffsetY;
            drawRect(getScreenX() + getPaddingLeft() + cursorPosX, getScreenY() + getPaddingTop() + cursorPosY, getScreenX() + getPaddingLeft() + cursorPosX + 1, getScreenY() + getPaddingTop() + cursorPosY + 9, Color.WHITE.getRGB());
        }
    }

    protected void drawSelectedRegion()
    {
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);

        int cursorLine = getLine(cursorIndex);
        int selectionEndLine = getLine(selectionEndIndex);

        int cursorPosition = getPosition(cursorIndex);
        int selectionEndPosition = getPosition(selectionEndIndex);

        for(int i = Math.min(cursorLine, selectionEndLine); i <= Math.max(cursorLine, selectionEndLine); i++) {

            String line = getRenderedTextLines().get(i);

            int x1 = 0;
            int x2 = mc.fontRenderer.getStringWidth(line);

            if(i == Math.min(cursorLine, selectionEndLine)) {
                if(cursorLine == selectionEndLine) {
                    x1 = mc.fontRenderer.getStringWidth(line.substring(0, Math.min(cursorPosition, selectionEndPosition)));
                } else if(i == cursorLine) {
                    x1 = mc.fontRenderer.getStringWidth(line.substring(0, cursorPosition));
                } else {
                    x1 = mc.fontRenderer.getStringWidth(line.substring(0, selectionEndPosition));
                }
            }

            if(i == Math.max(cursorLine, selectionEndLine)) {

                if(cursorLine == selectionEndLine) {
                    x2 = mc.fontRenderer.getStringWidth(line.substring(0, Math.max(cursorPosition, selectionEndPosition)));
                } else if(i == cursorLine) {
                    x2 = mc.fontRenderer.getStringWidth(line.substring(0, cursorPosition));
                } else {
                    x2 = mc.fontRenderer.getStringWidth(line.substring(0, selectionEndPosition));
                }

            }

            drawRect(getScreenX() + getPaddingLeft() + x1 - lineScrollOffsetX, getScreenY() + getPaddingTop() + i * 9 - lineScrollOffsetY, getScreenX() + getPaddingLeft() + x2 - lineScrollOffsetX, getScreenY() + getPaddingTop() + i * 9 + 9 - lineScrollOffsetY, Color.BLUE.getRGB());

        }

        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public void writeText(String text)
    {
        String part1 = getText().substring(0, Math.min(cursorIndex, selectionEndIndex));
        String part2 = getText().substring(Math.max(cursorIndex, selectionEndIndex));
        setText(part1 + text + part2);

        if(cursorIndex < selectionEndIndex) {
            moveSelectionToCursor();
        } else {
            moveCursorToSelection();
        }
    }

    public GuiTextArea setText(String text)
    {
        if(text.isEmpty() || regexPattern.matcher(text).matches()) {
            this.text = text.substring(0, Math.min(text.length(), maxTextLength));
        } else {
            throw new IllegalStateException(String.format("The text %s doesn't match with the regex %s", text, regexPattern.toString()));
        }

        updateIndexes();

        return this;
    }

    protected void updateIndexes() {
        cursorIndex = MathHelper.clamp(cursorIndex, 0, text.length());
        selectionEndIndex = MathHelper.clamp(selectionEndIndex, 0, text.length());
    }

    protected void moveSelectionToCursor()
    {
        setSelectionEndIndex(cursorIndex);
    }

    protected void moveCursorToSelection() {
        setCursorIndex(selectionEndIndex);
    }

    protected void moveCursorUp()
    {
        setCursorIndex(getIndexAtRelativeLine(cursorIndex, -1));
    }

    protected void moveCursorDown()
    {
        setCursorIndex(getIndexAtRelativeLine(cursorIndex, 1));
    }

    protected void moveSelectionUp()
    {
        setSelectionEndIndex(getIndexAtRelativeLine(selectionEndIndex, -1));
    }

    protected void moveSelectionDown()
    {
        setSelectionEndIndex(getIndexAtRelativeLine(selectionEndIndex, 1));
    }

    /**
     * @param index The absolute index
     * @return Return the line, k [0; getRenderedTextLines().size()-1]
     */
    protected int getLine(int index)
    {
        List<String> lines = getRenderedTextLines();

        int k = 0;
        int l = lines.get(k).length();

        while(index > l && k < lines.size() - 1) {
            k++;
            l += lines.get(k).length();
        }

        return k;
    }

    /**
     * @param index The absolute index
     * @return Return the relative position, k [0; line.length()]
     */
    protected int getPosition(int index)
    {
        List<String> lines = getRenderedTextLines();
        int lineIndex = getLine(index);

        int k = index;

        for(int i = 0; i < lineIndex; i++) {
            k -= lines.get(i).length();
        }

        return k;
    }

    /**
     * @param index The absolute index
     * @param i The number of line to offset by
     * @return Return the new absolute position at actualLine+i
     */
    protected int getIndexAtRelativeLine(int index, int i) {

        List<String> lines = getRenderedTextLines();
        int lineIndex = getLine(index);

        if(lineIndex + i >= 0 && lineIndex + i < lines.size()) {
            int relPosition = getPosition(index);

            int w = mc.fontRenderer.getStringWidth(lines.get(lineIndex).substring(0, relPosition));
            String dstLine = lines.get(lineIndex + i);

            String str0 = mc.fontRenderer.trimStringToWidth(dstLine, w);
            String str1 = dstLine.substring(0, MathHelper.clamp(str0.length() + 1, 0, dstLine.length()));

            int newRelPosition;

            if(Math.abs(w - mc.fontRenderer.getStringWidth(str0)) > Math.abs(w - mc.fontRenderer.getStringWidth(str1))) {
                newRelPosition = str1.length();
            } else {
                newRelPosition = str0.length();
            }

            return getIndexAt(lineIndex + i, newRelPosition);
        }

        return index;
    }


    public void moveCursorBy(int i) {
        setCursorIndex(cursorIndex + i);
    }

    protected void moveSelectionEndBy(int i) {
        setSelectionEndIndex(selectionEndIndex + i);
    }

    public void updateTextOffset()
    {
        int selectionEndLine = getLine(this.selectionEndIndex);
        int selectionEndPosition = getPosition(this.selectionEndIndex);

        String line = getRenderedTextLines().get(selectionEndLine).substring(0, selectionEndPosition);

        int k = mc.fontRenderer.getStringWidth(line) - lineScrollOffsetX;
        int l = selectionEndLine * 9 - lineScrollOffsetY;

        if(k <= 0) {
            lineScrollOffsetX += k;
        } else if(k >= getWidth() - (getPaddingLeft() + getPaddingRight())) {
            lineScrollOffsetX += k - (getWidth() - (getPaddingLeft() + getPaddingRight()));
        }


        int height = getVerticalSize() == GuiConstants.ENUM_SIZE.RELATIVE ? (int) (getRelativeHeight() * getParent().getHeight()) : getHeight();


        if(l <= 0) {
            lineScrollOffsetY += l;
        } else if(l >= height - (getPaddingLeft() + getPaddingRight()) - 9) {
            lineScrollOffsetY += l - (height - (getPaddingTop() + getPaddingBottom())) + 9;
        }

        if(lineScrollOffsetX >= mc.fontRenderer.getStringWidth(line) && line.length() > 0) {
            lineScrollOffsetX = mc.fontRenderer.getStringWidth(line.substring(0, line.length() - 1));
        }
    }

    public int getMaxTextWidth()
    {
        int max = 0;
        List<String> lines = getRenderedTextLines();

        for(String line : lines)
        {
            int width = mc.fontRenderer.getStringWidth(line);
            if(width > max) {
                max = width;
            }
        }

        return max;
    }

    public int getTextHeight() {
        return getRenderedTextLines().size() * 9;
    }

    protected void setSelectionEndIndex(int selectionEndIndex)
    {
        this.selectionEndIndex = selectionEndIndex;
        updateIndexes();
        updateTextOffset();
    }

    protected void setCursorIndex(int cursorIndex) {
        this.cursorIndex = cursorIndex;
        updateIndexes();
    }

    protected int getIndexFromMouse(int mouseX, int mouseY)
    {
        List<String> lines = getRenderedTextLines();

        int d0 = MathHelper.clamp(mouseX - (getScreenX() + getPaddingLeft()), 0, Integer.MAX_VALUE) + lineScrollOffsetX;
        int d1 = mouseY - (getScreenY() + getPaddingTop()) + lineScrollOffsetY;

        int lineIndex = MathHelper.clamp((int) (d1 / 9.0), 0, lines.size() - 1);

        String str0 = mc.fontRenderer.trimStringToWidth(lines.get(lineIndex), d0);
        String str1 = lines.get(lineIndex).substring(0, MathHelper.clamp(str0.length() + 1, 0, lines.get(lineIndex).length()));

        int position;

        if(Math.abs(d0 - mc.fontRenderer.getStringWidth(str0)) > Math.abs(d0 - mc.fontRenderer.getStringWidth(str1))) {
            position = str1.length();
        } else {
            position = str0.length();
        }

        return getIndexAt(lineIndex, position);
    }

    protected int getIndexAt(int line, int position) {
        List<String> lines = getRenderedTextLines();
        int index = 0;

        for (int i = 0; i < line; i++) {
            index += lines.get(i).length();
        }

        return index + position;
    }

    public int getNthWordFromCursor(int n)
    {
        return this.getNthWordFromPos(n, cursorIndex);
    }

    /**
     * Gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int n, int position)
    {
        return this.getWord(n, position);
    }

    public int getWord(int n, int position)
    {
        int i = position;
        boolean flag = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k)
        {
            if (!flag)
            {
                int l = this.text.length();
                i = this.text.indexOf(32, i);

                if (i == -1)
                {
                    i = l;
                }
                else
                {
                    while (i < l && this.text.charAt(i) == 32)
                    {
                        ++i;
                    }
                }
            }
            else
            {
                while (i > 0 && this.text.charAt(i - 1) == 32)
                {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != 32)
                {
                    --i;
                }
            }
        }

        return i;
    }

    public String getText() {
        return text;
    }

    /**
     * @return Simple method to return a different rendered text
     * without modifying the actual text.
     */
    protected String getRenderedText() {
        return getText();
    }

    public String getSelectedText() {
        return text.substring(Math.min(cursorIndex, selectionEndIndex), Math.max(cursorIndex, selectionEndIndex));
    }

    public List<String> getRenderedTextLines()
    {
        return GuiAPIClientHelper.trimTextToWidth(getRenderedText(), getMaxLineLength());
    }

    protected List<String> getHintTextLines() {
        return GuiAPIClientHelper.trimTextToWidth(hintText, getMaxLineLength());
    }

    public static boolean isKeyComboCtrlX(int keyID)
    {
        return keyID == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown();
    }

    public static boolean isKeyComboCtrlV(int keyID)
    {
        return keyID == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown();
    }

    public static boolean isKeyComboCtrlC(int keyID)
    {
        return keyID == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown();
    }

    public static boolean isKeyComboCtrlA(int keyID)
    {
        return keyID == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown();
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode)
    {
        if(!isFocused() || !isEditable()) {
            return;
        }

        cursorCounter = 0;

        if (isKeyComboCtrlA(keyCode))
        {
            setCursorIndex(0);
            setSelectionEndIndex(text.length());
        }
        else if (isKeyComboCtrlC(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());
        }
        else if (isKeyComboCtrlV(keyCode))
        {
            if (isEnabled() && regexPattern.matcher(GuiScreen.getClipboardString()).matches())
            {
                writeText(GuiScreen.getClipboardString());
            }
        }
        else if (isKeyComboCtrlX(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());

            if (isEnabled())
            {
                writeText("");
            }
        }

        switch (keyCode)
        {

            case 14:

                //Delete

                if (!getSelectedText().isEmpty()) {
                    writeText("");
                } else {
                    moveCursorBy(-1);
                    writeText("");
                }

                updateTextOffset();

                break;

            case 203:

                //Left arrow

                if(GuiScreen.isShiftKeyDown())
                {
                    if(GuiScreen.isCtrlKeyDown()) {
                        setSelectionEndIndex(getNthWordFromPos(-1, selectionEndIndex));
                    } else {
                        moveSelectionEndBy(-1);
                    }

                } else if(GuiScreen.isCtrlKeyDown()) {
                    setCursorIndex(getNthWordFromCursor(-1));
                    moveSelectionToCursor();
                } else {
                    if(!getSelectedText().isEmpty()) {
                        if(selectionEndIndex < cursorIndex) {
                            moveCursorToSelection();
                        } else {
                            moveSelectionToCursor();
                        }
                    } else {
                        moveCursorBy(-1);
                        moveSelectionToCursor();
                    }
                }

                break;

            case 205:

                //Right arrow

                if(GuiScreen.isShiftKeyDown()) {

                    if(GuiScreen.isCtrlKeyDown()) {
                        setSelectionEndIndex(getNthWordFromPos(1, selectionEndIndex));
                    } else {
                        moveSelectionEndBy(1);
                    }

                } else if(GuiScreen.isCtrlKeyDown()) {
                    setCursorIndex(getNthWordFromCursor(1));
                    moveSelectionToCursor();
                } else {
                    if(!getSelectedText().isEmpty()) {
                        if(selectionEndIndex < cursorIndex) {
                            moveSelectionToCursor();
                        } else {
                            moveCursorToSelection();
                        }
                    } else {
                        moveCursorBy(1);
                        moveSelectionToCursor();
                    }
                }

                break;

            case 208:

                //Bottom arrow

                if(GuiScreen.isShiftKeyDown()) {
                    moveSelectionDown();
                } else {
                    moveCursorDown();
                    moveSelectionToCursor();
                }

                break;


            case 200:

                //Top arrow

                if(GuiScreen.isShiftKeyDown()) {
                    moveSelectionUp();
                } else {
                    moveCursorUp();
                    moveSelectionToCursor();
                }

                break;

            case 211:

                if(!getSelectedText().isEmpty()) {
                    writeText("");
                } else {
                    moveSelectionEndBy(1);
                    writeText("");
                }

                break;

            default:
                if(ChatAllowedCharacters.isAllowedCharacter(typedChar) && isEnabled() && text.length() < maxTextLength) {
                    if(regexPattern.matcher(getRenderedText() + typedChar).matches()) {
                        writeText(String.valueOf(typedChar));
                        moveCursorBy(1);
                        moveSelectionToCursor();
                    }
                }

                break;
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if(isEditable()) {
            setCursorIndex(getIndexFromMouse(mouseX, mouseY));
            moveSelectionToCursor();
        }
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY)
    {
        if(isEditable() && isPressed()) {
            setSelectionEndIndex(getIndexFromMouse(mouseX, mouseY));
        }
    }

    @Override public void onMouseHover(int mouseX, int mouseY) {}

    @Override public void onMouseUnhover(int mouseX, int mouseY) {}

    public GuiTextArea setHintText(String hintText) {
        this.hintText = hintText;
        return this;
    }

    public boolean isEditable() {
        return editable;
    }

    public Pattern getRegexPattern() {
        return regexPattern;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    public int getMaxLineLength() {
        return MathHelper.clamp(getWidth() - (getPaddingLeft() + getPaddingRight()), 0, Integer.MAX_VALUE);
    }

    public int getEnabledTextColor() {
        return enabledTextColor;
    }

    public int getDisabledTextColor() {
        return disabledTextColor;
    }

    public int getPaddingTop() {
        return 4;
    }

    public int getPaddingBottom() {
        return 4;
    }

    public int getPaddingLeft() {
        return 4;
    }

    public int getPaddingRight() {
        return 4;
    }

    public int getLineScrollOffsetX() {
        return lineScrollOffsetX;
    }

    public GuiTextArea setLineScrollOffsetX(int lineScrollOffsetX) {
        this.lineScrollOffsetX = lineScrollOffsetX;
        return this;
    }

    public int getLineScrollOffsetY() {
        return lineScrollOffsetY;
    }

    public GuiTextArea setLineScrollOffsetY(int lineScrollOffsetY) {
        this.lineScrollOffsetY = lineScrollOffsetY;
        return this;
    }

    public GuiTextArea setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public GuiTextArea setRegexPattern(Pattern regexPattern) {
        this.regexPattern = regexPattern;
        return this;
    }

    public GuiTextArea setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
        return this;
    }

    public GuiTextArea setEnabledTextColor(int enabledTextColor) {
        this.enabledTextColor = enabledTextColor;
        return this;
    }

    public GuiTextArea setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }
}
