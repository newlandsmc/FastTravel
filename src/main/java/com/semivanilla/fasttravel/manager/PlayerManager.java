package com.semivanilla.fasttravel.manager;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.PlayerData;
import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.task.PlayerTransportTask;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class PlayerManager {

    private final FastTravel plugin;
    private final HashMap<UUID, PlayerData> playerDataHashMap;
    private final HashMap<UUID, PlayerTransportTask> playerTransportTaskHashMap;

    /**
     * Constructor for invoking the PlayerManager Class
     *
     * @param plugin FastTravel class as main instance
     */
    public PlayerManager(FastTravel plugin) {
        this.plugin = plugin;
        this.playerDataHashMap = new HashMap<>();
        this.playerTransportTaskHashMap = new HashMap<>();
    }

    /**
     * Add new value to the map.
     *
     * @param data {@link PlayerData} class
     */
    public void cachePlayerData(@NotNull PlayerData data) {
        playerDataHashMap.put(data.getPlayerID(), data);
    }

    /**
     * Is a waypoint unlocked for a player
     *
     * @param uuid     UUID of the player
     * @param waypoint Name of the waypoint
     * @return boolean whether unlocked or not
     */
    public boolean isUnlocked(@NotNull UUID uuid, String waypoint) {
        return (playerDataHashMap.get(uuid).getVisitedPoints().contains(waypoint));
    }

    /**
     * Get all unlocked waypoint of player to iterate
     *
     * @param uuid UUID of the player
     * @return {@link Iterator}
     */
    public Iterator<Waypoint> getAllUnlockedWaypoints(@NotNull UUID uuid) {
        final List<Waypoint> waypoints = new ArrayList<>();
        playerDataHashMap.get(uuid).getVisitedPoints().forEach((waypointName) -> {
            if (plugin.getWaypointManager().contains(waypointName))
                waypoints.add(plugin.getWaypointManager().getWaypoint(waypointName));
        });
        return waypoints.iterator();
    }

    /**
     * Unlock a new waypoint for the player. The messages will be also sent from this method
     *
     * @param uuid     UUID of the player
     * @param waypoint Name of the waypoint that needs to be unlocked
     */
    public void unlockWaypointForPlayer(@NotNull UUID uuid, String waypoint) {
        if (playerDataHashMap.containsKey(uuid)) {
            final PlayerData data = playerDataHashMap.get(uuid);
            data.getVisitedPoints().add(waypoint);
            plugin.getDataStorage().update(data);
            MiniMessageUtils.sendMessage(data.getPlayer(), plugin.getFileHandler().getConfiguration().getNewWaypointDiscovered()
                    .replace("%waypoint_name%", waypoint));
        } else {
            plugin.getLogger().severe("The plugin seems to have not cached " + uuid + " while the player is in server, which is impossible. Something is wrong!");
        }
    }

    /**
     * Is player waiting for a teleportation.
     *
     * @param player Player
     * @return boolean Whether he is waiting or not
     */
    public boolean isPlayerOnTransportQueue(@NotNull Player player) {
        return playerTransportTaskHashMap.containsKey(player.getUniqueId());
    }

    /**
     * Adds a new player to the transportation queue
     * NOTE: This also cancels already existing queue
     *
     * @param player   Player to be transported
     * @param waypoint Waypoint he needs to be transported
     */
    public void preparePlayerTransport(@NotNull Player player, @NotNull Waypoint waypoint) {
        if (playerTransportTaskHashMap.containsKey(player.getUniqueId())) {
            playerTransportTaskHashMap.get(player.getUniqueId()).cancelTimer();
            MiniMessageUtils.sendMessage(player, plugin.getFileHandler().getConfiguration().getTeleportCancelledForNewRequest());
        }

        if (playerDataHashMap.containsKey(player.getUniqueId())) {
            PlayerData data = playerDataHashMap.get(player.getUniqueId());
            if (playerDataHashMap.get(player.getUniqueId()).isInCoolDown()) {
                MiniMessageUtils.sendMessage(player, plugin.getFileHandler().getConfiguration().getInCoolDownMessage().replace("%cool-down%", data.getCoolDownInSec()));
                return;
            }

            PlayerTransportTask task = new PlayerTransportTask(this.plugin, player, waypoint);
            playerTransportTaskHashMap.put(player.getUniqueId(), task);
            task.runTaskTimerAsynchronously(plugin, 0, 20);
            MiniMessageUtils.sendMessage(player, plugin.getFileHandler().getConfiguration().getOnQueue());
        }
    }

    /**
     * Teleports a player when the queue gets finished
     *
     * @param player   Player to be transported
     * @param waypoint Waypoint he needs to be transported
     */
    public void teleportPlayer(@NotNull Player player, @NotNull Waypoint waypoint) {
        if (!player.isOnline()) {
            plugin.getLogger().info(player.getName() + " seems to be offline, remove the teleport");
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                MiniMessageUtils.sendMessage(player, plugin.getFileHandler().getConfiguration().getTeleportationCommencing());
                player.teleport(waypoint.getWaypoint());
                setCoolDownForPlayer(player);
            }
        });
    }

    /**
     * Add cool-down to transport for a player
     * This will add the cool-down in millis and the {@link PlayerData#isInCoolDown()} will check whether the time
     * exceeded or not, instead of the inefficient schedulers
     *
     * @param player Player to add cool-down
     */
    public void setCoolDownForPlayer(@NotNull Player player) {
        playerDataHashMap.get(player.getUniqueId()).setCoolDownMillis(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getFileHandler().getConfiguration().getCoolDownForTeleportation()));
    }

    /**
     * Updates whether the player moved or not if he is in a transportation queue
     *
     * @param player player who needs to be updated
     */
    public void updatePlayerMovedOnTask(@NotNull Player player) {
        if (playerTransportTaskHashMap.containsKey(player.getUniqueId()))
            playerTransportTaskHashMap.get(player.getUniqueId()).setPlayerMoved(true);
    }

    /**
     * Remove a player transportation task
     *
     * @param uuid UUID of the player
     */
    public void removeTransportTask(@NotNull UUID uuid) {
        this.playerTransportTaskHashMap.remove(uuid);
    }

    /**
     * Removes player data from the map
     *
     * @param uuid UUID of the player
     */
    public void unloadPlayerData(@NotNull UUID uuid) {
        playerDataHashMap.remove(uuid);
    }


}
