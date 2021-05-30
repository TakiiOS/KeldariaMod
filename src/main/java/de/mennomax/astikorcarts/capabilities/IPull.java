/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.capabilities;

import de.mennomax.astikorcarts.entity.AbstractDrawn;

public interface IPull
{
    public void setDrawn(AbstractDrawn drawnIn);

    public AbstractDrawn getDrawn();
}