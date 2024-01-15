package agh.ics.oop.model.util;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import agh.ics.oop.model.ModelConfiguration;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ConfigurationManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIGURATION_FILE_PATH = "configuration/configuration.json";
    private static Map<String, ModelConfiguration> configurations = new HashMap<>();

    public static void loadConfigurationsFromFile() throws IOException {
        URL url = ConfigurationManager.class.getClassLoader().getResource(CONFIGURATION_FILE_PATH);
        if(url == null)
            throw new FileNotFoundException("Configuration file not found");

        String path = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
        File configurationFile = new File(path);

        String json = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(configurationFile))) {
            json = reader.lines().reduce("", (acc, line) -> acc + line);
        } catch (IOException ex) {
            System.out.printf("Failed to read configuration file: %s, with error: %s\n", configurationFile.getPath(), ex.getMessage());
        }

        if(json == null)
            throw new IOException("Failed to read configuration file");

        Type mapType = new TypeToken<Map<String, ModelConfiguration>>(){}.getType();
        configurations = gson.fromJson(json, mapType);
    }

    public static void saveConfigurationToFiles() throws IOException {
        String json = gson.toJson(configurations);

        URL url = ConfigurationManager.class.getClassLoader().getResource(CONFIGURATION_FILE_PATH);
        if(url == null)
            throw new FileNotFoundException("Configuration file not found");

        String path = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
        File configurationFile = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configurationFile))) {
            writer.write(json);
        } catch (IOException ex) {
            System.out.printf("Failed to write configuration file: %s, with error: %s\n", configurationFile.getPath(), ex.getMessage());
        }
    }

    public static List<String> getConfigurationNames() {
        return new ArrayList<>(configurations.keySet());
    }

    public static ModelConfiguration getConfiguration(String name) {
        if(!configurations.containsKey(name))
            throw new IllegalArgumentException("Configuration with given name does not exist");

        return configurations.get(name);
    }

    public static void addConfiguration(String name, ModelConfiguration configuration) {
        if(configurations.containsKey(name))
            throw new IllegalArgumentException("Configuration with given name already exists");

        configurations.put(name, configuration);
    }

    public static void removeConfiguration(String name) {
        if(!configurations.containsKey(name))
            throw new IllegalArgumentException("Configuration with given name does not exist");

        configurations.remove(name);
    }
}
