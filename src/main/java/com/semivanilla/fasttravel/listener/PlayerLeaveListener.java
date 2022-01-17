package com.semivanilla.fasttravel.listener;

import com.semivanilla.fasttravel.FastTravel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerLeaveListener extends AbstractListener implements Listener {

    public PlayerLeaveListener(FastTravel plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        getPlayerManager().unloadPlayerData(player.getUniqueId());
    }

}
