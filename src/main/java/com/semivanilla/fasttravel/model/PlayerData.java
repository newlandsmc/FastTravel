package com.semivanilla.fasttravel.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class PlayerData {

    private final UUID playerID;
    private final List<String> visitedPoints;
    private long coolDownMillis;

    public PlayerData(UUID playerID, List<String> visitedPoints) {
        this.playerID = playerID;
        this.visitedPoints = visitedPoints;
        this.coolDownMillis = Long.MIN_VALUE;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerID);
    }

    public List<String> getVisitedPoints() {
        return visitedPoints;
    }

    public long getCoolDownMillis() {
        return coolDownMillis;
    }

    public void setCoolDownMillis(long coolDownMillis) {
        this.coolDownMillis = coolDownMillis;
    }

    public boolean isInCoolDown() {
        return coolDownMillis >= System.currentTimeMillis();
    }

    public String getCoolDownInSec() {
        if (isInCoolDown()) {
            return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(this.coolDownMillis - System.currentTimeMillis()));
        } else {
            return "0";
        }
    }

    public static PlayerData from(@NotNull UUID uuid, @NotNull List<String> list) {
        return new PlayerData(uuid, list);
    }

    public static PlayerData from(Player player) {
        return new PlayerData(player.getUniqueId(), new ArrayList<String>());
    }

}
