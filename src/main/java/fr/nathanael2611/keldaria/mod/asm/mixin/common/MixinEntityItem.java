package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.log.PlayerSpy;
import fr.nathanael2611.keldaria.mod.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem extends Entity
{
    @Shadow public abstract String getName();

    @Shadow public abstract ItemStack getItem();

    public MixinEntityItem(World worldIn)
    {
        super(worldIn);
    }

    public void setDead()
    {
        if(!this.world.isRemote)
        {
            NBTTagCompound compound = this.getItem().getTagCompound();
            if(compound != null && compound.toString().length() > 1999)
            {
                compound = null;
            }
            PlayerSpy.ITEMS_LOG.log("**Item Death:** " + this.getItem().getDisplayName() + "\n**Position**: " + Helpers.blockPosToString(this.getPosition()) + "\nGive command: " + String.format("`/give %s %s %s %s`", this.getItem().getItem().getRegistryName(), this.getItem().getCount(), this.getItem().getMetadata(), compound));
        }
        this.isDead = true;
    }

}
