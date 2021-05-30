/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.jre.java.text;

public abstract class NumberFormat extends Format {
	public final String format(double number) {
		return Double.toString(number);
	}
	public final String format(long number) {
	    return Long.toString(number);
	}
}
