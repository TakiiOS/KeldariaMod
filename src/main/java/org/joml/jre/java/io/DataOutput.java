/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.jre.java.io;

import java.io.IOException;

public interface DataOutput {
	void writeInt(int value) throws IOException;
	void writeFloat(float value) throws IOException;
	void writeDouble(double value) throws IOException;
}
