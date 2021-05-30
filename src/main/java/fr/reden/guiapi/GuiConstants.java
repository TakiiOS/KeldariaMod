/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi;

import net.minecraft.util.ResourceLocation;

public class GuiConstants {

    public enum ENUM_POSITION { ABSOLUTE, RELATIVE }
    
    public enum ENUM_RELATIVE_X { LEFT, CENTER, RIGHT }
    public enum ENUM_RELATIVE_Y { TOP, CENTER, BOTTOM }
    
	public enum ENUM_SIZE { ABSOLUTE, RELATIVE }
    
	public enum ENUM_ICON_POSITION { CENTER, LEFT, RIGHT, TOP, BOTTOM }
	
    public enum HORIZONTAL_TEXT_ALIGNMENT { CENTER, LEFT, RIGHT, JUSTIFY }
    public enum VERTICAL_TEXT_ALIGNMENT { CENTER, TOP, BOTTOM }
	
	public static final GuiTextureSprite dirtTexture = new GuiTextureSprite(new ResourceLocation("textures/gui/options_background.png"), 16, 16, 0, 0, 16, 16);
	
}
