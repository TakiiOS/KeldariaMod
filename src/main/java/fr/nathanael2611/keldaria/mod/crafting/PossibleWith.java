/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.crafting;

import fr.nathanael2611.keldaria.mod.features.skill.EnumComplement;
import fr.nathanael2611.keldaria.mod.features.skill.EnumJob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class PossibleWith implements INBTSerializable<NBTTagCompound>
{

    private EnumJob job = null;
    private EnumJob.Level level = null;

    private EnumComplement complement = null;

    public PossibleWith(EnumJob job, EnumJob.Level level)
    {
        this.job = job;
        this.level = level;
    }

    public PossibleWith(EnumComplement complement)
    {
        this.complement = complement;
    }

    public PossibleWith(NBTTagCompound comp)
    {
        this.deserializeNBT(comp);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        if(isJobNeed())
        {
            compound.setString("Job", this.job.getName());
            compound.setInteger("Level", this.level.getId());
        }
        else if(isComplementNeed())
        {
            compound.setString("Complement", this.complement.getName());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("Job") && nbt.hasKey("Level"))
        {
            this.job = EnumJob.byName(nbt.getString("Job"));
            this.level = EnumJob.Level.byId(nbt.getInteger("Level"));
        }
        else if(nbt.hasKey("Complement"))
        {
            this.complement = EnumComplement.byName(nbt.getString("Complement"));
        }
    }

    public boolean isValid()
    {
        return (this.job != null && this.level != null) || this.complement != null;
    }

    public boolean isJobNeed()
    {
        return this.job != null && this.level != null;
    }

    public boolean isComplementNeed()
    {
        return !isJobNeed() && this.complement != null;
    }

    public EnumJob getJob()
    {
        return job;
    }

    public EnumJob.Level getLevel()
    {
        return level;
    }

    public EnumComplement getComplement()
    {
        return complement;
    }

    public boolean isPossible(EntityPlayer player)
    {
        if(isComplementNeed())
        {
            return this.complement.has(player);
        }
        else if(isJobNeed())
        {
            return this.job.getLevel(player).atLeast(this.level);
        }
        return false;
    }
}
