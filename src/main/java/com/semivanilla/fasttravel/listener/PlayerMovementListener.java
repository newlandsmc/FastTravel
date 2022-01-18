package com.semivanilla.fasttravel.listener;

import com.semivanilla.fasttravel.FastTravel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener extends AbstractListener implements Listener {

    public PlayerMovementListener(FastTravel plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        //Checking if the player is awaiting a teleport
        if (!getPlayerManager().isPlayerOnTransportQueue(player))
            return;

        //Checking if the player has moved a block at least so that just rotating doesn't count as movement
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ())
            return;

        getPlayerManager().updatePlayerMovedOnTask(player);
    }

}
