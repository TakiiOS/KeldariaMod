/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package net.minecraft.entity;

import net.minecraft.util.EnumFacing;

public class EntityHangingAccessor
{

    public static void updateDirection(EntityHanging hanging, EnumFacing facing)
    {
        hanging.updateFacingWithBoundingBox(facing);
    }

}
