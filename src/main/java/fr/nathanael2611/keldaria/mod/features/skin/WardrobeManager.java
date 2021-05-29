package fr.nathanael2611.keldaria.mod.features.skin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class WardrobeManager
{

    public static final String KEY = Keldaria.MOD_ID + ":wardrobe";

    private static WardrobeManager instance;

    public static WardrobeManager getInstance()
    {
        if(instance == null) instance = new WardrobeManager();
        return instance;
    }

    public Wardrobe getWardrobe(EntityPlayer player)
    {
        return new Wardrobe(player);
    }

    public static class Wardrobe
    {

        private DatabaseReadOnly playerData;

        private HashMap<String, Skin> skinList = new HashMap<>();

        public Wardrobe(EntityPlayer player)
        {
            if(player.world.isRemote)
            {
                playerData = ClientDatabases.getPersonalPlayerData();
            } else {
                playerData = Databases.getPlayerData(player);
            }
            load();
        }

        public void load()
        {
            skinList.clear();
            if(playerData.getString(KEY) == null) return;
            JsonElement element = new JsonParser().parse(playerData.getString(KEY)).getAsJsonObject();

            if(element.isJsonObject())
            {
                JsonObject object = element.getAsJsonObject();
                object.entrySet().forEach(skin -> {
                    if(skin.getValue().getAsString() != null)
                    {
                        String name = skin.getKey();
                        String link = skin.getValue().getAsString();
                        skinList.put(name, new Skin(name, link));
                    }
                });
            }
        }

        public void save(Database database)
        {
            JsonObject object = new JsonObject();
            skinList.forEach((name, skin) -> object.addProperty(skin.getName(), skin.getLink()));
            database.setString(KEY, new Gson().toJson(object));
        }

        public HashMap<String, Skin> getSkinList() {
            return skinList;
        }

        public void addSkin(Skin skin, Database playerData)
        {
            skinList.put(skin.getName(), skin);
            save(playerData);
        }

        public void removeSkin(Skin skin, Database database)
        {
            skinList.remove(skin.getName());
            save(database);
        }

    }

}
