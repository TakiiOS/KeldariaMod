package fr.nathanael2611.keldaria.mod.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

public class MojangHelpers {

    public static final HashMap<String, MojangProfile> UUID_CACHE = new HashMap<>();

    public static final MojangProfile INVALID_USER = new MojangProfile(
            "INVALID",
            "00000000000000000000000000000000"
    );

    public static MojangProfile getMojangProfileByName(final String PLAYER){
        if(UUID_CACHE.containsKey(PLAYER)){
            return UUID_CACHE.get(PLAYER);
        }
        MojangProfile uuid = INVALID_USER;
        try {
            String json = Helpers.readJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + PLAYER);
            MojangProfile profile = new Gson().fromJson(json, MojangProfile.class);
            return UUID_CACHE.put(profile.getPlayerUuid(), profile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return uuid;
    }

    public static class MojangProfile {
        private String name;
        private String id;
        public MojangProfile() {
        }
        public MojangProfile(
                String playerName,
                String playerUuid
        ) {

            this.name = playerName;
            this.id = playerUuid;
        }

        public String getPlayerName() {
            return name;
        }

        public String getPlayerUuid() {
            return id;
        }
    }

}