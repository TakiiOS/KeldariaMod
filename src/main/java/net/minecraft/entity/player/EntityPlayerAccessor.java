/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package net.minecraft.entity.player;

public class EntityPlayerAccessor {
	
	public static void setSleeping(EntityPlayer player, boolean val) {
		player.sleeping = val;
	}
}
