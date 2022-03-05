package com.semivanilla.fasttravel.gui;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class WaypointMenu {

    private final static Material LOCKED_WARP = Material.GRAY_DYE;
    private final FastTravel plugin;
    private final Player player;

    public WaypointMenu(@NotNull FastTravel plugin, @NotNull Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public WaypointMenu openMenu() {
        prepareGUI().thenAccept(menu -> {
            plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    menu.open(player);
                }
            });
        });
        return this;
    }

    public CompletableFuture<Gui> prepareGUI() {
        return CompletableFuture.supplyAsync(new Supplier<Gui>() {
            @Override
            public Gui get() {

                final Gui gui = Gui.gui()
                        .disableAllInteractions()
                        .title(MiniMessageUtils.transform(plugin.getFileHandler().getConfiguration().getMenuName()))
                        .rows(plugin.getFileHandler().getConfiguration().getRowSize())
                        .create();

                plugin.getFileHandler().getConfiguration().getGuiFillers().forEach((fillerItem) -> {
                    final GuiItem item = fillerItem.asItem();
                    fillerItem.getSlots().forEach(slot -> {
                        gui.setItem(slot, item);
                    });
                });
                plugin.getWaypointManager().getAllActiveWaypointIterator().forEachRemaining((waypoint -> {
                    if (waypoint.getRow() == 0 || waypoint.getColumn() == 0)
                        return;

                    GuiItem item = null;

                    if (waypoint.isDefaultWaypoint() || plugin.getPlayerManager().isUnlocked(player.getUniqueId(), waypoint.getName())) {
                        item = ItemBuilder.from(waypoint.getIcon())
                                .name(MiniMessageUtils.transform(plugin.getFileHandler().getConfiguration().getUnlockedButtonName(waypoint.getName())))
                                .lore(MiniMessageUtils.transform(Stream.concat(waypoint.getLore().stream(), plugin.getFileHandler().getConfiguration().getUnlockedButtonLore().stream()).collect(Collectors.toList())))
                                .asGuiItem(event -> {
                                    gui.close(player);
                                    plugin.getPlayerManager().preparePlayerTransport(player, waypoint);
                                });
                    } else {
                        item = ItemBuilder.from(LOCKED_WARP)
                                .name(MiniMessageUtils.transform(plugin.getFileHandler().getConfiguration().getLockedButtonName(waypoint.getName())))
                                .lore(MiniMessageUtils.transform(Stream.concat(waypoint.getLore().stream(), plugin.getFileHandler().getConfiguration().getLockedButtonLore(waypoint.getWaypointX(), waypoint.getWaypointZ()).stream()).collect(Collectors.toList())))
                                .asGuiItem(event -> {
                                    MiniMessageUtils.sendMessage(player, plugin.getFileHandler().getConfiguration().getWaypointLocked());
                                });
                    }
                    gui.setItem(waypoint.getRow(), waypoint.getColumn(), item);

                }));
                return gui;
            }
        });
    }

}
