package com.semivanilla.fasttravel.manager;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.PlayerData;
import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.task.PlayerTransportTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PlayerManager {

    private final FastTravel plugin;
    private final HashMap<UUID, PlayerData> playerDataHashMap;
    private final HashMap<UUID, PlayerTransportTask> playerTransportTaskHashMap;

    public PlayerManager(FastTravel plugin) {
        this.plugin = plugin;
        this.playerDataHashMap = new HashMap<>();
        this.playerTransportTaskHashMap = new HashMap<>();
    }

    public void cachePlayerData(@NotNull PlayerData data) {
        playerDataHashMap.put(data.getPlayerID(), data);
    }

    public boolean isUnlocked(@NotNull UUID uuid, String waypoint) {
        return (playerDataHashMap.get(uuid).getVisitedPoints().contains(waypoint));
    }

    public Iterator<Waypoint> getAllUnlockedWaypoints(@NotNull UUID uuid) {
        final List<Waypoint> waypoints = new ArrayList<>();
        playerDataHashMap.get(uuid).getVisitedPoints().forEach((waypointName) -> {
            if (plugin.getWaypointManager().contains(waypointName))
                waypoints.add(plugin.getWaypointManager().getWaypoint(waypointName));
        });
        return waypoints.iterator();
    }

    public void unlockWaypointForPlayer(@NotNull UUID uuid, String waypoint) {
        if (playerDataHashMap.containsKey(uuid)) {
            final PlayerData data = playerDataHashMap.get(uuid);
            data.getVisitedPoints().add(waypoint);
            plugin.getDataStorage().update(data);
            //TODO Add messages
        } else {
            plugin.getLogger().severe("The plugin seems to have not cached " + uuid + " while the player is in server, which is impossible. Something is wrong!");
        }
    }

    public boolean isPlayerOnTransportQueue(@NotNull Player player) {
        return playerTransportTaskHashMap.containsKey(player.getUniqueId());
    }

    public void preparePlayerTransport(@NotNull Player player, @NotNull Waypoint waypoint) {
        if (playerTransportTaskHashMap.containsKey(player.getUniqueId())) {
            playerTransportTaskHashMap.get(player.getUniqueId()).cancelTimer();
            //TODO add message for cancellation
        }

        PlayerTransportTask task = new PlayerTransportTask(this.plugin, player, waypoint);
        playerTransportTaskHashMap.put(player.getUniqueId(), task);
        task.runTaskTimerAsynchronously(plugin, 0, 20);
        //TODO Add message for transport
    }

    public void teleportPlayer(@NotNull Player player, @NotNull Waypoint waypoint) {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                player.teleport(waypoint.getWaypoint());

                //TODO Add Message
            }
        });
    }

    public void updatePlayerMovedOnTask(@NotNull Player player) {
        if (playerTransportTaskHashMap.containsKey(player.getUniqueId()))
            playerTransportTaskHashMap.get(player.getUniqueId()).setPlayerMoved(true);
    }

    public void removeTransportTask(@NotNull UUID uuid) {
        this.playerTransportTaskHashMap.remove(uuid);
    }


    public void unloadPlayerData(@NotNull UUID uuid) {
        playerDataHashMap.remove(uuid);
    }


}
