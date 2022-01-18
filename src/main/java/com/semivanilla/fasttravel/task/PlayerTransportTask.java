package com.semivanilla.fasttravel.task;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerTransportTask extends BukkitRunnable {

    private final FastTravel plugin;
    private final Player player;
    private final Waypoint waypoint;

    private boolean playerMoved;

    private int counter;

    public PlayerTransportTask(FastTravel plugin, Player player, Waypoint waypoint) {
        this.plugin = plugin;
        this.player = player;
        this.waypoint = waypoint;
        this.counter = 0;
        playerMoved = false;
    }

    @Override
    public void run() {
        if (counter >= plugin.getFileHandler().getConfiguration().getTeleportationDelay()) {
            this.plugin.getPlayerManager().teleportPlayer(player, waypoint);
            this.cancelTimer();
            return;
        }

        if (playerMoved) {
            this.cancelTimer();
            //TODO add message for movement cancelled
            return;
        }

        counter++;
    }

    public void cancelTimer() {
        this.cancel();
        this.plugin.getPlayerManager().removeTransportTask(this.player.getUniqueId());
        ;
    }

    public void setPlayerMoved(boolean playerMoved) {
        this.playerMoved = playerMoved;
    }
}
