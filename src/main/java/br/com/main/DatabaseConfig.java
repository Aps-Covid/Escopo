package br.com.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Arquivo database.properties não encontrado no classpath");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo database.properties: " + e.getMessage());
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUser() {
        return properties.getProperty("db.user");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getName() {
        return properties.getProperty("db.name");
    }

    public static String getPB() {
        return properties.getProperty("pb.powerbi");
    }

    public static String getUrlCompleta() {
        return getUrl() + getName();
    }
}
