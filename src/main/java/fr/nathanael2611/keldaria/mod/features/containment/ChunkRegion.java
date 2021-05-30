/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.features.containment;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.world.chunk.Chunk;

public class ChunkRegion
{

    private int x1, z1, x2, z2;

    public ChunkRegion(int x1, int z1, int x2, int z2)
    {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    public boolean isIn(Chunk chunk)
    {
        return chunk.x>>4 >= x1 && chunk.x>>4 <= x2 && chunk.z>>4 >= z1 && chunk.z>>4 <= z2;
    }

    public static ChunkRegion fromString(String str)
    {
        String[] parts = str.split(":");
        if(parts.length == 4)
            return new ChunkRegion(Helpers.parseOrZero(parts[0]), Helpers.parseOrZero(parts[1]), Helpers.parseOrZero(parts[2]), Helpers.parseOrZero(parts[3]));
        return new ChunkRegion(0, 0, 0, 0);
    }

    @Override
    public String toString()
    {
        return String.format("%s:%s:%s:%s", x1, z1, x2, z2);
    }

}