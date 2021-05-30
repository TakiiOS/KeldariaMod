/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.fight.attacks.weapons;

import com.google.common.collect.Lists;
import fr.nathanael2611.keldaria.mod.fight.attacks.Attack;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public abstract class Weapon
{

    public List<Attack.AttackPart> parts = Lists.newArrayList();

    public Weapon()
    {
    }

    public abstract Attack createAttack(EntityPlayer player, float renderYawOffset, SwingTypes type);

}
