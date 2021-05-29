package fr.nathanael2611.keldaria.mod.server.zone.storage.api;

import fr.nathanael2611.keldaria.mod.server.zone.landscapes.RPZone;

public interface IZoneManager
{

    String getZoneName();

    RPZone getZone();

    void setZone(RPZone zone);

}
