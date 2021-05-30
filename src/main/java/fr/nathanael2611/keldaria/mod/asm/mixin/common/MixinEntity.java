/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.ICanAddEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICanAddEntity
{
    @Shadow protected abstract void addPassenger(Entity passenger);

    @Shadow public float width;

    @Shadow public float height;

    @Shadow public double posX;

    @Shadow public double posY;

    @Shadow public double posZ;

    @Shadow public World world;

    @Shadow public abstract void playSound(SoundEvent soundIn, float volume, float pitch);

    @Shadow protected Random rand;

    @Shadow public abstract boolean isSneaking();

    @Shadow public abstract boolean isRiding();

    @Shadow public abstract void sendMessage(ITextComponent component);

    @Shadow public abstract String getName();

    @Override
    public void addEPassenger(Entity passenger) {
        addPassenger(passenger);
    }

}
