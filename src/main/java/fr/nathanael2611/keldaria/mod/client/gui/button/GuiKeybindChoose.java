package fr.nathanael2611.keldaria.mod.client.gui.button;

import net.minecraft.client.settings.KeyBinding;

import java.awt.*;

public class GuiKeybindChoose extends GuiButtonFullgood

{
    private KeyBinding binding;
    public GuiKeybindChoose(KeyBinding binding, int x, int y, int widthIn, int heightIn, String buttonText, Color bgColor, Color color)
    {
        super(0, x, y, widthIn, heightIn, buttonText, bgColor, color);
        this.binding = binding;
    }

    public KeyBinding getBinding()
    {
        return binding;
    }
}
