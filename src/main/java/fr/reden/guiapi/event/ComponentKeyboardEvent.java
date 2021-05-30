/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi.event;

import fr.reden.guiapi.component.GuiComponent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class ComponentKeyboardEvent extends Event {

    public final GuiComponent component;

    public ComponentKeyboardEvent(GuiComponent component) {
        this.component = component;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public static class ComponentKeyTypeEvent extends ComponentKeyboardEvent {

        public final char typedChar;
        public final int keyCode;

        public ComponentKeyTypeEvent(GuiComponent component, char typedChar, int keyCode) {
            super(component);
            this.typedChar = typedChar;
            this.keyCode = keyCode;
        }

    }

}
