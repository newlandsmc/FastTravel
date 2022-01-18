package com.semivanilla.fasttravel.hook.squaremap.world;

import com.semivanilla.fasttravel.hook.squaremap.Squaremap;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.util.HashMap;
import java.util.Optional;

public final class WorldHandler {

    public final static Key LAYER_KEY = Key.key("fasttravel");
    private final Squaremap hook;
    private final MapWorld mapWorld;
    private final SimpleLayerProvider provider;
    private final HashMap<String, Key> registeredKeys;


    public WorldHandler(Squaremap hook, MapWorld mapWorld) {
        this.hook = hook;
        this.mapWorld = mapWorld;
        this.provider = SimpleLayerProvider.builder(hook.getManager().getPlugin().getName())
                .defaultHidden(false)
                .showControls(true)
                .build();
        this.registeredKeys = new HashMap<>();
    }

    public WorldHandler registerLayer() {
        mapWorld.layerRegistry().register(LAYER_KEY, this.provider);
        return this;
    }

    public void showIcon(@NotNull String name, @NotNull Location location, @NotNull String iconName) {
        final Optional<Key> iconKey = this.hook.getImgRegister().getImageKey(iconName);
        Key iconImageKey = null;
        if (iconKey.isEmpty()) {
            hook.getManager().getPlugin().getLogger().warning("The specified icon for the waypoint " + name + " does not seems to exist, The plugin will automatically try to get default marker. If failed will be aborted");
            if (hook.getImgRegister().containsImage(Waypoint.DEFAULT_ICON)) {
                iconImageKey = hook.getImgRegister().getImageKey(Waypoint.DEFAULT_ICON).get();
            } else {
                hook.getManager().getPlugin().getLogger().severe("Default icon is not available in cache. Its better to restart the server ASAP!");
            }
        } else {
            iconImageKey = iconKey.get();
        }

        if (iconImageKey == null) {
            hook.getManager().getPlugin().getLogger().warning("Failed to set icon for waypoint " + name + " as icon provided " + iconName + " does not seems to be registered.");
            return;
        }
        final Icon icon = Marker.icon(BukkitAdapter.point(location), iconImageKey, hook.getManager().getPlugin().getFileHandler().getConfiguration().getIconSize());
        icon.markerOptions(MarkerOptions.builder().hoverTooltip(name));

        final Key key = Key.key(name);
        registeredKeys.put(name, key);
        this.provider.addMarker(Key.key(name), icon);
    }

    public void removeIcon(@NotNull String name) {
        if (registeredKeys.containsKey(name)) {
            this.provider.removeMarker(registeredKeys.get(name));
            this.registeredKeys.remove(name);
        }
    }

    public void clearMarkers() {
        this.provider.clearMarkers();
    }

}
