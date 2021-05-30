/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui.button;

import java.awt.*;

public class GuiButtonIngameMenu extends GuiButtonFullgood
{

    public GuiButtonIngameMenu(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText, new Color(0, 0, 0, 0), new Color(255, 255, 200));
    }
}
