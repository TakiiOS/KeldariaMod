/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.fight;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class FightProvider implements ICapabilitySerializable<NBTBase>
{

	private FightControl<?> control;

	public FightProvider(FightControl<?> manager)
	{
		this.control = manager;
	}

	public FightProvider(Entity entity)
	{
		this.control = new FightControl(entity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == FightStorage.CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return this.hasCapability(capability, facing) ? FightStorage.CAPABILITY.cast(this.control) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
		return FightStorage.CAPABILITY.writeNBT(this.control, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		FightStorage.CAPABILITY.readNBT(this.control, null, nbt);
	}
}