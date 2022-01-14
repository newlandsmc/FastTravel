package com.semivanilla.fasttravel.files.core;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.files.Configuration;
import com.semivanilla.fasttravel.files.WaypointConfiguration;

public class FileHandler {

    private final FastTravel plugin;
    private final Configuration configuration;
    private final WaypointConfiguration waypointConfiguration;

    public FileHandler(FastTravel plugin) {
        this.plugin = plugin;
        configuration = new Configuration(this);
        waypointConfiguration = new WaypointConfiguration(this);
    }

    public boolean createConfigurationFiles(){
        return configuration.initConfig() && waypointConfiguration.initConfig();
    }

    public void loadAllConfigs(){
        configuration.loadConfig();
        waypointConfiguration.loadConfig();
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
}

