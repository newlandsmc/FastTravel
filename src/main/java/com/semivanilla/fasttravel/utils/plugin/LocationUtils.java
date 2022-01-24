package com.semivanilla.fasttravel.utils.plugin;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LocationUtils {

    public static Map<String,Object> serializeLocation(@NotNull Location location){
        final HashMap<String,Object> locationMap = new HashMap<>();
        locationMap.put("world-name",location.getWorld().getName());
        locationMap.put("x",location.getBlockX());
        locationMap.put("y", location.getBlockY());
        locationMap.put("z", location.getBlockZ());
        locationMap.put("yaw", location.getYaw());
        locationMap.put("pitch",location.getPitch());
        return locationMap;
    }

}
