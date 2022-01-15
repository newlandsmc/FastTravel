package com.semivanilla.fasttravel.files.core;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.files.Configuration;
import com.semivanilla.fasttravel.files.IconLoader;
import com.semivanilla.fasttravel.files.WaypointConfiguration;

public class FileHandler {

    private final FastTravel plugin;
    private final Configuration configuration;
    private final WaypointConfiguration waypointConfiguration;
    private final IconLoader iconLoader;

    public FileHandler(FastTravel plugin) {
        this.plugin = plugin;
        configuration = new Configuration(this);
        waypointConfiguration = new WaypointConfiguration(this);
        iconLoader = new IconLoader(this);
    }

    public boolean createConfigurationFiles() {
        return configuration.initConfig() && iconLoader.initConfig() && waypointConfiguration.initConfig();
    }

    public void loadAllConfigs() {
        configuration.loadConfig();
        iconLoader.loadConfig();
        waypointConfiguration.loadConfig();
    }

    public boolean reloadAllConfiguration() {
        if (!createConfigurationFiles())
            return false;

        loadAllConfigs();
        return true;
    }

    public FastTravel getPlugin() {
        return plugin;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public WaypointConfiguration getWaypointConfiguration() {
        return waypointConfiguration;
    }

    public IconLoader getIconLoader() {
        return iconLoader;
    }
}

