package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;
import com.semivanilla.fasttravel.model.Waypoint;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WaypointConfiguration extends AbstractFile {

    public WaypointConfiguration(FileHandler handler) {
        super(handler);
    }

    @Override
    public boolean initConfig() {
        file = handler.getPlugin().getUtilityManager().getFileUtils().createYamlFile("waypoints.yml");
        return file != null;
    }

    @Override
    public void loadConfig() {
        List<Waypoint> waypointList = new ArrayList<>();
        file.singleLayerKeySet().stream().iterator().forEachRemaining((s) -> {
            Optional<Waypoint> loadingPoint = deserializeWaypoint(s);
            if(loadingPoint.isPresent()) {
                waypointList.add(loadingPoint.get());
                handler.getPlugin().getLogger().info("Added "+s+" to the waypoint");
            }else {
                handler.getPlugin().getLogger().info("Unable to add "+s+" to the waypoint");
            }
        });

        handler.getPlugin().getWaypointManager().insert(waypointList);
    }

    public void insertNewWayPoint(@NotNull String name, Map<String, Object> rawMap){
        this.file.set(name, rawMap);
    }

    public void removeWaypoint(@NotNull String name){
        if(this.file.contains(name))
            file.remove(name);
    }

    public boolean contains(@NotNull String name){
        return this.file.contains(name);
    }

    public Optional<Waypoint> fetchWaypoint(@NotNull String name){
        return deserializeWaypoint(name);
    }
}
