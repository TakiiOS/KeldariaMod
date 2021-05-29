package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.ClientSkinManager;
import fr.nathanael2611.keldaria.mod.features.skin.Skin;
import fr.nathanael2611.keldaria.mod.features.skin.WardrobeManager;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketActionWardrobe;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.GuiEntityRender;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import fr.reden.guiapi.component.textarea.GuiLabel;
import fr.reden.guiapi.component.textarea.GuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class GuiSkinChooser extends GuiFrame
{

    public static final GuiTextureSprite GUI_TEXTURES = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/skin_chooser.png"), 300, 166, 0, 0, 300, 166);
    public int buttonY = 0;
    public boolean needRefresh = false;
    public GuiScrollPane scrollPane = new GuiScrollPane(9, 21, 128, 136);

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks)
    {
        if(needRefresh)
        {
            GuiScreen screen = new GuiSkinChooser().getGuiScreen();
            Minecraft.getMinecraft().displayGuiScreen(screen);
        }
        super.drawBackground(mouseX, mouseY, partialTicks);
    }

    public GuiSkinChooser()
    {
        super(0, 0, 300, 166);
        this.setPauseGame(false);
        this.setBackgroundColor(new Color(0, 0, 0, 0).getRGB());
        this.setTexture(GUI_TEXTURES);
        GuiEntityRender render = new GuiEntityRender(195 ,21,  45, 70, mc.player);
        render.setBackgroundColor(0);
        this.add(render);
        GuiLabel label = new GuiLabel(9, 8, 80, 20, "§8Liste des skins");
        label.setShadowed(false);
        this.add(label);
        GuiLabel noSkin = new GuiLabel(scrollPane.getX() + scrollPane.getWidth() / 2 - 40, scrollPane.getY() + scrollPane.getHeight() / 2 - 10, 80, 20, "§cAucun skins");
        noSkin.setVisible(getWardrobe().getSkinList().size() == 0);
        noSkin.addTickListener(() -> noSkin.setVisible(getWardrobe().getSkinList().size() == 0));
        this.add(noSkin);
        scrollPane.setBackgroundColor(0);
        scrollPane.updateSlidersVisibility();
        for(Skin url : getWardrobe().getSkinList().values()) GuiButtonSkin.create(this, url);
        this.add(scrollPane);
        scrollPane.getySlider().setWheelStep(0.3f);
        GuiTextField nameArea = new GuiTextField(142, 96, 149, 20);
        nameArea.addTickListener(() -> nameArea.setEnabledTextColor(getWardrobe().getSkinList().keySet().contains(nameArea.getText()) ? Color.RED.getRGB() : Color.GREEN.getRGB()));
        this.add(nameArea);
        GuiTextField urlArea = new GuiTextField(142, 96 + 20, 149, 20);
        urlArea.setMaxTextLength(150);
        urlArea.addTickListener(() -> {
            boolean isOk = false;
            try
            {
                if(urlArea.getText().endsWith(".png"))
                {
                    URL url = new URL(urlArea.getText());
                    url.toURI();
                    isOk = true;
                }
            }
            catch(MalformedURLException | URISyntaxException ignored) {}
            urlArea.setEnabledTextColor(!isOk ? Color.RED.getRGB() : Color.GREEN.getRGB());
        });
        this.add(urlArea);
        GuiLabel namePlaceholder = new GuiLabel(nameArea.getX() + 4, nameArea.getY() + 6, 18, 20, "Nom");
        namePlaceholder.addTickListener(() -> namePlaceholder.setText(!nameArea.isFocused() && nameArea.getText().length() == 0 ? "Nom" : ""));
        this.add(namePlaceholder);
        GuiLabel urlPlaceholder = new GuiLabel(urlArea.getX() + 4, urlArea.getY() + 6, 18, 20, "URL");
        urlPlaceholder.addTickListener(() -> urlPlaceholder.setText(!urlArea.isFocused() && urlArea.getText().length() == 0 ? "URL" : ""));
        this.add(urlPlaceholder);
        GuiButton addButton = new GuiButton(142, 96 + 40, 100, 20, "Ajouter");
        addButton.setEnabled(false);
        addButton.addTickListener(() -> addButton.setEnabled(urlArea.getEnabledTextColor() == Color.GREEN.getRGB() && nameArea.getEnabledTextColor() == Color.GREEN.getRGB()));
        addButton.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionWardrobe("addSkin", nameArea.getText(), urlArea.getText()));
            GuiButtonSkin.create(this, new Skin(nameArea.getText(), urlArea.getText()));
            nameArea.setText("");
            urlArea.setText("");
        });
        addButton.setBackgroundColor(0);
        this.add(addButton);
        GuiButton reset = new GuiButton(142 + 100, 96 + 40, 49, 20, "Reset");
        reset.setBackgroundColor(0);
        reset.addClickListener((mouseX1, mouseY1, mouseButton1) ->
        {
            KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketActionWardrobe("setSkin", "Default", "Default"));
        });
        this.add(reset);
    }

    @Override
    public void resize(int width, int height)
    {
        this.setX((width - this.getWidth()) / 2);
        this.setY((height - this.getHeight()) / 2);
        super.resize(width, height);
    }

    public static WardrobeManager.Wardrobe getWardrobe()
    {
        return WardrobeManager.getInstance().getWardrobe(mc.player);
    }

    public static class GuiButtonSkin extends GuiButton
    {

        private Skin skin;

        public static GuiButtonSkin create(GuiSkinChooser skinChooser, Skin skin)
        {
            GuiButtonSkin buttonSkin = new GuiButtonSkin(0, skinChooser.buttonY, 108, 20, skin);
            buttonSkin.setBackgroundColor(0);
            GuiButton removeButton = new GuiButton(108, skinChooser.buttonY, 20, 20, "§cX");
            removeButton.setBackgroundColor(0);
            removeButton.addClickListener(((mouseX1, mouseY1, mouseButton1) -> {
                KeldariaPacketHandler.getInstance().getNetwork()
                        .sendToServer(new PacketActionWardrobe("removeSkin", skin.getName(), skin.getLink()));
                skinChooser.scrollPane.remove(removeButton);
                skinChooser.scrollPane.remove(buttonSkin);
            }));
            skinChooser.scrollPane.add(removeButton);
            skinChooser.scrollPane.add(/*new GuiButton(1, 0, y, 150, 15, url.getName(), url.getLink())*/
                    buttonSkin);
            buttonSkin.addClickListener((mouseX, mouseY, mouseButton) -> {
                KeldariaPacketHandler.getInstance().getNetwork()
                        .sendToServer(new PacketActionWardrobe("setSkin", skin.getName(), skin.getLink()));
            });
            skinChooser.buttonY += 20;
            return buttonSkin;
        }

        public GuiButtonSkin(int x, int y, int width, int height, Skin skin)
        {
            super(x, y, width, height, skin.getName());
            this.skin = skin;
        }

        @Override
        public void drawBackground(int mouseX, int mouseY, float partialTicks)
        {
            this.setForegroundColor(isSelected() ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
            super.drawBackground(mouseX, mouseY, partialTicks);
        }

        public boolean isSelected()
        {
            return this.skin.getLink().equalsIgnoreCase(ClientSkinManager.getSkinURL(mc.player));
        }
    }
}