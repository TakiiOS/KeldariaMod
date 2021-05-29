package fr.nathanael2611.keldaria.mod.client.gui;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.ArmPosesStorage;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.features.armoposes.Arms;
import fr.nathanael2611.keldaria.mod.network.KeldariaPacketHandler;
import fr.nathanael2611.keldaria.mod.network.toserver.PacketChooseArmPose;
import fr.reden.guiapi.GuiTextureSprite;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.GuiEntityRender;
import fr.reden.guiapi.component.GuiSlider;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.panel.GuiScrollPane;
import fr.reden.guiapi.component.textarea.GuiLabel;
import fr.reden.guiapi.component.textarea.GuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.function.Consumer;

public class GuiArmsChooser extends GuiFrame
{

    public static final GuiTextureSprite GUI_TEXTURES = new GuiTextureSprite(new ResourceLocation(Keldaria.MOD_ID, "textures/gui/arms_chooser.png"), 300, 166, 0, 0, 300, 166);
    public int buttonY = 0;
    public boolean needRefresh = false;
    public GuiScrollPane scrollPane = new GuiScrollPane(9, 21, 128, 136);
    private ArmPosesStorage storage;
    private Arms toAdd = new Arms();

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks)
    {
        if (needRefresh)
        {
            GuiScreen screen = new GuiArmsChooser().getGuiScreen();
            Minecraft.getMinecraft().displayGuiScreen(screen);
        }
        super.drawBackground(mouseX, mouseY, partialTicks);
    }

    float yaw = 0;

    public GuiArmsChooser()
    {
        super(0, 0, 300, 166);

        this.storage = ArmPosesStorage.get();
        this.setPauseGame(false);
        this.setBackgroundColor(new Color(0, 0, 0, 0).getRGB());
        this.setTexture(GUI_TEXTURES);
        GuiEntityRender render = new GuiEntityRender(195, 21, 45, 70, mc.player)
        {
            @Override
            public void drawForeground(int mouseX, int mouseY, float partialTicks)
            {
                GlStateManager.pushMatrix();
                if (entity != null)
                {
                    int scale = (int) ((getHeight() - (paddingBottom + paddingTop)) / entity.height);
                    int x = getScreenX() + getWidth() / 2;
                    int y = getScreenY() + getHeight() - paddingBottom;
                    RenderHelpers.drawEntityWithYaw(x, y, scale, yaw, entity);
                }
                GlStateManager.popMatrix();
            }
        };
        render.addTickListener(() -> yaw += 1f);
        render.setBackgroundColor(0);
        this.add(render);

        GuiLabel label = new GuiLabel(9, 8, 120, 20, "§8Liste des positions");
        label.setShadowed(false);
        this.add(label);

        GuiLabel label2 = new GuiLabel(145, 8, 120, 20, "§8Créer une position");
        label2.setShadowed(false);
        this.add(label2);

        GuiLabel noPoses = new GuiLabel(scrollPane.getX() + scrollPane.getWidth() / 2 - 40, scrollPane.getY() + scrollPane.getHeight() / 2 - 10, 100, 20, "§cAucune positions");
        noPoses.setVisible(this.storage.getArms().size() == 0);
        noPoses.addTickListener(() -> noPoses.setVisible(this.storage.getArms().size() == 0));
        this.add(noPoses);

        scrollPane.setBackgroundColor(0);
        scrollPane.updateSlidersVisibility();
        for (Arms pose : this.storage.getArms()) GuiButtonArm.create(this, pose);
        this.add(scrollPane);
        scrollPane.getySlider().setWheelStep(0.3f);

        this.add(getSlider(142, 21 + 10, this.getToAdd().getLeftArm()::setRotX));
        this.add(getSlider(142, 21 + 30, this.getToAdd().getLeftArm()::setRotY));
        this.add(getSlider(142, 21 + 60 - 10, this.getToAdd().getLeftArm()::setRotZ));

        this.add(getSlider(239, 21 + 10, this.getToAdd().getRightArm()::setRotX));
        this.add(getSlider(239, 21 + 30, this.getToAdd().getRightArm()::setRotY));
        this.add(getSlider(239, 21 + 60 - 10, this.getToAdd().getRightArm()::setRotZ));

        GuiTextField name = new GuiTextField(142, 95, 149, 15);
        name.addKeyboardListener((typedChar, keyCode) ->
        {
            this.toAdd.setName(name.getText());
        });
        this.add(name);

        GuiButton addButton = new GuiButton(142, 114, 149, 15, "Ajouter");
        addButton.setBackgroundColor(0);
        addButton.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            this.storage.add(this.toAdd);
            this.toAdd = new Arms();
            needRefresh = true;
        });
        this.add(addButton);


        GuiButton reset = new GuiButton(142, 96 + 40, 149, 20, "Reset");
        reset.setBackgroundColor(0);
        reset.addClickListener((mouseX1, mouseY1, mouseButton1) -> KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketChooseArmPose(PacketChooseArmPose.REMOVE, null)));
        this.add(reset);
    }

    public GuiSlider getSlider(int x, int y, Consumer<Float> consumer)
    {
        GuiSlider rot = new GuiSlider(x, y, 52, 10, true);
        rot.setMin(-3);
        rot.setMax(3);
        rot.addSliderListener(val -> consumer.accept((float) val));
        return rot;
    }

    @Override
    public void resize(int width, int height)
    {
        this.setX((width - this.getWidth()) / 2);
        this.setY((height - this.getHeight()) / 2);
        super.resize(width, height);
    }

    public static class GuiButtonArm extends GuiButton
    {

        private Arms pose;

        public static GuiButtonArm create(GuiArmsChooser armsChooser, Arms pose)
        {
            GuiButtonArm buttonSkin = new GuiButtonArm(0, armsChooser.buttonY, 108, 20, pose);
            buttonSkin.setBackgroundColor(0);
            GuiButton removeButton = new GuiButton(108, armsChooser.buttonY, 20, 20, "§cX");
            removeButton.setBackgroundColor(0);
            removeButton.addClickListener(((mouseX1, mouseY1, mouseButton1) ->
            {
                armsChooser.storage.remove(pose);
                armsChooser.scrollPane.remove(removeButton);
                armsChooser.scrollPane.remove(buttonSkin);
                armsChooser.needRefresh = true;
            }));
            armsChooser.scrollPane.add(removeButton);
            armsChooser.scrollPane.add(/*new GuiButton(1, 0, y, 150, 15, url.getName(), url.getLink())*/
                    buttonSkin);
            buttonSkin.addClickListener((mouseX, mouseY, mouseButton) ->
            {
                KeldariaPacketHandler.getInstance().getNetwork().sendToServer(new PacketChooseArmPose(PacketChooseArmPose.SET, buttonSkin.pose));
            });
            armsChooser.buttonY += 20;
            return buttonSkin;
        }

        public GuiButtonArm(int x, int y, int width, int height, Arms pose)
        {
            super(x, y, width, height, pose.getName());
            this.pose = pose;
        }

        @Override
        public void drawBackground(int mouseX, int mouseY, float partialTicks)
        {
            this.setForegroundColor(isSelected() ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
            super.drawBackground(mouseX, mouseY, partialTicks);
        }

        public boolean isSelected()
        {
            return Keldaria.getInstance().getArmPoses().hasPose(mc.player) && this.pose.getName().equalsIgnoreCase(Keldaria.getInstance().getArmPoses().getPose(mc.player).getName());
        }
    }

    public Arms getToAdd()
    {
        return toAdd;
    }
}
