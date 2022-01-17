package com.semivanilla.fasttravel.manager;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.PlayerData;
import com.semivanilla.fasttravel.model.Waypoint;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PlayerManager {

    private final FastTravel plugin;
    private final HashMap<UUID, PlayerData> playerDataHashMap;

    public PlayerManager(FastTravel plugin) {
        this.plugin = plugin;
        this.playerDataHashMap = new HashMap<>();
    }

    public void cachePlayerData(@NotNull PlayerData data) {
        playerDataHashMap.put(data.playerID(), data);
    }

    public boolean isUnlocked(@NotNull UUID uuid, String waypoint) {
        return (playerDataHashMap.get(uuid).visitedPoints().contains(waypoint));
    }

    public Iterator<Waypoint> getAllUnlockedWaypoints(@NotNull UUID uuid) {
        final List<Waypoint> waypoints = new ArrayList<>();
        playerDataHashMap.get(uuid).visitedPoints().forEach((waypointName) -> {
            if (plugin.getWaypointManager().contains(waypointName))
                waypoints.add(plugin.getWaypointManager().getWaypoint(waypointName));
        });
        return waypoints.iterator();
    }

    public void unlockWaypointForPlayer(@NotNull UUID uuid, String waypoint) {
        if (playerDataHashMap.containsKey(uuid)) {
            final PlayerData data = playerDataHashMap.get(uuid);
            data.visitedPoints().add(waypoint);
            plugin.getDataStorage().update(data);
            //TODO Add messages
        } else {
            plugin.getLogger().severe("The plugin seems to have not cached " + uuid + " while the player is in server, which is impossible. Something is wrong!");
        }
    }

    public void unloadPlayerData(@NotNull UUID uuid) {
        playerDataHashMap.remove(uuid);
    }


}
