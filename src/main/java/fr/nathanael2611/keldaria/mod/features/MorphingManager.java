/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

import com.google.common.collect.Maps;
import fr.nathanael2611.simpledatabasemanager.client.ClientDatabases;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import fr.nathanael2611.simpledatabasemanager.core.SyncedDatabases;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import java.util.HashMap;

public class MorphingManager
{

    public static final String KEY = "keldaria:morphings";

    public static final HashMap<String, EntityLivingBase> ENTITIES = Maps.newHashMap();

    public static DatabaseReadOnly getDb(boolean client)
    {
        if(client) return ClientDatabases.getDatabase(KEY);
        else return Databases.getDatabase(KEY);
    }

    public static EntityLivingBase getMorphAsEntity(EntityPlayer player, boolean client)
    {
        if(isMorphed(player, client))
        {
            String morphJson = getDb(client).getString(player.getName());
            if(ENTITIES.containsKey(morphJson))
            {
                return ENTITIES.get(morphJson);
            }
            else
            {
                try
                {
                    NBTTagCompound compound = JsonToNBT.getTagFromJson(morphJson);
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(compound, player.world, 0, 0, 0, false);
                    if(entity instanceof EntityLivingBase)
                    {
                        return ENTITIES.put(morphJson, (EntityLivingBase) entity);
                    }
                    else return ENTITIES.put(morphJson, null);
                } catch (NBTException e)
                {
                    e.printStackTrace();
                    return ENTITIES.put(morphJson, null);
                }
            }
        }
        else
        {
            return null;
        }
    }

    public static boolean isMorphed(EntityPlayer player, boolean client)
    {
        DatabaseReadOnly db  = getDb(client);
        return db.isString(player.getName());
    }

    public static void morph(EntityPlayer player, String json)
    {
        Databases.getDatabase(KEY).setString(player.getName(), json);
    }

    public static void unMorph(EntityPlayer player)
    {
        Databases.getDatabase(KEY).remove(player.getName());
        SyncedDatabases.syncAll();

    }

}
