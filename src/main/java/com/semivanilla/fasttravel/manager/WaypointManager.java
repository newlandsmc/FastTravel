package com.semivanilla.fasttravel.manager;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class WaypointManager {

    private final FastTravel plugin;
    private final HashMap<String, Waypoint> waypointHashMap = new HashMap<String, Waypoint>();

    public WaypointManager(FastTravel plugin) {
        this.plugin = plugin;
    }

    public void insert(@NotNull Waypoint waypoint){
        waypointHashMap.put(waypoint.getName(),waypoint);
    }

    public void insert(@NotNull List<Waypoint> waypoints){
        waypoints.stream().iterator().forEachRemaining((this::insert));
    }

    public void insert(@NotNull Optional<Waypoint> waypoint){
        waypoint.ifPresent(this::insert);
    }

    public boolean contains(@NotNull String name){
        return waypointHashMap.containsKey(name);
    }

    public List<Waypoint> getAllWaypoints(){
        return new ArrayList<>(waypointHashMap.values());
    }

    public Optional<Waypoint> getIfInsideWaypoint(@NotNull Location location){
        return getAllWaypoints().stream().filter(waypoint -> waypoint.isInside(location)).findAny();
    }

    public void createNewWaypoint(@NotNull Location location, @NotNull String name){
        plugin.getFileHandler().getWaypointConfiguration();
    }


}
