package fr.nathanael2611.keldaria.mod.features.containment;

import fr.nathanael2611.simpledatabasemanager.core.DatabaseReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class Regions extends ContainmentSystem
{

    public static final String KEY = "regions";

    public static void set(String name, ChunkRegion region)
    {
        getDB(KEY).setString(name, region.toString());
    }

    public static void remove(String name)
    {
        getDB(KEY).remove(name);
    }

    public static boolean canMoveOn(EntityPlayer player, Chunk chunk)
    {
        DatabaseReadOnly db = getReadDB(chunk.getWorld(), KEY);
        for (String regionName : db.getAllEntryNames())
        {
            ChunkRegion region = ChunkRegion.fromString(db.getString(regionName));
            if(region.isIn(chunk))
            {
                return true;
            }
        }
        return false;
    }

}
