/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package net.minecraft.entity;

public class EntityAccessor {
	
	// access protected methods of classes by package-injection
	
	public static void updateFallState(Entity entity, double dy, boolean isOnGround) {
		entity.updateFallState(dy, isOnGround, entity.world.getBlockState(entity.getPosition().down()), entity.getPosition().down());
	}
	
	public static void setSize(Entity entity, float x, float z) {
		entity.setSize(x, z);
	}
}
