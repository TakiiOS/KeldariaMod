package fr.nathanael2611.keldaria.mod.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiButtonKeyCategory;
import fr.nathanael2611.keldaria.mod.client.gui.button.GuiKeybindChoose;
import fr.nathanael2611.modularvoicechat.client.voice.VoiceClientManager;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.panel.GuiPanel;
import fr.reden.guiapi.component.textarea.GuiLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GuiCustomControls extends GuiScreen
{
    private static final GameSettings.Options[] OPTIONS_ARR = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN, GameSettings.Options.AUTO_JUMP};
    /** A reference to the screen object that created this. Used for navigating between screens. */
    private final GuiScreen parentScreen;
    protected String screenTitle = "Controls";
    /** Reference to the GameSettings object. */
    private final GameSettings options;
    /** The ID of the button that has been pressed. */
    public KeyBinding buttonId;
    public long time;

    public HashMap<String, List<KeyBinding>> keys = Maps.newHashMap();

    private String categorySelected = null;

    public GuiCustomControls(GuiScreen screen, GameSettings settings)
    {
        this.parentScreen = screen;
        this.options = settings;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.keys.clear();
        for (KeyBinding keyBinding : options.keyBindings)
        {
            String category = keyBinding.getKeyCategory();
            keys.putIfAbsent(category, Lists.newArrayList());
            keys.get(category).add(keyBinding);

        }

        {
            int y = 20;
            for (String category : keys.keySet())
            {
                this.buttonList.add(new GuiButtonKeyCategory(category, 0, y, 150, 8, I18n.format(category), new Color(0, 0, 0, 0), Color.YELLOW));
                y += 8;
            }
            this.buttonList.add(new GuiButtonKeyCategory("Chat Vocal", 0, y, 150, 20, "Chat Vocal",  new Color(0, 0, 0, 0), Color.YELLOW));

        }

        {
            for (String category : keys.keySet())
            {
                List<KeyBinding> binds = this.keys.get(category);
                int y = 0;
                for (KeyBinding bind : binds)
                {
                    this.buttonList.add(new GuiKeybindChoose(bind, 150, 30 + y + 1, 80, 13, I18n.format(bind.getDisplayName()), Color.GRAY, Color.WHITE));
                    y += 15;
                }
            }

        }

        this.buttonList.add(new GuiOptionSlider(GameSettings.Options.SENSITIVITY.getOrdinal(), 1, height - 20, GameSettings.Options.SENSITIVITY));
        this.buttonList.add(new GuiOptionButton(GameSettings.Options.AUTO_JUMP.getOrdinal(), 1, height - 40, GameSettings.Options.AUTO_JUMP, this.options.getKeyBinding(GameSettings.Options.AUTO_JUMP)));

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if(buttonId != null)
        {
            if (keyCode == 1)
            {
                this.buttonId.setKeyModifierAndCode(net.minecraftforge.client.settings.KeyModifier.NONE, 0);
                this.options.setOptionKeyBinding(this.buttonId, 0);
            }
            else if (keyCode != 0)
            {
                this.buttonId.setKeyModifierAndCode(net.minecraftforge.client.settings.KeyModifier.getActiveModifier(), keyCode);
                this.options.setOptionKeyBinding(this.buttonId, keyCode);
            }
            else if (typedChar > 0)
            {
                this.buttonId.setKeyModifierAndCode(net.minecraftforge.client.settings.KeyModifier.getActiveModifier(), typedChar + 256);
                this.options.setOptionKeyBinding(this.buttonId, typedChar + 256);
            }

                this.buttonId = null;
            this.time = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
        }else
        {

            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if(buttonId != null)
        {
            this.buttonId.setKeyModifierAndCode(net.minecraftforge.client.settings.KeyModifier.getActiveModifier(), -100 + mouseButton);
            this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (net.minecraft.client.gui.GuiButton button : this.buttonList)
        {
            if(button instanceof GuiButtonKeyCategory && this.categorySelected != null)
            {
                ((GuiButtonKeyCategory) button).bgColor = this.categorySelected.equalsIgnoreCase(((GuiButtonKeyCategory) button).getCategoryName()) ? Color.YELLOW : new Color(0, 0, 0, 0);
            }
            else if(button instanceof GuiKeybindChoose)
            {


                boolean keyCodeModifierConflict = false;

                if (((GuiKeybindChoose) button).getBinding().getKeyCode() != 0)
                {
                    for (KeyBinding keybinding : mc.gameSettings.keyBindings)
                    {
                        if (keybinding != ((GuiKeybindChoose) button).getBinding() && keybinding.getKeyCode() == ((GuiKeybindChoose) button).getBinding().getKeyCode())
                        {
                            keyCodeModifierConflict = true;
                            break;
                        }
                    }
                }


                button.visible = ((GuiKeybindChoose) button).getBinding().getKeyCategory().equalsIgnoreCase(this.categorySelected);
                button.displayString = buttonId == ((GuiKeybindChoose) button).getBinding() ? ">  <" : ((GuiKeybindChoose) button).getBinding().getDisplayName();
                ((GuiKeybindChoose) button).bgColor = keyCodeModifierConflict ? Color.RED : new Color(10, 10, 10, 30);
            }
        }

        int color = new Color(17, 128, 106).darker().darker().darker().darker().darker().darker().getRGB();
        RenderHelpers.drawLinearGradientRect(150, 0, width, height,Color.BLACK.getRGB(), new Color(0, 0, 0, 0).getRGB(), 0);
        for(int i = 0; i < height; i ++)
        {
            fontRenderer.drawString("|", 150, i, Color.WHITE.getRGB());
        }

        Gui.drawRect(0, 0, 150, height, color);

        RenderHelpers.drawScaledString(fontRenderer, "§nCatégories", 0, 5, 1.3f, Color.WHITE.getRGB(), true);

        if(categorySelected != null)
        {

            RenderHelpers.drawScaledString(fontRenderer, "§n" + I18n.format(categorySelected), 160, 10, 1.5f, Color.WHITE.getRGB(), true);

            int y = 0;
            for (KeyBinding keyBinding : this.keys.getOrDefault(categorySelected, Lists.newArrayList()))
            {
                fontRenderer.drawString(I18n.format(keyBinding.getKeyDescription()), 240, 30 +  y + 3, Color.LIGHT_GRAY.getRGB(), true);
                for (int i = 0; i < 200; i++)
                {
                    fontRenderer.drawString("-", 151 + i, 30 +  y - 3, Color.LIGHT_GRAY.getRGB(), false);
                }
                y += 15;
            }

        }
        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    @Override
    protected void actionPerformed(net.minecraft.client.gui.GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if(button instanceof GuiButtonKeyCategory)
        {
            if(((GuiButtonKeyCategory) button).getCategoryName().equalsIgnoreCase("Chat Vocal"))
            {
                if(VoiceClientManager.isStarted() && VoiceClientManager.getClient().isConnected())
                {
                    mc.displayGuiScreen(new fr.nathanael2611.modularvoicechat.client.gui.GuiConfig());
                }
            }else
            {
                this.categorySelected = ((GuiButtonKeyCategory) button).getCategoryName();
            }
        }
        else if(button instanceof GuiKeybindChoose)
        {
            this.buttonId = ((GuiKeybindChoose) button).getBinding();
        }
        else if( button instanceof GuiOptionButton)
        {
            options.setOptionValue(((GuiOptionButton) button).getOption(), options.getOptionOrdinalValue(((GuiOptionButton) button).getOption()) ? 0 : 1);
            button.displayString = this.options.getKeyBinding(GameSettings.Options.AUTO_JUMP);

        }
    }

    public static class Controls extends GuiPanel
    {

        private String categoryName;
        private List<KeyBinding> bindings;

        public Controls(String categoryName, List<KeyBinding> keys, int x, int y, int width, int height)
        {
            super(x, y, width, height);
            setBackgroundColor(Color.WHITE.getRGB());
            GuiLabel label = new GuiLabel(0, 0, 100, 20, categoryName);
add(label);
            int posY = 0;
            for (KeyBinding key : keys)
            {
                add(new GuiButton(10, 20 + posY * 20, 120, 20, key.getKeyDescription()));
                posY ++;
            }
        }
    }

}