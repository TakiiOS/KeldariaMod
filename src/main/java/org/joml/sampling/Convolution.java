/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.sampling;

//#ifdef __HAS_NIO__
import java.nio.FloatBuffer;
//#endif

import org.joml.Math;

/**
 * Generates various convolution kernels.
 *
 * @author Kai Burjack
 */
public class Convolution {

//#ifdef __HAS_NIO__
    /**
     * Generate a Gaussian convolution kernel with the given number of rows and columns, and store
     * the factors in row-major order in <code>dest</code>.
     *
     * @param rows
     *          the number of rows (must be an odd number)
     * @param cols
     *          the number of columns (must be an odd number)
     * @param sigma
     *          the standard deviation of the filter kernel values
     * @param dest
     *          will hold the kernel factors in row-major order
     */
    public static void gaussianKernel(int rows, int cols, float sigma, FloatBuffer dest) {
        if ((rows & 1) == 0) {
            throw new IllegalArgumentException("rows must be an odd number");
        }
        if ((cols & 1) == 0) {
            throw new IllegalArgumentException("cols must be an odd number");
        }
        if (dest == null) {
            throw new IllegalArgumentException("dest must not be null");
        }
        if (dest.remaining() < rows * cols) {
            throw new IllegalArgumentException("dest must have at least " + (rows * cols) + " remaining values");
        }
        float sum = 0.0f;
        int pos = dest.position();
        for (int i = 0, y = -(rows - 1) / 2; y <= (rows - 1) / 2; y++) {
            for (int x = -(cols - 1) / 2; x <= (cols - 1) / 2; x++, i++) {
                float k = (float) Math.exp(-(y * y + x * x) / (2.0 * sigma * sigma));
                dest.put(pos + i, k);
                sum += k;
            }
        }
        for (int i = 0; i < rows * cols; i++) {
            dest.put(pos + i, dest.get(pos + i) / sum);
        }
    }
//#endif

    /**
     * Generate a Gaussian convolution kernel with the given number of rows and columns, and store
     * the factors in row-major order in <code>dest</code>.
     *
     * @param rows
     *          the number of rows (must be an odd number)
     * @param cols
     *          the number of columns (must be an odd number)
     * @param sigma
     *          the standard deviation of the filter kernel values
     * @param dest
     *          will hold the kernel factors in row-major order
     */
    public static void gaussianKernel(int rows, int cols, float sigma, float[] dest) {
        if ((rows & 1) == 0) {
            throw new IllegalArgumentException("rows must be an odd number");
        }
        if ((cols & 1) == 0) {
            throw new IllegalArgumentException("cols must be an odd number");
        }
        if (dest == null) {
            throw new IllegalArgumentException("dest must not be null");
        }
        if (dest.length < rows * cols) {
            throw new IllegalArgumentException("dest must have a size of at least " + (rows * cols));
        }
        float sum = 0.0f;
        for (int i = 0, y = -(rows - 1) / 2; y <= (rows - 1) / 2; y++) {
            for (int x = -(cols - 1) / 2; x <= (cols - 1) / 2; x++, i++) {
                float k = (float) Math.exp(-(y * y + x * x) / (2.0 * sigma * sigma));
                dest[i] = k;
                sum += k;
            }
        }
        for (int i = 0; i < rows * cols; i++) {
            dest[i] = dest[i] / sum;
        }
    }

}
