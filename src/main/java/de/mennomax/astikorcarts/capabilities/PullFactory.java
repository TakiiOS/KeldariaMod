/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package de.mennomax.astikorcarts.capabilities;

import de.mennomax.astikorcarts.entity.AbstractDrawn;

public class PullFactory implements IPull
{
    private AbstractDrawn drawn = null;

    @Override
    public void setDrawn(AbstractDrawn drawnIn)
    {
        this.drawn = drawnIn;
    }

    @Override
    public AbstractDrawn getDrawn()
    {
        return this.drawn;
    }

}