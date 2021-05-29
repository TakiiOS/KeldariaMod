package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.ILivingHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityHorse.class)
public abstract class MixinEntityHorse extends AbstractHorse
{
    public MixinEntityHorse(World worldIn)
    {
        super(worldIn);
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityDonkey) && !(otherAnimal instanceof EntityHorse))
        {
            return false;
        }
        else
        {
            AbstractHorse other = (AbstractHorse) otherAnimal;
            boolean can = this.canMate() &&
                    (
                            !other.isBeingRidden()
                                    && !other.isRiding()
                                    && other.isTame()
                                    && !other.isChild()
                                    && other.isInLove()
                                    && ((ILivingHorse) other).getGender() != ((ILivingHorse) this).getGender()
                    );
            return can;
        }
    }
}
