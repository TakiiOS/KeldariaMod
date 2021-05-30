/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package fr.nathanael2611.keldaria.mod.asm.mixin.client;

import fr.nathanael2611.keldaria.mod.asm.MixinClientHooks;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MovementInputFromOptions.class)
public abstract class MixinMovmentInputFromOption extends MovementInput
{

    @Shadow @Final private GameSettings gameSettings;

    @Overwrite
    public void updatePlayerMoveState()
    {
        MixinClientHooks.processMovementInputs((MovementInputFromOptions) (Object) this, this.gameSettings);
    }

}
