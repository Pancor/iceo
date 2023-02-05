package org.example.helpers;

import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {

    private static final String CONFIG_FILE_PATH = "config.properties";

    public static String getBaseUrl() {
        Properties properties = getProperties();
        return properties.getProperty("baseUrl");
    }

    public static String getApiKey() {
        Properties properties = getProperties();
        return properties.getProperty("apiKey");
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = classloader.getResourceAsStream(CONFIG_FILE_PATH)){
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return properties;
    }
}
