package com.semivanilla.fasttravel.manager;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.Location;
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
        return getAllWaypoints().stream().filter(Waypoint::isActive).filter(waypoint -> waypoint.isInside(location)).findAny();
    }

    public void createNewWaypoint(@NotNull Location location, @NotNull String name){
        plugin.getFileHandler().getWaypointConfiguration().insertNewWayPoint(name,Waypoint.serializeRawWaypoint(location));
        waypointHashMap.put(name,Waypoint.buildFrom(name,location));
    }

    public void removeWaypoint(@NotNull String name){
        if(contains(name))
            waypointHashMap.remove(name);

        plugin.getFileHandler().getWaypointConfiguration().removeWaypoint(name);
    }

    public void setActiveFor(@NotNull String name, boolean status){
        waypointHashMap.get(name).setActive(status);
    }


    public boolean isWaypointActive(@NotNull String name){
        if(contains(name))
            return waypointHashMap.get(name).isActive();
        else return false;
    }

    public boolean updateToPluginCache(@NotNull String name){
        if(contains(name))
            waypointHashMap.remove(name);

        Optional<Waypoint> optionalWaypoint = plugin.getFileHandler().getWaypointConfiguration().fetchWaypoint(name);
        if(optionalWaypoint.isPresent()){
            this.insert(optionalWaypoint);
            return true;
        }else {
            plugin.getLogger().severe("Update failed. Waypoint Object seems to be null!");
            return false;
        }
    }

    public void prepareWaypointReload(){
        waypointHashMap.clear();
    }

}
