package org.example.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading properties from the "application.yml" file.
 */
public final class ConfigUtil {
    private static final Properties properties = new Properties();

    private ConfigUtil() {
    }

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (input != null)
                properties.load(input);
            else
                System.out.println("Sorry, unable to find application.yml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves the property value associated with the given key from the loaded properties.
     * @param key The key whose associated value is to be returned
     * @return The value associated with the specified key, or null if no property with that key exists
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
