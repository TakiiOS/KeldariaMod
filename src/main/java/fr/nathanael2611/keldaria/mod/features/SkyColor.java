package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.util.math.Vec3d;

public class SkyColor
{

    public static final String DB_KEY = Keldaria.MOD_ID + ":roleplayinfos";

    public static final String KEY_SKY_COLOR = "sky-color";
    public static final String KEY_FOG_COLOR = "fog-color";

    public static void setSkyColor(double r, double g, double b)
    {
        Databases.getDatabase(DB_KEY).setString(KEY_SKY_COLOR, String.format("%s/%s/%s", r, g, b));
    }

    public static void setFogColor(double r, double g, double b)
    {
        Databases.getDatabase(DB_KEY).setString(KEY_FOG_COLOR, String.format("%s/%s/%s", r, g, b));
    }

    public static boolean isSkyColorCustom(boolean client)
    {
        return client ? ClientDatabases.getDatabase(DB_KEY).isString(KEY_SKY_COLOR) : Databases.getDatabase(DB_KEY).isString(KEY_SKY_COLOR);
    }

    public static Vec3d getSkyColor(boolean client)
    {
        DatabaseReadOnly db = client ? ClientDatabases.getDatabase(DB_KEY) : Databases.getDatabase(DB_KEY);
        String[] skyColor = db.get(KEY_SKY_COLOR).asString().split("/");
        return skyColor.length == 3 ? new Vec3d(Double.parseDouble(skyColor[0]), Double.parseDouble(skyColor[1]), Double.parseDouble(skyColor[2])) : new Vec3d(0, 0, 0);
    }

    public static void resetSkyColor()
    {
        Databases.getDatabase(DB_KEY).remove(KEY_SKY_COLOR);
    }

    public static boolean isFogColorCustom(boolean client)
    {
        return client ? ClientDatabases.getDatabase(DB_KEY).isString(KEY_FOG_COLOR) : Databases.getDatabase(DB_KEY).isString(KEY_FOG_COLOR);
    }

    public static Vec3d getFogColor(boolean client)
    {
        DatabaseReadOnly db = client ? ClientDatabases.getDatabase(DB_KEY) : Databases.getDatabase(KEY_FOG_COLOR);
        String[] fogColor = db.get(KEY_FOG_COLOR).asString().split("/");
        return fogColor.length == 3 ? new Vec3d(Double.parseDouble(fogColor[0]), Double.parseDouble(fogColor[1]), Double.parseDouble(fogColor[2])) : new Vec3d(0, 0, 0);
    }

    public static void resetFogColor()
    {
        Databases.getDatabase(DB_KEY).remove(KEY_FOG_COLOR);
    }

}
