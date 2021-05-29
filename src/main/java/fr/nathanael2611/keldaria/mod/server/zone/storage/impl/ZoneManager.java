package fr.nathanael2611.keldaria.mod.server.zone.storage.impl;

import fr.nathanael2611.keldaria.mod.server.zone.Zones;
import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;
import fr.nathanael2611.keldaria.mod.server.zone.storage.api.IZoneManager;

public class ZoneManager implements IZoneManager
{

    private RPZone zone = Zones.EVERYWHERE;

    @Override
    public String getZoneName()
    {
        return this.zone.getName();
    }

    @Override
    public RPZone getZone()
    {
        return this.zone;
    }

    @Override
    public void setZone(RPZone zone)
    {
        this.zone = zone;
    }
}
