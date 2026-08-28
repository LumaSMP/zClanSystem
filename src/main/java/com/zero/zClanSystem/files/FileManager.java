package com.zero.zClanSystem.files;

import com.zero.zClanSystem.zClanSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final zClanSystem plugin;

    private File clansFile;
    private FileConfiguration clansConfig;

    public FileManager(zClanSystem plugin) {
        this.plugin = plugin;
        createFiles();
    }

    private void createFiles() {
        clansFile = new File(plugin.getDataFolder(), "clans.yml");

        if (!clansFile.exists()) {
            clansFile.getParentFile().mkdirs();
            plugin.saveResource("clans.yml", false);
        }

        clansConfig = YamlConfiguration.loadConfiguration(clansFile);
    }

    public FileConfiguration getClansConfig() {
        return clansConfig;
    }

    public void saveClansConfig() {
        try {
            clansConfig.save(clansFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadClansConfig() {
        clansConfig = YamlConfiguration.loadConfiguration(clansFile);
    }
}
