/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.group;

import com.google.gson.JsonArray;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

/**
 * This class will be used to do checks on groups permissions
 */
public class Permissions
{

    private final Group ATTACHED_GROUP;
    private final ArrayList<String> PERMISSIONS;
    private final String INHERIT_GROUP_NAME;

    public Permissions(Group attachedGroup, JsonArray permArray, String inheritGroupName)
    {
        this.ATTACHED_GROUP = attachedGroup;
        this.PERMISSIONS = new ArrayList<>();
        permArray.forEach(jsonElement -> PERMISSIONS.add(jsonElement.getAsString()));
        this.INHERIT_GROUP_NAME = inheritGroupName;
    }

    public boolean has(String permission)
    {

        if(PERMISSIONS.contains("*")) return true;
        if(GroupManager.getInstance().has(this.INHERIT_GROUP_NAME))
        {
            if(!this.ATTACHED_GROUP.getName().equals(this.INHERIT_GROUP_NAME) && this.ATTACHED_GROUP.getPermissions() != null)
            {

                if(!this.ATTACHED_GROUP.getPermissions().getInheritGroupName().equals(this.ATTACHED_GROUP.getName()))
                {
                    if(GroupManager.getInstance().get(this.INHERIT_GROUP_NAME).getPermissions().has(permission))
                    {
                        return true;
                    }
                }
            }
        }
        return this.PERMISSIONS.contains(permission);
    }

    public boolean has(EntityPlayer player, String permission)
    {
        return Helpers.isOP(player.getName()) || has(permission);
    }

    public String getInheritGroupName() {
        return INHERIT_GROUP_NAME;
    }

    public String[] getPermissionsAsString()
    {
        String[] permissions = new String[PERMISSIONS.size()];
        int i = 0;
        for (String perm : PERMISSIONS) {
            permissions[i] = perm;
            i++;
        }
        return permissions;
    }

    public boolean inherit()
    {
        return GroupManager.getInstance().has(INHERIT_GROUP_NAME);
    }
}
