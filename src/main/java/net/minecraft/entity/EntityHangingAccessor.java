package net.minecraft.entity;

import net.minecraft.util.EnumFacing;

public class EntityHangingAccessor
{

    public static void updateDirection(EntityHanging hanging, EnumFacing facing)
    {
        hanging.updateFacingWithBoundingBox(facing);
    }

}
