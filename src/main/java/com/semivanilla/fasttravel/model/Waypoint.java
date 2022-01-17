package com.semivanilla.fasttravel.model;

import com.semivanilla.fasttravel.utils.plugin.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Waypoint {

    private final String name;
    private final ItemStack icon;
    private final List<String> lore;
    private final Location waypoint;
    public static final String DEFAULT_ICON = "default.png";
    private final BoundingBox radius;
    public static final int DEFAULT_GUI_CODE = 0;

    private boolean active;
    private static final List<String> DEFAULT_LORE = new ArrayList<String>();

    static {
        DEFAULT_LORE.add("<white>Coming soon!");
    }

    private static final int DEFAULT_OFFSET_X = 5;
    private static final int DEFAULT_OFFSET_Y = 5;
    private static final int DEFAULT_OFFSET_Z = 5;

    private final String iconName;
    private final int row, column;

    public Waypoint(String name, ItemStack icon, List<String> lore, Location waypoint, int offsetRadiusX, int offsetRadiusY, int offsetRadiusZ, String iconName, int row, int column) {
        this.name = name;
        this.icon = icon;
        this.lore = lore;
        this.waypoint = waypoint;

        final Location c1 = waypoint.clone();
        final Location c2 = waypoint.clone();

        c1.setX(c1.getX() + offsetRadiusX);
        c2.setX(c2.getX() - offsetRadiusX);

        c1.setY(c1.getY() + offsetRadiusY);
        c2.setY(c2.getY() - offsetRadiusY);

        c1.setZ(c1.getZ() + offsetRadiusZ);
        c2.setZ(c2.getZ() - offsetRadiusZ);

        this.row = row;
        this.column = column;
        this.radius = BoundingBox.of(c1, c2);
        this.active = true;
        this.iconName = iconName;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<String> getLore() {
        return lore;
    }

    public Location getWaypoint() {
        return waypoint;
    }

    public BoundingBox getRadius() {
        return radius;
    }

    public int getWaypointX(){
        return waypoint.getBlockX();
    }

    public int getWaypointY(){
        return waypoint.getBlockY();
    }

    public int getWaypointZ(){
        return waypoint.getBlockZ();
    }

    public boolean isInside(@NotNull Location location){
        return radius.contains(location.getX(),location.getY(),location.getZ());
    }

    public static Map<String,Object> serializeRawWaypoint(@NotNull Location location){
        final HashMap<String, Object> map = new HashMap<>();
        final HashMap<String, Object> offsetRadius = new HashMap<>();
        final HashMap<String, Object> guiPosition = new HashMap<>();
        map.put("location", LocationUtils.serializeLocation(location));
        map.put("icon", Material.GRASS_BLOCK.name());
        map.put("lore", DEFAULT_LORE.stream().toList());
        map.put("icon-name", "default.png");
        offsetRadius.put("x", DEFAULT_OFFSET_X);
        offsetRadius.put("y", DEFAULT_OFFSET_Y);
        offsetRadius.put("z", DEFAULT_OFFSET_Z);
        guiPosition.put("row", DEFAULT_GUI_CODE);
        guiPosition.put("col", DEFAULT_GUI_CODE);
        map.put("gui", guiPosition);
        map.put("offset-radius", offsetRadius);
        return map;
    }

    public static Waypoint buildFrom(@NotNull String name, @NotNull ItemStack icon, @NotNull List<String> lore, @NotNull Location location, int offsetX, int offsetY, int offsetZ, String iconName, int row, int column) {
        return new Waypoint(name, icon, lore, location, offsetX, offsetY, offsetZ, iconName, row, column);
    }

    public static Waypoint buildFrom(@NotNull String name, @NotNull Location location) {
        return new Waypoint(name
                , new ItemStack(Material.GRASS_BLOCK)
                , DEFAULT_LORE.stream().toList()
                , location
                , DEFAULT_OFFSET_X
                , DEFAULT_OFFSET_Y
                , DEFAULT_OFFSET_Z
                , DEFAULT_ICON
                , DEFAULT_GUI_CODE
                , DEFAULT_GUI_CODE
        );
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getIconName() {
        return iconName;
    }
}
