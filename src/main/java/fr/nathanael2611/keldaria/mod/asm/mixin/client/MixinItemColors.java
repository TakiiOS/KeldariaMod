/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.MixinHooks;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemColors.class)
public abstract class MixinItemColors
{

    @Final
    @Shadow
    private final java.util.Map<net.minecraftforge.registries.IRegistryDelegate<Item>, IItemColor> itemColorMap = com.google.common.collect.Maps.newHashMap();


    /**
     * @author
     */
    @Overwrite
    public int colorMultiplier(ItemStack stack, int tintIndex)
    {
        return MixinHooks.colorMultiplier(this.itemColorMap, stack, tintIndex);
    }

}
