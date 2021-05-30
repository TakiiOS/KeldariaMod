/**
 * Copyright 2019-2021 Keldaria. Tous droits réservés.
 * Toute reproduction, diffusion, partage, distribution,
 * commercialisation sans autorisation explicite est interdite.
 */
package org.joml;

/**
 * Exception thrown when using an invalid JOML runtime configuration.
 * 
 * @author Kai Burjack
 */
public class ConfigurationException extends RuntimeException {
    private static final long serialVersionUID = -7832356906364070687L;
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
