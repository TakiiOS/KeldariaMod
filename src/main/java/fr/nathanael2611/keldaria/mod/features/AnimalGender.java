/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features;

public enum AnimalGender
{

    FEMALE(0, "female", "♀"), MALE(1, "male", "♂");

    private int id;
    private String name;
    private String symbol;

    AnimalGender(int id, String name, String symbol)
    {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public static AnimalGender byId(int id)
    {
        if (id == 1)
        {
            return MALE;
        }
        return FEMALE;
    }
}
