/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.jre.java.io;

import java.io.IOException;

public interface ObjectInput extends DataOutput {

	double readDouble() throws IOException;
	float readFloat() throws IOException;
	int readInt() throws IOException;

}
