package com.semivanilla.fasttravel.model;

import de.leonhard.storage.sections.FlatFileSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private UUID playerID;
    private List<String> visitedPoints;

    public PlayerData(UUID playerID, List<String> visitedPoints) {
        this.playerID = playerID;
        this.visitedPoints = visitedPoints;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public List<String> getVisitedPoints() {
        return visitedPoints;
    }

    public static PlayerData from(FlatFileSection section) {
        return new PlayerData(UUID.fromString(section.getPathPrefix()), section.getStringList(section.getPathPrefix()));
    }

    public static PlayerData from(Player player) {
        return new PlayerData(player.getUniqueId(), new ArrayList<String>());
    }

}
