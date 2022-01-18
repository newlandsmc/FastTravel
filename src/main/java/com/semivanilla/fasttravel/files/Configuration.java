package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class Configuration extends AbstractFile {

    private int teleportationDelay;
    private int coolDownForTeleportation;

    //Messages
    private String onQueue, teleportationCommencing, teleportCancelledOnMove, teleportCancelledForNewRequest, newWaypointDiscovered, waypointLocked, inCooldownMessage;
    private String errorMessageNoPerm, errorMessageUnknownCommand;

    private int iconSize;

    //GUI Configuration
    private String menuName;
    private int rowSize;
    //Gui Buttons
    private String unlockedButtonName, lockedButtonName, additionalButtonName;
    private List<String> unlockedButtonLore, lockedButtonLore, additionButtonLore;

    private String additionButtonCommand;
    private ItemStack additionalButtonMaterial;
    private boolean additionalButtonEnabled;

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
        this.iconSize = this.file.getInt("square-map-icon-size");

        this.file.setPathPrefix("messages");
        this.onQueue = this.file.getString("on-teleport-queue");
        this.teleportationCommencing = this.file.getString("teleport-commencing");
        this.teleportCancelledOnMove = this.file.getString("teleport-cancelled.on-move");
        this.teleportCancelledForNewRequest = this.file.getString("teleport-cancelled.on-new-request");
        this.newWaypointDiscovered = this.file.getString("waypoint-discovered");
        this.waypointLocked = this.file.getString("waypoint-locked");
        this.inCooldownMessage = this.file.getString("player-in-cooldown");

        this.errorMessageNoPerm = this.file.getString("no-permission");
        this.errorMessageUnknownCommand = this.file.getString("unknown-command");
        this.file.setPathPrefix(null);

        this.file.setPathPrefix("gui");
        this.menuName = this.file.getString("name");
        this.rowSize = this.file.getInt("rows");
        this.unlockedButtonName = this.file.getString("buttons.unlocked.name");
        this.unlockedButtonLore = this.file.getStringList("buttons.unlocked.lore-to-add-at-last");
        this.lockedButtonName = this.file.getString("buttons.locked.name");
        this.lockedButtonLore = this.file.getStringList("buttons.locked.lore-to-add-at-last");
        this.additionalButtonMaterial = getMaterial("buttons.additional.item");
        this.additionalButtonEnabled = this.file.getBoolean("buttons.additional.enabled");
        this.additionalButtonName = this.file.getString("buttons.additional.name");
        this.additionButtonLore = this.file.getStringList("buttons.additional.lore");
        this.additionButtonCommand = this.file.getString("buttons.additional.command");
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

    public String getErrorMessageNoPerm() {
        return errorMessageNoPerm;
    }

    public String getErrorMessageUnknownCommand() {
        return errorMessageUnknownCommand;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getRowSize() {
        return rowSize;
    }

    public String getUnlockedButtonName(String p1) {
        return unlockedButtonName.replace("%name%", p1);
    }

    public String getLockedButtonName(String p1) {
        return lockedButtonName.replace("%name%", p1);
    }

    public String getAdditionalButtonName() {
        return additionalButtonName;
    }

    public List<String> getUnlockedButtonLore() {
        return unlockedButtonLore;
    }

    public List<String> getLockedButtonLore(int x, int z) {
        return lockedButtonLore.stream().map(w -> w.replace("%x%", String.valueOf(x)).replace("%z%", String.valueOf(z))).toList();
    }

    public List<String> getAdditionButtonLore() {
        return additionButtonLore;
    }

    public String getAdditionButtonCommand() {
        return additionButtonCommand;
    }

    public ItemStack getAdditionalButtonMaterial() {
        return additionalButtonMaterial;
    }

    public boolean isAdditionalButtonEnabled() {
        return additionalButtonEnabled;
    }

    public int getIconSize() {
        return iconSize;
    }
}
