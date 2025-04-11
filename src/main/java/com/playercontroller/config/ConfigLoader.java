package com.playercontroller.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return System.getProperty("base.url", props.getProperty("base.url"));
    }

    public static int getThreadCount() {
        return Integer.parseInt(System.getProperty("thread.count", props.getProperty("thread.count", "1")));
    }
}
