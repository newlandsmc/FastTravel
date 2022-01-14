package com.semivanilla.fasttravel.model;

import com.semivanilla.fasttravel.utils.plugin.LocationUtils;
import de.leonhard.storage.sections.FlatFileSection;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Waypoint {

    private final String name;
    private final ItemStack icon;
    private final List<String> lore;
    private final Location waypoint;

    private final BoundingBox radius;
    private boolean active;

    private static final int DEFAULT_OFFSET_X = 5;
    private static final int DEFAULT_OFFSET_Y = 5;
    private static final int DEFAULT_OFFSET_Z = 5;
    private static final List<String> DEFAULT_LORE = new ArrayList<String>(){{
        add("<white>Coming soon!");
    }};

    public Waypoint(String name, ItemStack icon, List<String> lore, Location waypoint, int offsetRadiusX, int offsetRadiusY, int offsetRadiusZ) {
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

        this.radius = BoundingBox.of(c1,c2);
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static Map<String,Object> serializeRawWaypoint(@NotNull Location location){
        final HashMap<String,Object> map = new HashMap<>();
        final HashMap<String, Object> offsetRadius = new HashMap<>();
        map.put("location",LocationUtils.serializeLocation(location));
        map.put("icon", Material.GRASS_BLOCK.name());
        map.put("lore",DEFAULT_LORE.stream().toList());
        offsetRadius.put("x",DEFAULT_OFFSET_X);
        offsetRadius.put("y",DEFAULT_OFFSET_Y);
        offsetRadius.put("z",DEFAULT_OFFSET_Z);
        map.put("offset-radius",offsetRadius);
        return map;
    }


    public static Waypoint buildFrom(@NotNull String name,@NotNull ItemStack icon, @NotNull List<String> lore, @NotNull Location location, int offsetX, int offsetY, int offsetZ ){
        return new Waypoint(name, icon, lore, location, offsetX, offsetY, offsetZ);
    }

    public static Waypoint buildFrom(@NotNull String name, @NotNull Location location){
        return new Waypoint(name
                ,new ItemStack(Material.GRASS_BLOCK)
                ,DEFAULT_LORE.stream().toList()
                ,location
                ,DEFAULT_OFFSET_X
                ,DEFAULT_OFFSET_Y
                ,DEFAULT_OFFSET_Z
        );
    }
}
