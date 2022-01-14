package com.semivanilla.fasttravel;

import com.semivanilla.fasttravel.model.Waypoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Test implements CommandExecutor, Listener {

    private final FastTravel plugin;

    public Test(FastTravel plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        return true;
    }

    @EventHandler
    public void onSneek(PlayerToggleSneakEvent event){
        if(event.isCancelled())
            return;
        final boolean sneeking = event.isSneaking();
        final Player player = event.getPlayer();

        if(sneeking) {
            Optional<Waypoint> waypoint = plugin.getWaypointManager().getIfInsideWaypoint(player.getLocation());
            if(waypoint.isPresent()){
                plugin.getLogger().info("Waypoint is present");
                Waypoint waypoint1 = waypoint.get();
                plugin.getLogger().info(waypoint1.getName());
            }else {
                plugin.getLogger().info("Waypoint is not present");
            }
        }

    }
}
