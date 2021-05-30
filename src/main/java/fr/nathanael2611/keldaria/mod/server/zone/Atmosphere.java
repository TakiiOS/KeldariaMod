/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.server.zone;

import net.minecraft.util.math.Vec3d;

public class Atmosphere
{

    private RGB skyColor = new RGB();
    private RGB fogColor = new RGB();
    private double fogDensity = -1;

    public Atmosphere(RGB skyColor, RGB fogColor, double fogDensity)
    {
        this.skyColor = skyColor;
        this.fogColor = fogColor;
        this.fogDensity = fogDensity;
    }

    public Atmosphere()
    {
    }




    public RGB getSkyColor()
    {
        return skyColor;
    }

    public RGB getFogColor()
    {
        return fogColor;
    }

    public double getFogDensity()
    {
        return fogDensity;
    }

    public static class RGB
    {
        private float r = -1, g = -1, b = -1;
        private boolean additive = false;

        public RGB(float r, float g, float b, boolean additive)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            this.additive = additive;
        }

        public RGB(float r, float g, float b)
        {
            this(r, g, b, false);
        }

        public RGB()
        {
        }


        public boolean isAdditive()
        {
            return additive;
        }

        public float getRed()
        {
            return r;
        }

        public float getGreen()
        {
            return g;
        }

        public float getBlue()
        {
            return b;
        }

        public Vec3d asVec()
        {
            return new Vec3d(this.r, this.g, this.b);
        }

        public boolean hasColor()
        {
            return this.r != -1 && this.g != -1 && this.b != -1;
        }

    }

}