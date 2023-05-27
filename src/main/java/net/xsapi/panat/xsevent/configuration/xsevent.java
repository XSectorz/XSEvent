package net.xsapi.panat.xsevent.configuration;

import net.xsapi.panat.xsevent.core.core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class xsevent {
    public static File customConfigFile;

    public static FileConfiguration customConfig;

    public FileConfiguration getConfig() {
        return customConfig;
    }

    public void loadConfigu() {
        customConfigFile = new File(core.getPlugin().getDataFolder(), "xsevent.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            core.getPlugin().saveResource("xsevent.yml", false);
        }
        customConfig = (FileConfiguration) new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        customConfigFile = new File(core.getPlugin().getDataFolder(), "xsevent.yml");
        try {
            customConfig.options().copyDefaults(true);
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        xsevent.customConfigFile = new File(core.getPlugin().getDataFolder(), "xsevent.yml");
        if (!xsevent.customConfigFile.exists()) {
            xsevent.customConfigFile.getParentFile().mkdirs();
            core.getPlugin().saveResource("xsevent.yml", false);
        } else {
            xsevent.customConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(customConfigFile);
            try {
                xsevent.customConfig.save(xsevent.customConfigFile);
                core.getPlugin().reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}