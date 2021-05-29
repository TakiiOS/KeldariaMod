package fr.nathanael2611.keldaria.mod.crafting.storage.api;

import fr.nathanael2611.keldaria.mod.crafting.CraftManager;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface IKnownRecipes
{


    default boolean canCraft(String key)
    {
        for (String s : CraftManager.MANAGERS.keySet())
        {
            if(key.startsWith(s + "/default/")) return true;
        }
        for (String knownKey : this.getKnownKeys())
        {
            if(knownKey.endsWith("/"))
            {
                if(key.startsWith(knownKey))
                    return true;
            }
            else
            {
                if(knownKey.equalsIgnoreCase(key))
                    return true;
            }
        }
        return false;
    }

    List<String> getKnownKeys();

    void discover(String key);

    void forget(String key);

}