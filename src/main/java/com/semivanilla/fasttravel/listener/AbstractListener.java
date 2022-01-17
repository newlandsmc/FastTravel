package com.semivanilla.fasttravel.listener;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.data.DataImpl;
import com.semivanilla.fasttravel.files.core.FileHandler;
import com.semivanilla.fasttravel.hook.HookManager;
import com.semivanilla.fasttravel.manager.PlayerManager;
import com.semivanilla.fasttravel.manager.WaypointManager;
import com.semivanilla.fasttravel.utils.UtilityManager;

public abstract class AbstractListener {

    private final FastTravel plugin;

    public AbstractListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    protected FileHandler getFileHandler() {
        return plugin.getFileHandler();
    }

    protected DataImpl getDataStorage() {
        return plugin.getDataStorage();
    }

    protected WaypointManager getWaypointManager() {
        return plugin.getWaypointManager();
    }

    protected PlayerManager getPlayerManager() {
        return plugin.getPlayerManager();
    }

    protected HookManager getHookManager() {
        return plugin.getHookManager();
    }

    protected UtilityManager getUtilityManager() {
        return plugin.getUtilityManager();
    }

}
