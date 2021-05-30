/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.sampling;

/**
 * Callback used for notifying about a new generated 2D sample.
 * 
 * @author Kai Burjack
 */
public interface Callback2d {
    /**
     * Will be called whenever a new sample with the given coordinates <code>(x, y)</code> is generated.
     * 
     * @param x
     *            the x coordinate of the new sample point
     * @param y
     *            the y coordinate of the new sample point
     */
    void onNewSample(float x, float y);
}
