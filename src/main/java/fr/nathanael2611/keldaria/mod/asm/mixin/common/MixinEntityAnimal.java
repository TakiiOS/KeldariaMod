package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.IKeldariaAnimal;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(EntityAnimal.class)
public abstract class MixinEntityAnimal extends EntityAgeable implements IKeldariaAnimal
{

    @Shadow private UUID playerInLove;
    @Shadow private int inLove;
    long timeCreated = -1;

    public MixinEntityAnimal(World worldIn)
    {
        super(worldIn);
    }

  //  @Overwrite
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("InLove", this.inLove);

        if (this.playerInLove != null)
        {
            compound.setUniqueId("LoveCause", this.playerInLove);
        }

        compound.setLong("TimeCreated", this.timeCreated);
    }

    @Overwrite
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.inLove = compound.getInteger("InLove");
        this.playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
        this.timeCreated = compound.hasKey("TimeCreated") ? compound.getLong("TimeCreated") : -1;
    }

    @Override
    public long getTimeCreated()
    {
        return this.timeCreated;
    }

    @Override
    public void setTimeCreated(long timeCreated)
    {
        this.timeCreated = timeCreated;
    }
}
