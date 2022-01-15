package com.semivanilla.fasttravel.hook;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.hook.placeholders.PlaceholderAPI;
import com.semivanilla.fasttravel.hook.squaremap.Squaremap;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class HookManager {

    private final FastTravel plugin;
    private final PlaceholderAPI placeholderAPIHook;
    private final Squaremap squaremapHook;
    private boolean placeholderHooked, squaremapHooked = false;

    public HookManager(FastTravel plugin) {
        this.plugin = plugin;
        placeholderAPIHook = new PlaceholderAPI(this);
        squaremapHook = new Squaremap(this);
    }

    public void initHooks() {
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderHooked = placeholderAPIHook.register();
            if (placeholderHooked) {
                plugin.getLogger().info("Hook Registered : PlaceholderAPI");
            }
        }
        if (plugin.getServer().getPluginManager().isPluginEnabled("squaremap")) {
            squaremapHooked = squaremapHook.registerSquareMap();
            if (squaremapHooked) {
                squaremapHook.loadImages();
                squaremapHook.registerImagesOnMap();
                plugin.getLogger().info("Hook Registered : Squaremap");
            }
        }
    }

    public void reloadHooks() {
        if (squaremapHooked) {
            squaremapHook.clearMarkers();
            squaremapHook.getImgRegister().unregisterAllImages();
            squaremapHook.loadImages();
            squaremapHook.registerImagesOnMap();
        }
    }

    public FastTravel getPlugin() {
        return plugin;
    }

    public boolean isPlaceholderHooked() {
        return placeholderHooked;
    }

    public boolean isSquaremapHooked() {
        return squaremapHooked;
    }

    public void addIconToMap(@NotNull String name, @NotNull Location location, @NotNull String iconName) {
        if (squaremapHooked) {
            squaremapHook.addIcon(name, location, iconName);
        }
    }

    public void removeIcon(@NotNull String name, @NotNull String worldName) {
        if (squaremapHooked) {
            squaremapHook.removeIcon(name, worldName);
        }
    }

    public void clearIcons() {
        if (squaremapHooked) {
            squaremapHook.clearMarkers();
        }
    }
}
