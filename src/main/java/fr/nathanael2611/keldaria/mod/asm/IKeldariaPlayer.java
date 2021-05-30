/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm;

import fr.nathanael2611.keldaria.mod.clothe.InventoryClothes;
import fr.nathanael2611.keldaria.mod.features.accessories.InventoryAccessories;
import fr.nathanael2611.keldaria.mod.inventory.InventoryArmor;

public interface IKeldariaPlayer
{

    InventoryClothes getInventoryCloths();

    InventoryAccessories getInventoryAccessories();

    InventoryArmor getInventoryArmor();

}
