/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.fight.attacks.weapons;

public enum SwingTypes
{

    LATERAL_RIGHT(0), LATERAL_LEF(1), BY_UP_RIGHT(2), BY_UP_LEFT(3), THRUST(4), PARRY(5), NONE(6);

    private int id;

    SwingTypes(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public static SwingTypes byId(int id)
    {
        for (SwingTypes value : values())
        {
            if(value.id == id)
            {
                return value;
            }
        }
        return NONE;
    }
}
