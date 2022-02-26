package com.semivanilla.fasttravel.hook.squaremap;

import com.semivanilla.fasttravel.hook.HookManager;
import com.semivanilla.fasttravel.hook.squaremap.world.WorldHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import java.util.HashMap;

public class Squaremap {

    private final HookManager manager;
    private final ImageRegister imgRegister;
    private final HashMap<String, WorldHandler> worldProvider;

    public Squaremap(HookManager manager) {
        this.manager = manager;
        this.imgRegister = new ImageRegister(this);
        this.worldProvider = new HashMap<>();
    }

    public boolean registerSquareMap() {
        SquaremapProvider.get().mapWorlds().forEach((world) -> {
            final World bWorld = BukkitAdapter.bukkitWorld(world);
            worldProvider.put(bWorld.getName(), new WorldHandler(this, world).registerLayer());
        });
        return true;
    }

    public void loadImages() {
        imgRegister.registerAndPopulateMap();
    }

    public void registerImagesOnMap() {
        manager.getPlugin().getWaypointManager().getAllActiveWaypoints().iterator().forEachRemaining((waypoint -> {
            System.out.println(worldProvider.containsKey(waypoint.getWaypoint().getWorld().getName()));
            if (worldProvider.containsKey(waypoint.getWaypoint().getWorld().getName())) {
                final WorldHandler worldHandler = worldProvider.get(waypoint.getWaypoint().getWorld().getName());
                worldHandler.showIcon(waypoint.getName(), waypoint.getWaypoint(), waypoint.getIconName());
            }
        }));
    }

    public void addIcon(@NotNull String name, @NotNull Location location, @NotNull String iconName) {
        final String worldName = location.getWorld().getName();
        if (worldProvider.containsKey(worldName)) {
            final WorldHandler handler = worldProvider.get(worldName);
            handler.showIcon(name, location, iconName);
        }
    }

    public void removeIcon(@NotNull String name, String worldName) {
        if (worldProvider.containsKey(worldName)) {
            final WorldHandler handler = worldProvider.get(worldName);
            handler.removeIcon(name);
        }
    }

    public void clearMarkers() {
        worldProvider.forEach((name, world) -> {
            world.clearMarkers();
        });
    }

    public HookManager getManager() {
        return manager;
    }

    public ImageRegister getImgRegister() {
        return imgRegister;
    }
}
