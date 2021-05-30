/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.crafting.storage;

import fr.nathanael2611.keldaria.mod.crafting.storage.api.IKnownRecipes;
import fr.nathanael2611.keldaria.mod.crafting.storage.impl.KnownRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class KnownRecipesProvider implements ICapabilitySerializable<NBTBase>
{

    private IKnownRecipes knownRecipes;

    public KnownRecipesProvider(IKnownRecipes manager)
    {
        this.knownRecipes = manager;
    }

    public KnownRecipesProvider()
    {
        this.knownRecipes = new KnownRecipes();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return this.hasCapability(capability, facing) ? KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES.cast(this.knownRecipes) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES.writeNBT(this.knownRecipes, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        KnownRecipesStorage.CAPABILITY_KNOWN_RECIPES.readNBT(this.knownRecipes, null, nbt);
    }
}
