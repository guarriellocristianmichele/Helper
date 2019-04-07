package me.mrcookies.helper.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private Configuration yml;
    private File file;
    private HashMap<String, Object> defaults = new HashMap<>();

    public ConfigManager(String name, String folder) {
        this.file = new File(folder + "/" + name + ".yml");

        if (!new File(folder).exists()) {
            new File(folder).mkdir();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a default value to config
     */
    public void addDefault(String path, Object value) {
        if (yml.get(path) == null) defaults.put(path, value);
    }

    /**
     * Save default configuration
     */
    public void saveDefaults() {
        for (Map.Entry<String, Object> e : defaults.entrySet()) {
            yml.set(e.getKey(), e.getValue());
        }
        defaults.clear();
        save();
    }

    /**
     * Save changes
     */
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get configuration instance
     */
    public Configuration getYml() {
        return yml;
    }
}
