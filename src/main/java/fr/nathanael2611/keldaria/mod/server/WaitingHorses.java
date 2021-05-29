package fr.nathanael2611.keldaria.mod.server;

import fr.nathanael2611.keldaria.mod.Keldaria;
import fr.nathanael2611.simpledatabasemanager.core.Database;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraft.entity.passive.AbstractHorse;

import java.util.UUID;

public class WaitingHorses
{

    public static final Database DB = Databases.getDatabase(Keldaria.MOD_ID + "waiting_horses");

    public static boolean isWaiting(AbstractHorse horse)
    {
        return DB.isString(horse.getUniqueID().toString());
    }

    public static void unWait(AbstractHorse horse)
    {
        DB.remove(horse.getUniqueID().toString());
    }

    public static void setWait(AbstractHorse horse, UUID uuid)
    {
        DB.setString(horse.getUniqueID().toString(), uuid.toString());
    }

    public static WaitingHorse getWaitingHorse(AbstractHorse horse)
    {
        if (isWaiting(horse))
            return new WaitingHorse(horse.getUniqueID(), UUID.fromString(DB.getString(horse.getUniqueID().toString())), true);
        return new WaitingHorse(horse.getUniqueID(), UUID.randomUUID(), false);
    }

    public static class WaitingHorse
    {

        public final UUID entityId;
        public final UUID ownerId;
        public final boolean exist;

        public WaitingHorse(UUID entityId, UUID owner, boolean exist)
        {
            this.entityId = entityId;
            this.ownerId = owner;
            this.exist = exist;
        }
    }

}
