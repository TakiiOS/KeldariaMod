/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.tileentity;

import fr.nathanael2611.keldaria.mod.entity.EntityHomingPigeon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityPigeonCage extends TileEntity
{

    private EntityHomingPigeon cachedPigeon = null;
    private NBTTagCompound pigeonTag;

    public boolean doesHostPigeon()
    {
        return this.pigeonTag != null;
    }

    public EntityHomingPigeon getCachedPigeon()
    {
        if(this.cachedPigeon == null)
        {
            this.cachedPigeon = new EntityHomingPigeon(this.world);
            this.cachedPigeon.deserializeNBT(this.pigeonTag);
            this.cachedPigeon.setPosition(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        }
        return this.cachedPigeon;
    }

}
