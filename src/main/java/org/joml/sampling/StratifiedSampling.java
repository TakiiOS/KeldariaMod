/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.sampling;

import org.joml.Random;

/**
 * Creates samples on a unit quad using an NxN strata grid.
 * 
 * @author Kai Burjack
 */
public class StratifiedSampling {

    private final Random rnd;

    /**
     * Create a new instance of {@link StratifiedSampling} and initialize the random number generator with the given
     * <code>seed</code>.
     * 
     * @param seed
     *            the seed to initialize the random number generator with
     */
    public StratifiedSampling(long seed) {
        this.rnd = new Random(seed);
    }

    /**
     * Generate <code>n * n</code> random sample positions in the unit square of <code>x, y = [-1..+1]</code>.
     * <p>
     * Each sample within its stratum is distributed randomly.
     * 
     * @param n
     *            the number of strata in each dimension
     * @param callback
     *            will be called for each generated sample position
     */
    public void generateRandom(int n, Callback2d callback) {
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                float sampleX = (rnd.nextFloat() / n + (float) x / n) * 2.0f - 1.0f;
                float sampleY = (rnd.nextFloat() / n + (float) y / n) * 2.0f - 1.0f;
                callback.onNewSample(sampleX, sampleY);
            }
        }
    }

    /**
     * Generate <code>n * n</code> random sample positions in the unit square of <code>x, y = [-1..+1]</code>.
     * <p>
     * Each sample within its stratum is confined to be within <code>[-centering/2..1-centering]</code> of its stratum.
     * 
     * @param n
     *            the number of strata in each dimension
     * @param centering
     *            determines how much the random samples in each stratum are confined to be near the center of the
     *            stratum. Possible values are <code>[0..1]</code>
     * @param callback
     *            will be called for each generated sample position
     */
    public void generateCentered(int n, float centering, Callback2d callback) {
        float start = centering * 0.5f;
        float end = 1.0f - centering;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                float sampleX = ((start + rnd.nextFloat() * end) / n + (float) x / n) * 2.0f - 1.0f;
                float sampleY = ((start + rnd.nextFloat() * end) / n + (float) y / n) * 2.0f - 1.0f;
                callback.onNewSample(sampleX, sampleY);
            }
        }
    }

}
