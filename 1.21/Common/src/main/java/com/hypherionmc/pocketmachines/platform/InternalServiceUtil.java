package com.hypherionmc.pocketmachines.platform;

import com.hypherionmc.pocketmachines.ModConstants;

import java.util.ServiceLoader;

/**
 * @author HypherionSA
 * Utility class to handle SPI loading
 */
class InternalServiceUtil {

    /**
     * Try to load a service
     *
     * @param clazz The service class type to load
     * @return The loaded class
     */
    static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ModConstants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}