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
        final Player player = (Player) sender;
        if(plugin.getWaypointManager().getIfInsideWaypoint(player.getLocation()).isPresent()){
            sender.sendMessage("Already Exists");
            return true;
        }

        if(plugin.getWaypointManager().contains(args[0])){
            sender.sendMessage("Name already Exists");
            return true;
        }

        plugin.getWaypointManager().createNewWaypoint(player.getLocation(),args[0]);
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
                Waypoint waypoint1 = waypoint.get();
                player.sendMessage("This is a waypoint of "+waypoint1.getName());
            }
        }

    }
}
