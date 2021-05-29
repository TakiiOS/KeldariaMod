package fr.nathanael2611.keldaria.mod.features.containment;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Points extends ContainmentSystem
{

    public static final String KEY = "points";

    public static void set(Chunk key, int maxDistance)
    {
        getDB(KEY).setInteger(key.x + "/" + key.z, maxDistance);
    }

    public static void remove(Chunk key)
    {
        getDB(KEY).remove(key.x + "/" + key.z);
    }

    public static boolean canMoveOn(EntityPlayer player, Chunk chunk)
    {
        DatabaseReadOnly db = getReadDB(player.world, KEY);
        for (String chunkParts : db.getAllEntryNames())
        {
            String[] parts = chunkParts.split("/");
            if(parts.length == 2)
            {
                int x = Helpers.parseOrZero(parts[0]);
                int z = Helpers.parseOrZero(parts[1]);
                if(Helpers.getChunkDistance(chunk.x>>4, chunk.z>>4, x, z) <= db.getInteger(chunkParts))
                {
                    return true;
                }
            }
        }
        return false;
    }


}
