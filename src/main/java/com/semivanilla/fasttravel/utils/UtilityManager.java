package com.semivanilla.fasttravel.utils;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.utils.plugin.FileUtils;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;

public class UtilityManager {

    private final FastTravel plugin;
    private final MiniMessageUtils miniMessageUtils;
    private final FileUtils fileUtils;

    public UtilityManager(FastTravel plugin) {
        this.plugin = plugin;
        this.miniMessageUtils = new MiniMessageUtils(this);
        this.fileUtils = new FileUtils(this);
    }

    public FastTravel getPlugin() {
        return plugin;
    }

    public MiniMessageUtils getMiniMessageUtils() {
        return miniMessageUtils;
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }
}
