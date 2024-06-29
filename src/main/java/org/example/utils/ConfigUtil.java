package org.example.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ConfigUtil {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null)
                properties.load(input);
            else
                System.out.println("Sorry, unable to find application.properties");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ConfigUtil() {
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
