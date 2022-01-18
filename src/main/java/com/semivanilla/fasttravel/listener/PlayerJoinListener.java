package com.semivanilla.fasttravel.listener;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.model.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener extends AbstractListener implements Listener {

    public PlayerJoinListener(FastTravel plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        getDataStorage().getIfPresent(player.getUniqueId()).thenAccept(playerData -> {
            if (playerData.isPresent()) {
                System.out.println("Is present");
                getPlayerManager().cachePlayerData(playerData.get());
            } else {
                System.out.println("Is not present");
                final PlayerData newData = PlayerData.from(player);
                getPlayerManager().cachePlayerData(newData);
                getDataStorage().register(player.getUniqueId());
            }
        });
    }
}
