package com.semivanilla.fasttravel.listener;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Optional;

public class PlayerToggleSneakListener extends AbstractListener implements Listener {

    public PlayerToggleSneakListener(FastTravel plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (event.isSneaking()) {
            Optional<Waypoint> waypoint = getWaypointManager().getIfInsideWaypoint(player.getLocation());
            if (waypoint.isEmpty())
                return;

            if (!getPlayerManager().isUnlocked(player.getUniqueId(), waypoint.get().getName())) {
                getPlayerManager().unlockWaypointForPlayer(player.getUniqueId(), waypoint.get().getName());
            }

        }
    }

}
