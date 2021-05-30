/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.client.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.client.RenderHelpers;
import fr.nathanael2611.keldaria.mod.proxy.ClientProxy;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.reden.guiapi.component.GuiButton;
import fr.reden.guiapi.component.panel.GuiFrame;
import fr.reden.guiapi.component.textarea.GuiLabel;
import fr.reden.guiapi.component.textarea.GuiPasswordField;
import fr.reden.guiapi.component.textarea.GuiTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.UUID;

public class GuiLogin extends GuiFrame
{
    GuiLabel label;
    private GuiTextField usernameField;
    private GuiPasswordField passwordField;
    private String message;
    private GuiButton login;

    public GuiLogin()
    {
        super(0, 0, 300, 300);

        usernameField = new GuiTextField(width / 2 - 200, height / 2, 100, 20);
        add(usernameField);
        passwordField = new GuiPasswordField(width / 2 - 50, height / 2 + 50, 100, 20);
        add(passwordField);


        login = new GuiButton(1, 1, 100, 40);
        login.setBackgroundColor(0);
        login.setTexture(null);
        login.setHoveredTexture(null);
        login.setPressedTexture(null);
        add(login);



        login.addClickListener((mouseX, mouseY, mouseButton) ->
        {
            {
                processLogin(this.usernameField.getText(), this.passwordField.getText());
            }
        });

    }

    private static final ResourceLocation BG = new ResourceLocation("keldaria", "textures/gui/mainmenu/login.png");
    private static final ResourceLocation TITLE = new ResourceLocation(Keldaria.MOD_ID, "textures/content/kgl.png");

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks)
    {
        super.drawBackground(mouseX, mouseY, partialTicks);
        RenderHelpers.drawImage(0, 0, BG, width, height);
        int c = new Color(0, 0, 0, 100).getRGB();
        drawRect(width / 2 + 108, 0, width / 2 + 104, height, c);
        drawRect(width / 2 - 108, 0, width / 2 - 104, height, c);
        drawRect(width / 2 - 100, 0,width / 2 + 100, height, c);
        RenderHelpers.drawImage(width / 2 - 50, height / 2 - 120, TITLE, 100, 100);

        if (message != null)
        {
            RenderHelpers.drawCenteredString(mc.fontRenderer, "§c" + message, width / 2, height / 2 + 100, Color.WHITE.getRGB());
        }

        RenderHelpers.drawCenteredString(mc.fontRenderer, "Pseudonyme", width / 2, height / 2 - 30, Color.WHITE.getRGB());
        RenderHelpers.drawCenteredString(mc.fontRenderer, "Mot-de-passe", width / 2, height / 2 + 10, Color.WHITE.getRGB());


        ResourceLocation rs = mouseX > width / 2 - 50 && mouseX < width / 2 + 100
                && mouseY > height / 2 + 50 && mouseY < height / 2 + 50 + 40 ?
                new ResourceLocation("keldaria", "textures/gui/mainmenu/connect_button_hover.png") :
                new ResourceLocation("keldaria", "textures/gui/mainmenu/connect_button.png");

        RenderHelpers.drawImage(width / 2 - 50, height / 2 + 50,
                rs
                , 100, 40);

        RenderHelpers.drawCenteredString(mc.fontRenderer, "§8(Identifiants du site)", width / 2, height / 2 + 200,Color.WHITE.getRGB());

    }

    public boolean processLogin(String username, String password)
    {
        try
        {
            URL url = new URL(Keldaria.API_URL + "?action=login&id=" + username + "&password=" + password);

            JsonObject object = new JsonParser().parse(Helpers.readResponse(url)).getAsJsonObject();
            String state = object.get("state").getAsString();

            this.message = object.get("message").getAsString();
            if(state.equalsIgnoreCase("success"))
            {
                UUID uuid = UUID.fromString(object.get("uuid").getAsString());
                String token = object.get("token").getAsString();
                Sessionutil.set(new Session(username, uuid.toString(), token, "legacy"));
                ClientProxy.accessToken = token;
                Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
            }

            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            message = "§cErreur de connexion.!";
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            message = "§cErreur de connexion.!";

        }

        return false;

    }

    @Override
    public void resize(int screenWidth, int screenHeight)
    {
        this.setX(0);
        this.setY(0);
        this.setWidth(screenWidth);
        this.setHeight(screenHeight);

        this.usernameField.setX(screenWidth / 2 - 50);
        this.usernameField.setY(screenHeight / 2 - 20);

        this.passwordField.setX(screenWidth / 2 - 50);
        this.passwordField.setY(screenHeight / 2 + 20);

        this.login.setX(screenWidth / 2 - 50);
        this.login.setY(screenHeight / 2 + 50);

        super.resize(screenWidth, screenHeight);
    }


    static final class Sessionutil {
        /**
         * as the Session field in Minecraft.class is final we have to access it
         * via reflection
         */
        private static Field sessionField = ReflectionHelper.findField(Minecraft.class, "session", "S", "field_71449_j");

        static Session get() {
            return Minecraft.getMinecraft().getSession();
        }

        static void set(Session s) throws IllegalArgumentException, IllegalAccessException {
            Sessionutil.sessionField.set(Minecraft.getMinecraft(), s);
            //GuiHandler.invalidateStatus();
        }
    }
}
