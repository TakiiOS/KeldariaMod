package fr.nathanael2611.keldaria.mod.entity.bestiary;

import fr.nathanael2611.keldaria.mod.features.combat.IHasStat;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class EntityBestiary extends EntityMob implements IHasStat
{

    private String customTexture;

    public EntityBestiary(World worldIn)
    {
        super(worldIn);
    }

    public boolean hasCustomTexture()
    {
        return this.customTexture != null;
    }

    public String getCustomTexture()
    {
        return this.customTexture;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        if(this.customTexture != null)
        {
            compound.setString("CustomTexture", customTexture);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        if(compound.hasKey("CustomTexture", Constants.NBT.TAG_STRING))
        {
            this.customTexture = compound.getString("CustomTexture");
        }
    }
}
