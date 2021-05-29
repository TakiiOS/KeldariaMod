package fr.nathanael2611.keldaria.mod.server;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.server.group.GroupManager;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

/**
 * This c
 */
public class ChatUsernameAssets
{

    public static final String USERNAME_FORMAT_KEY = Keldaria.MOD_ID + ":username.format";

    public static String getUnformattedUsername(EntityPlayer player)
    {
        Database playerData = Databases.getPlayerData(player);
        if(playerData.isString(USERNAME_FORMAT_KEY)) return playerData.getString(USERNAME_FORMAT_KEY);
        return "{roleplayname}";
    }

    public static String getFormattedUsername(EntityPlayer player)
    {
        String username = getUnformattedUsername(player);
        username = username.replace("&", "§");
        username = username.replace("{username}", player.getName());
        username = username.replace("{roleplayname}", RolePlayNames.getName(player));
        username = username.replace("{group}", GroupManager.getInstance().get(player).getFormattedName());
        return username;
    }

    public static String getFormattedUncoloredUsername(EntityPlayer player)
    {
        String username = getFormattedUsername(player);
        for (TextFormatting value : TextFormatting.values()) {
            username = username.replace(value.toString(), "");
        }
        return username;
    }

}
