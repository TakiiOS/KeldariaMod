package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.IGuiChat;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen implements IGuiChat
{

    @Shadow private String defaultInputFieldText;

    @Override
    public String getDefaultString()
    {
        return defaultInputFieldText;
    }
}
