/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml.jre.java.io;

public interface ObjectOutput extends DataOutput {
	void writeObject(Object obj);
}
