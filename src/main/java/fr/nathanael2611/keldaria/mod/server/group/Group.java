package fr.nathanael2611.keldaria.mod.server.group;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Group
{

    private final Permissions PERMISSIONS;

    private final String NAME;

    private final String FORMATTED_NAME;

    public Group(String groupName, JsonObject object)
    {
        this.NAME = groupName;
        this.FORMATTED_NAME = object.has("formatted-name") ? object.get("formatted-name").getAsString() : NAME;
        this.PERMISSIONS = new Permissions(
                this,
                object.has("permissions") ? object.get("permissions").getAsJsonArray() : new JsonParser().parse("[]").getAsJsonArray(),
                object.has("inherit") ? object.get("inherit").getAsString() : ""
        );
    }

    public String getName() {
        return NAME;
    }

    public String getFormattedName() {
        return FORMATTED_NAME;
    }

    public Permissions getPermissions() {
        return PERMISSIONS;
    }
}
