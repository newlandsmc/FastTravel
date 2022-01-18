package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;

public final class Configuration extends AbstractFile {

    private int teleportationDelay;
    private int coolDownForTeleportation;

    //Messages
    private String onQueue, teleportationCommencing, teleportCancelledOnMove, teleportCancelledForNewRequest, newWaypointDiscovered, waypointLocked, inCooldownMessage;

    public Configuration(FileHandler handler) {
        super(handler);
    }


    @Override
    public boolean initConfig() {
        this.file = handler.getPlugin().getUtilityManager().getFileUtils().createConfiguration();
        return file != null;
    }

    @Override
    public void loadConfig() {
        this.teleportationDelay = this.file.getInt("teleportation-delay-in-sec");
        this.coolDownForTeleportation = this.file.getInt("cooldown-between-teleportation-in-sec");

        this.file.setPathPrefix("messages");
        this.onQueue = this.file.getString("on-teleport-queue");
        this.teleportationCommencing = this.file.getString("teleport-commencing");
        this.teleportCancelledOnMove = this.file.getString("teleport-cancelled.on-move");
        this.teleportCancelledForNewRequest = this.file.getString("teleport-cancelled.on-new-request");
        this.newWaypointDiscovered = this.file.getString("waypoint-discovered");
        this.waypointLocked = this.file.getString("waypoint-locked");
        this.inCooldownMessage = this.file.getString("player-in-cooldown");
        this.file.setPathPrefix(null);
    }

    public int getTeleportationDelay() {
        return teleportationDelay;
    }

    public int getCoolDownForTeleportation() {
        return coolDownForTeleportation;
    }

    public String getOnQueue() {
        return onQueue;
    }

    public String getTeleportationCommencing() {
        return teleportationCommencing;
    }

    public String getTeleportCancelledOnMove() {
        return teleportCancelledOnMove;
    }

    public String getTeleportCancelledForNewRequest() {
        return teleportCancelledForNewRequest;
    }

    public String getNewWaypointDiscovered() {
        return newWaypointDiscovered;
    }

    public String getWaypointLocked() {
        return waypointLocked;
    }

    public String getInCoolDownMessage() {
        return inCooldownMessage;
    }
}
