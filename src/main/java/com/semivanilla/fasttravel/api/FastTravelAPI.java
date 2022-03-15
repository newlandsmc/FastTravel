package com.semivanilla.fasttravel.api;

import com.semivanilla.fasttravel.model.PlayerData;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FastTravelAPI {

    boolean isFastTravelPoint(@NotNull Location location);

    boolean isInsideFastTravelPoint(@NotNull Player player);

    Optional<Waypoint> getWaypointWithLocation(@NotNull Location location);

    Optional<Waypoint> getWaypointWithName(@NotNull String waypointName);

    List<Waypoint> getAllWaypoints();

    List<Waypoint> getAllActiveWaypoint();

    boolean isWaypointActive(@NotNull String name);

    boolean disableWaypoint(@NotNull String name);

    boolean enableWaypoint(@NotNull String name);

    CompletableFuture<Boolean> updateWaypointFromConfig(@NotNull String name);

    Optional<PlayerData> getPlayerData(@NotNull UUID playerUID);
}
