/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.handler;

import fr.nathanael2611.keldaria.mod.features.KeldariaDate;
import fr.nathanael2611.keldaria.mod.season.Season;
import fr.nathanael2611.simpledatabasemanager.core.Databases;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DateHandler
{

    private static long worldTimeTimer = 0;
    private static long dateSyncTimer = 0;

    private static long worldLoadedTimeStamp = 0;

    @SubscribeEvent
    public void onWorldLoaded(WorldEvent.Load e)
    {
        worldLoadedTimeStamp = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent e)
    {
        boolean doNotUseKeldariaDate = e.world.getGameRules().getBoolean("doNotUseKeldariaDate");
        if(doNotUseKeldariaDate)
        {
            return;
        }
        worldTimeTimer++;
        if(worldTimeTimer > 10)
        {
            worldTimeTimer = 0;
            KeldariaDate.KeldariaDateFormat date = KeldariaDate.getKyrgonDate();
            e.world.setWorldTime(date.toTicks());
            date.updateWeather(e.world);
        }
        dateSyncTimer++;
        if(dateSyncTimer > 120)
        {
            dateSyncTimer = 0;
            Databases.getDatabase("roleplayinfos").setString("date", KeldariaDate.getKyrgonDate().toFormattedString());
            if(System.currentTimeMillis() - worldLoadedTimeStamp > 50000 && e.world.loadedEntityList.size() > 0)
            {
                Season newSeason = KeldariaDate.getKyrgonDate().getSeason();
                if (newSeason != null)
                {
                    Season.setSeason(e.world, newSeason);
                    //SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(e.world);
                    //seasonData.seasonCycleTicks = SeasonTime.ZERO.getSubSeasonDuration() * newSeason.ordinal();
                    //seasonData.markDirty();
                    //SeasonHandler.sendSeasonUpdate(e.world);
                }
            }
        }
    }
}
