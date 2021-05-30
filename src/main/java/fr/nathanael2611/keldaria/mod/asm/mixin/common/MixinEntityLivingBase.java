/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.common;

import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity
{

    public MixinEntityLivingBase(World worldIn)
    {
        super(worldIn);
    }

    @Shadow public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    @Shadow public abstract boolean isActiveItemStackBlocking();

    @Shadow public abstract Vec3d getLook(float partialTicks);

    @Shadow public abstract ItemStack getActiveItemStack();

    @Overwrite
    private boolean canBlockDamageSource(DamageSource damageSourceIn)
    {
        return MixinHooks.canBlockDamageSource(((EntityLivingBase) (Object) this), damageSourceIn);
    }

}
