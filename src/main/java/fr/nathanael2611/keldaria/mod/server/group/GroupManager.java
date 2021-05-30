/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.group;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.command.KeldariaCommand;
import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.discord.impl.DiscordCommandSender;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.ConcurrentHashMap;

public class GroupManager
{

    private static GroupManager INSTANCE;

    public static final Group ALL_PERMS_GROUP;
    static {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse("{}").getAsJsonObject();
        JsonArray perms = parser.parse("[]").getAsJsonArray();
        perms.add("*");
        object.add("permissions", perms);
        ALL_PERMS_GROUP = new Group("all_perms", object);
    }

    private ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();
    private Group defaultGroup = null;
    private boolean isSinglePlayer = false;

    private GroupManager()
    {
    }

    public static GroupManager getInstance()
    {
        return INSTANCE == null ? INSTANCE = new GroupManager() : INSTANCE;
    }

    public void init()
    {
        groups.clear();

        if(Keldaria.getInstance().getFiles().GROUPS == null || !Keldaria.getInstance().getFiles().GROUPS.exists())
        {
            isSinglePlayer = true;
            return;
        }

        JsonObject groupsObj = new JsonParser().parse(Helpers.readFileToString(Keldaria.getInstance().getFiles().GROUPS)).getAsJsonObject();
        String defaultGroupName = groupsObj.has("default-group") && groupsObj.get("default-group").isJsonPrimitive() && groupsObj.get("default-group").getAsJsonPrimitive().isString() ? groupsObj.get("default-group").getAsString() : "";

        groupsObj.entrySet().forEach(entry -> {
            if(entry.getValue().isJsonObject())
            {
                JsonObject object = entry.getValue().getAsJsonObject();
                Group group = new Group(entry.getKey(), object);
                this.groups.put(entry.getKey(), group);
                if(group.getName().equals(defaultGroupName)) this.defaultGroup = group;
            }
        });
        if(this.defaultGroup == null)
        {
            this.defaultGroup = new Group("default", new JsonParser().parse("{}").getAsJsonObject());
        }

    }

    public Group get(EntityPlayer player)
    {
        if(isSinglePlayer) return ALL_PERMS_GROUP;
        Database data = Databases.getPlayerData(player);
        if(data.isString("group"))
        {
            return this.groups.getOrDefault(data.get("group").asString(), this.defaultGroup);
        }
        return this.defaultGroup;
    }

    public Group get(String groupName)
    {
        return this.groups.get(groupName);
    }

    public boolean has(String groupName)
    {
        return this.groups.containsKey(groupName);
    }

    public String[] getGroupNames()
    {
        String[] groupNames = new String[groups.size()];
        int i = 0;
        for (Group value : groups.values()) {
            groupNames[i] = value.getName();
            i++;
        }
        return groupNames;
    }

    public static boolean executePermissionCheck(String perm, ICommandSender sender)
    {
        if(sender instanceof DiscordCommandSender)
        {
            return DiscordImpl.getInstance().getConfigManager().isOP(((DiscordCommandSender) sender).getID()) || getInstance().defaultGroup.getPermissions().has(perm);
        }
        return !(sender instanceof EntityPlayer) || GroupManager.getInstance().get((EntityPlayer) sender).getPermissions().has((EntityPlayer) sender, perm);
    }
    public static boolean executePermissionCheck(ICommand command, ICommandSender sender)
    {
        return (command instanceof KeldariaCommand && ((KeldariaCommand) command).isExecutableByAll()) || executePermissionCheck("command." + command.getName() + ".use", sender);
    }

}
