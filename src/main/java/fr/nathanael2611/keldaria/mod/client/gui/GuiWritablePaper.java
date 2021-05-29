package fr.nathanael2611.keldaria.mod.client.gui;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketSavePaper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiWritablePaper extends GuiScreen
{

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Keldaria.MOD_ID, "textures/gui/editable_paper.png");

    private GuiTextField titleField;
    private GuiButton signButton;

    private String title, content;
    private boolean isSigned;

    private boolean isUnGrabbed = true;

    public GuiWritablePaper(String content)
    {
        this("", content, false);
    }

    public GuiWritablePaper(String title, String content, boolean isSigned)
    {
        this.title = title;
        this.content = content;
        this.isSigned = isSigned;
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.signButton = new GuiButton(1, 0, 0, "Signer");
        if(!this.isSigned) this.buttonList.add(this.signButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        int baseX = this.width / 2 - 60;
        int baseY = 20;

        if(this.isUnGrabbed)
        {
            Mouse.setGrabbed(false);
            this.isUnGrabbed = false;
        }

        if(this.titleField == null)
        {
            this.titleField = new GuiTextField(0, this.fontRenderer, baseX, baseY + 8, 60 * 2, 10);
            this.titleField.setText(this.title);
            this.titleField.setEnableBackgroundDrawing(false);
            this.titleField.setTextColor(Color.GRAY.darker().darker().getRGB());
            this.titleField.setMaxStringLength(20);
        }

        this.titleField.x = baseX;
        this.titleField.y = baseY + 8;

        this.titleField.setEnabled(!this.isSigned);

        GlStateManager.pushMatrix();
        GlStateManager.translate(baseX, baseY, 0);

        RenderHelpers.drawImage(- 10, - 10, BACKGROUND, 60 * 2 + 20, 150 + 20);

        this.signButton.x = baseX;
        this.signButton.y = baseY + 150 + 40;
        this.signButton.width = 60 * 2;

        this.signButton.enabled = this.titleField.getText().length() > 0;

        this.titleField.setDisabledTextColour(Color.GRAY.darker().darker().getRGB());
        if(this.isSigned) this.titleField.setFocused(false);

        this.fontRenderer.drawSplitString(this.content + (this.isSigned ? "" : (!this.titleField.isFocused() ? (this.content.length() >= 250 ? "" : "_") : "")), 0, 20, 60 * 2, Color.BLACK.getRGB());
        GlStateManager.popMatrix();
        this.titleField.drawTextBox();
        drawRect(this.titleField.x, this.titleField.y + this.titleField.height, this.titleField.x + this.titleField.getWidth(), this.titleField.y + this.titleField.height + 1, Color.BLACK.getRGB());


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.titleField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        if(this.isSigned) return;
        if(this.titleField.isFocused())
        {
            this.titleField.textboxKeyTyped(typedChar, keyCode);
            return;
        }
        if(keyCode == Keyboard.KEY_BACK && this.content.length() > 0)
        {
            if(this.content.length() == 1)
            {
                this.content = "";
            }
            else
            {
                this.content = this.content.substring(0, this.content.length() - 1);
            }
        }
        else if(keyCode == Keyboard.KEY_RETURN && this.content.replace("\n", "                     ").length() < 250)
        {
            this.content += "\n";
        }
        else
        {
            List<Integer> disabledKeys = Lists.newArrayList(Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT, Keyboard.KEY_RETURN, Keyboard.KEY_BACK);
            if(!disabledKeys.contains(keyCode) && this.content.length() < 250)
            {
                this.content += Character.toString(typedChar);
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        this.save(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        this.save(true);
        this.mc.displayGuiScreen(null);
    }

    private void save(boolean sign)
    {
        PacketSavePaper packet = new PacketSavePaper(this.content, sign);
        if(sign) packet.setTitle(this.titleField.getText());
        KeldariaPacketHandler.getInstance().getNetwork().sendToServer(packet);
    }
}
