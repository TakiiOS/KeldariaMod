/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.sampling;

import org.joml.Random;

/**
 * Creates samples on a spiral around a center point.
 * 
 * @author Kai Burjack
 */
public class SpiralSampling {
    private final Random rnd;

    /**
     * Create a new instance of {@link SpiralSampling} and initialize the random number generator with the given <code>seed</code>.
     * 
     * @param seed
     *            the seed to initialize the random number generator with
     */
    public SpiralSampling(long seed) {
        rnd = new Random(seed);
    }

    /**
     * Create <code>numSamples</code> number of samples on a spiral with maximum radius <code>radius</code> around the center using <code>numRotations</code> number of rotations
     * along the spiral, and call the given <code>callback</code> for each sample generated.
     * <p>
     * The generated sample points are distributed with equal angle differences around the spiral, so they concentrate towards the center.
     * 
     * @param radius
     *            the maximum radius of the spiral
     * @param numRotations
     *            the number of rotations of the spiral
     * @param numSamples
     *            the number of samples to generate
     * @param callback
     *            will be called for each sample generated
     */
    public void createEquiAngle(float radius, int numRotations, int numSamples, Callback2d callback) {
        for (int sample = 0; sample < numSamples; sample++) {
            float angle = 2.0f * (float) Math.PI * (sample * numRotations) / numSamples;
            float r = radius * sample / (numSamples - 1);
            float x = (float) Math.sin_roquen_9(angle + 0.5f * (float) Math.PI) * r;
            float y = (float) Math.sin_roquen_9(angle) * r;
            callback.onNewSample(x, y);
        }
    }

    /**
     * Create <code>numSamples</code> number of samples on a spiral with maximum radius <code>radius</code> around the center using <code>numRotations</code> number of rotations
     * along the spiral, and call the given <code>callback</code> for each sample generated.
     * <p>
     * The generated sample points are distributed with equal angle differences around the spiral, so they concentrate towards the center.
     * <p>
     * Additionally, the radius of each sample point is jittered by the given <code>jitter</code> factor.
     * 
     * @param radius
     *            the maximum radius of the spiral
     * @param numRotations
     *            the number of rotations of the spiral
     * @param numSamples
     *            the number of samples to generate
     * @param jitter
     *            the factor by which the radius of each sample point is jittered. Possible values are <code>[0..1]</code>
     * @param callback
     *            will be called for each sample generated
     */
    public void createEquiAngle(float radius, int numRotations, int numSamples, float jitter, Callback2d callback) {
        float spacing = radius / numRotations;
        for (int sample = 0; sample < numSamples; sample++) {
            float angle = 2.0f * (float) Math.PI * (sample * numRotations) / numSamples;
            float r = radius * sample / (numSamples - 1) + (rnd.nextFloat() * 2.0f - 1.0f) * spacing * jitter;
            float x = (float) Math.sin_roquen_9(angle + 0.5f * (float) Math.PI) * r;
            float y = (float) Math.sin_roquen_9(angle) * r;
            callback.onNewSample(x, y);
        }
    }

}
