package fr.nathanael2611.keldaria.mod.asm;

import net.minecraft.entity.passive.*;

import java.util.Date;

public interface IKeldariaAnimal
{

    long getTimeCreated();

    void setTimeCreated(long timeCreated);

    default boolean needGrow(EntityAnimal animal)
    {
        final long day = ((1000 * 60) * 60) * 24;
        long time = /*604800000*/ day * 7;
        if(animal instanceof EntitySheep)
            time = day * 8;
        else if(animal instanceof EntityPig)
            time = day * 6;
        else if(animal instanceof EntityCow)
            time = day * 6;
        else if(animal instanceof AbstractHorse)
            time = day * 15;
        else if(animal instanceof EntityChicken)
            time = day * 4;
        else if(animal instanceof EntityRabbit)
            time = day * 3;
        return new Date().getTime() - getTimeCreated() > /**/ time;
    }

}
