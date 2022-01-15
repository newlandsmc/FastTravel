package com.semivanilla.fasttravel.hook.placeholders;

import com.semivanilla.fasttravel.hook.HookManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final HookManager manager;

    public PlaceholderAPI(HookManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ft";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Alen_Alex";
    }

    @Override
    public @NotNull String getVersion() {
        return manager.getPlugin().getDescription().getVersion();
    }
}
