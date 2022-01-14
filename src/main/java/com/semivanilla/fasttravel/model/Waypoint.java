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

    public Waypoint(String name, ItemStack icon, List<String> lore, Location waypoint, int offsetRadiusX, int offsetRadiusY, int offsetRadiusZ) {
        this.name = name;
        this.icon = icon;
        this.lore = lore;
        this.waypoint = waypoint;

        final Location c1 = waypoint.clone();
        final Location c2 = waypoint.clone();

        c1.setX(c1.getX() + offsetRadiusX);
        c2.setX(c2.getZ() + offsetRadiusX);

        c1.setY(c1.getY() + offsetRadiusY);
        c2.setY(c2.getY() - offsetRadiusY);

        c1.setZ(c1.getZ() + offsetRadiusZ);
        c2.setZ(c2.getZ() - offsetRadiusZ);

        this.radius = BoundingBox.of(c1,c2);
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

    public static Map<String,Object> serializeRawWaypoint(@NotNull String name, @NotNull Location location){
        HashMap<String,Object> map = new HashMap<>();
        map.put("location",LocationUtils.serializeLocation(location));
        map.put("icon", Material.GRASS_BLOCK.name());
        map.put("lore",new ArrayList<String>(){{
            add("<white>Coming soon!");
        }});
        map.put("offset-radius.x",5);
        map.put("offset-radius.y",5);
        map.put("offset-radius.z",5);
        return map;
    }

    public static Waypoint buildFrom(@NotNull String name,@NotNull ItemStack icon, @NotNull List<String> lore, @NotNull Location location, int offsetX, int offsetY, int offsetZ ){
        return new Waypoint(name, icon, lore, location, offsetX, offsetY, offsetZ);
    }
}
