package org.example.filters.config;

import java.util.List;

/**
 * Utility class for handling routing-related functionalities.
 * Provides methods to check if a given path is on a predefined whitelist.
 */
public class Routing {
    private static List<String> whiteList = List.of("/auth/", "/swagger-resources", "/swagger-ui", "/v2/api-docs");

    /**
     * Checks if the provided path is on the whitelist.
     *
     * @param path The path to be checked.
     * @return {@code true} if the path is on the whitelist, {@code false} otherwise.
     */
    public static boolean isPathOnWhiteList(String path) {
        return whiteList.stream().anyMatch(path::startsWith);
    }
}
