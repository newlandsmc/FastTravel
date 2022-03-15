package com.semivanilla.fasttravel;

import com.semivanilla.fasttravel.api.FastTravelAPI;
import com.semivanilla.fasttravel.model.PlayerData;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PluginAPI implements FastTravelAPI {

    private final FastTravel plugin;

    public PluginAPI(FastTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isFastTravelPoint(@NotNull Location location) {
        return plugin.getWaypointManager().getIfInsideWaypoint(location).isPresent();
    }

    @Override
    public boolean isInsideFastTravelPoint(@NotNull Player player) {
        return isFastTravelPoint(player.getLocation());
    }

    @Override
    public Optional<Waypoint> getWaypointWithLocation(@NotNull Location location) {
        return plugin.getWaypointManager().getIfInsideWaypoint(location);
    }

    @Override
    public Optional<Waypoint> getWaypointWithName(@NotNull String waypointName) {
        return Optional.ofNullable(plugin.getWaypointManager().getWaypoint(waypointName));
    }

    @Override
    public List<Waypoint> getAllWaypoints() {
        return plugin.getWaypointManager().getAllWaypoints();
    }

    @Override
    public List<Waypoint> getAllActiveWaypoint() {
        return plugin.getWaypointManager().getAllActiveWaypoints();
    }

    @Override
    public boolean isWaypointActive(@NotNull String name) {
        return plugin.getWaypointManager().isWaypointActive(name);
    }

    @Override
    public boolean disableWaypoint(@NotNull String name) {
        if (plugin.getWaypointManager().contains(name)) {
            plugin.getWaypointManager().setActiveFor(name, false);
            return true;
        } else return false;
    }

    @Override
    public boolean enableWaypoint(@NotNull String name) {
        if (plugin.getWaypointManager().contains(name)) {
            plugin.getWaypointManager().setActiveFor(name, true);
            return true;
        } else return false;
    }

    @Override
    public CompletableFuture<Boolean> updateWaypointFromConfig(@NotNull String name) {
        CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
        future.complete(plugin.getWaypointManager().updateToPluginCache(name));
        return future;
    }

    @Override
    public Optional<PlayerData> getPlayerData(@NotNull UUID playerUID) {
        return Optional.ofNullable(plugin.getPlayerManager().getPlayerDataOf(playerUID));
    }

}
