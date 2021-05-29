package fr.nathanael2611.keldaria.mod.client.gui.button;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.animation.Animation;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiAnimationButton extends GuiButton {
    private String name;
    private Animation animation;
    public GuiAnimationButton(int buttonId, int x, int y, Animation animation) {
        super(buttonId, x, y, 76, 76, animation.getName().toUpperCase());
        this.name = animation.getName();
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    float timer = 0;
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/animations/"+name+"_icon.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderHelpers.drawImage(x, y, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/animations/" + name + "_icon.png"), 76, 76);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }

            if (this.hovered)
            {
                j = 16777120;
                if(timer +8 <= 76){
                    timer +=8;
                }else timer = 76;
                RenderHelpers.drawImage(x, y+76- timer, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/animations/hover_icon.png"), 76, timer, timer);
                this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            }else{
                if(timer -2 >=0){
                    timer -=2;
                }else timer = 0;
                RenderHelpers.drawImage(x, y + 76 - timer, new ResourceLocation(Keldaria.MOD_ID, "textures/gui/animations/hover_icon.png"), 76, timer, timer);
            }

            //this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }
}
