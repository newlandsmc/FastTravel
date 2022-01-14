package com.semivanilla.fasttravel.command.core;

import com.semivanilla.fasttravel.FastTravel;

public final class CommandHandler {

    private final FastTravel plugin;

    public CommandHandler(FastTravel plugin) {
        this.plugin = plugin;
    }

    public FastTravel getPlugin() {
        return plugin;
    }
}
