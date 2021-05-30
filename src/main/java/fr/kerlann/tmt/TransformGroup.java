/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.kerlann.tmt;

import net.minecraft.util.math.Vec3d;

public abstract class TransformGroup
{
	public abstract double getWeight();
	
	public abstract Vec3d doTransformation(PositionTransformVertex vertex);
}
