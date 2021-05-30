/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.reden.guiapi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GuiManager {

    private static final List<GuiInfo> guiInfoList = new ArrayList<GuiInfo>();

    public static void openGui(int guiID) {
        if(getGuiInfo(guiID) != null) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            player.openGui(getGuiModInstance(guiID), guiID, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
        } else {
            throw new IllegalArgumentException(String.format("The GUI with id %d doesn't exists or isn't registered.", guiID));
        }
    }

    public static void openGui(String guiName) {
        if(getGuiInfo(guiName) != null) {
            openGui(getGuiID(guiName));
        } else {
            throw new IllegalArgumentException(String.format("The GUI named %s doesn't exists or isn't registered.", guiName));
        }
    }

    public static void registerGui(Object mod, String guiName, int guiID) {

        if(mod == null) {
            throw new IllegalArgumentException("The mod argument can't be null.");
        } else if(guiName == null || guiName.isEmpty()) {
            throw new IllegalArgumentException("The guiName argument can't be null or empty.");
        } else if(guiID < 0) {
            throw new IllegalArgumentException("The guiID must be a positive number.");
        }

        if(canCreateGui(guiName, guiID)) {
            guiInfoList.add(new GuiInfo(mod, guiName, guiID));
        } else {
            throw new IllegalStateException(String.format("The GUI [%s, %d] already exists.", guiName, guiID));
        }
    }

    private static boolean canCreateGui(String guiName, int guiID) {

        for(GuiInfo guiInfo : guiInfoList) {
            if(guiInfo.guiName.toLowerCase().equals(guiName.toLowerCase()) || guiInfo.guiID == guiID) {
                return false;
            }
        }

        return true;
    }

    private static GuiInfo getGuiInfo(int guiID)
    {
        for(GuiInfo guiInfo : guiInfoList) {
            if(guiInfo.guiID == guiID) {
                return guiInfo;
            }
        }

        return null;
    }

    private static GuiInfo getGuiInfo(String guiName)
    {
        for(GuiInfo guiInfo : guiInfoList) {
            if(guiInfo.guiName.toLowerCase().equals(guiName.toLowerCase())) {
                return guiInfo;
            }
        }

        return null;
    }

    public static int getGuiID(String guiName)
    {
        GuiInfo guiInfo = getGuiInfo(guiName);
        return guiInfo == null ? -1 : guiInfo.guiID;
    }

    public static String getGuiName(int guiID)
    {
        GuiInfo guiInfo = getGuiInfo(guiID);
        return guiInfo == null ? null : guiInfo.guiName;
    }

    public static Object getGuiModInstance(int guiID) {
        GuiInfo guiInfo = getGuiInfo(guiID);

        if(guiInfo != null) {
            return guiInfo.mod;
        }

        return null;
    }

    public static Object getGuiModInstance(String guiName) {
        return getGuiModInstance(getGuiID(guiName));
    }

    private static class GuiInfo {

        private final Object mod;
        private final String guiName;
        private final int guiID;

        private GuiInfo(Object mod, String guiName, int guiID) {
            this.mod = mod;
            this.guiName = guiName;
            this.guiID = guiID;
        }

    }

}
