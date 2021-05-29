package fr.nathanael2611.keldaria.mod.features;

import fr.nathanael2611.keldaria.mod.Keldaria;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class HorsesTextures
{

    public static final DataParameter<String> TEXTURE = EntityDataManager.<String>createKey(EntityHorse.class, DataSerializers.STRING);

    public static final String KEY = Keldaria.MOD_ID + ":customSkin";

    public static void setTexture(EntityHorse horse, String url)
    {
        horse.getDataManager().set(TEXTURE, url);
    }

    public static boolean hasTexture(EntityHorse horse)
    {
        return horse.getDataManager().get(TEXTURE).length() > 0;
    }

    public static String getTexture(EntityHorse horse)
    {
        return horse.getDataManager().get(TEXTURE);
    }

}
