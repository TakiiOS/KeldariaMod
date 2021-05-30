/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.crafting.storage.impl;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;

import java.util.List;

public class KnownRecipes implements IKnownRecipes
{

    private List<String> knownRecipes = Lists.newArrayList();

    @Override
    public List<String> getKnownKeys()
    {
        return this.knownRecipes;
    }

    @Override
    public void discover(String key)
    {
        if (!this.knownRecipes.contains(key))
        {
            this.knownRecipes.add(key);
        }
    }

    @Override
    public void forget(String key)
    {
        this.knownRecipes.remove(key);
        for (int i = 0; i < this.knownRecipes.size(); i++)
        {
            String s = this.knownRecipes.get(i);
            if(s.startsWith(key))
            {
                this.knownRecipes.remove(s);
            }
        }
    }
}
