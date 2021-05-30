/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml;

/**
 * Rounding modes.
 * 
 * @author Kai Burjack
 */
public class RoundingMode {
    private RoundingMode() {}
    /**
     * Discards the fractional part.
     */
    public static final int TRUNCATE = 0;
    /**
     * Round towards positive infinity.
     */
    public static final int CEILING = 1;
    /**
     * Round towards negative infinity.
     */
    public static final int FLOOR = 2;
    /**
     * Round towards the nearest neighbor. If both neighbors are equidistant, round
     * towards the even neighbor.
     */
    public static final int HALF_EVEN = 3;
    /**
     * Round towards the nearest neighbor. If both neighbors are equidistant, round
     * down.
     */
    public static final int HALF_DOWN = 4;
    /**
     * Round towards the nearest neighbor. If both neighbors are equidistant, round
     * up.
     */
    public static final int HALF_UP = 5;
}
