/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.discord.impl;

import com.google.gson.*;
import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.keldaria.mod.discord.DiscordImpl;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.dv8tion.jda.api.entities.Member;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscordConfigManager
{

    private String token = null;
    private boolean botEnabled = false;

    private List<String> opIds = new ArrayList<>();

    public void readConfig()
    {
        opIds.clear();
        String json = Helpers.readFileToString(Keldaria.getInstance().getFiles().DISCORD_CONFIG);
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();

        if(object.has("token"))
        {
            this.token = object.get("token").getAsString();
        } else {
            throw new JsonParseException("There's no token set in the discord config file !");
        }

        if(object.has("botEnabled"))
        {
            this.botEnabled = object.get("botEnabled").getAsBoolean();
        }


        JsonArray array = object.get("opIds").getAsJsonArray();

        array.forEach(element -> {
            if(!opIds.contains(element.getAsString())) opIds.add(element.getAsString());
        });

    }

    public String getToken() {
        return token;
    }

    public boolean isBotEnabled()
    {
        return botEnabled;
    }

    public boolean isOP(String id)
    {
        return opIds.contains(id);
    }

    public void deOp(String id)
    {
        if(opIds.contains(id))
        {
            opIds.remove(id);
            save(this);
        }
    }

    public void op(String id)
    {
        if(!opIds.contains(id))
        {
            opIds.add(id);
            save(this);
        }
    }

    public static void save(DiscordConfigManager config)
    {
        try {
            FileWriter writer = new FileWriter(Keldaria.getInstance().getFiles().DISCORD_CONFIG);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getOpIds() {
        return opIds;
    }

    public String getValidOPAccountsNames()
    {
        StringBuilder builder = new StringBuilder();
        opIds.forEach(s -> {
            Member member = DiscordImpl.getInstance().getBot().getKeldariaGuild().getMemberById(s);
            if(member != null)
            {
                builder.append(", ").append(member.getUser().getName());
            }
        });
        return ":arrow_right_hook: **Liste des comptes discord OP:** \n" + builder.toString().substring(2);
    }
}
