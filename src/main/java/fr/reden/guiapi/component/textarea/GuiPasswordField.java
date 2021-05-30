/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.component.textarea;

public class GuiPasswordField extends GuiTextField {
	
	public GuiPasswordField(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
    
    @Override
    protected String getRenderedText() {
		StringBuilder builder = new StringBuilder();
	
		for(int i = 0; i < getText().length(); i++) {
			builder.append('*');
		}
		
		return builder.toString();
	}

}
