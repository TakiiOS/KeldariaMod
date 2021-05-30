/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.util.math.mine;

import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

public class AABB implements INBTSerializable<NBTTagCompound>
{

    public Vec3d origin;
    public Vec3d size;

    public AABB()
    {
        this(new Vec3d(0, 0, 0), new Vec3d(1, 1, 1));
    }

    public AABB(AxisAlignedBB bb)
    {
        this(bb.getCenter(), new Vec3d(
                (bb.maxX - bb.minX) / 2,
                (bb.maxY - bb.minY) / 2,
                (bb.maxZ - bb.minZ) / 2
        ));
    }

    public AABB(Vec3d origin, Vec3d size)
    {
        this.origin = origin;
        this.size = size;
    }

    public AABB offset(Vec3d vec)
    {
        return new AABB(this.origin.add(vec), this.size);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("origin", Helpers.serializeVec3d(this.origin));
        compound.setTag("size", Helpers.serializeVec3d(this.size));
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.origin = Helpers.deserialize(nbt.getCompoundTag("origin"));
        this.size = Helpers.deserialize(nbt.getCompoundTag("size"));
    }
}
