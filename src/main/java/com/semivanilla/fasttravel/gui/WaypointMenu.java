package com.semivanilla.fasttravel.gui;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
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
                        .rows(plugin.getFileHandler().getConfiguration().getRowSize() + 1)
                        .create();

                final int rowSize = plugin.getFileHandler().getConfiguration().getRowSize();

                gui.setItem(rowSize + 1, 1, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 2, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 3, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 4, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 6, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 7, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 8, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 9, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).asGuiItem());
                gui.setItem(rowSize + 1, 5, ItemBuilder.from(plugin.getFileHandler().getConfiguration().getAdditionalButtonMaterial()).name(MiniMessageUtils.transform(plugin.getFileHandler().getConfiguration().getAdditionalButtonName())).lore(MiniMessageUtils.transform(plugin.getFileHandler().getConfiguration().getAdditionButtonLore())).asGuiItem(event -> {
                    player.performCommand(plugin.getFileHandler().getConfiguration().getAdditionButtonCommand());
                }));

                plugin.getWaypointManager().getAllActiveWaypointIterator().forEachRemaining((waypoint -> {
                    if (waypoint.getRow() == 0 || waypoint.getColumn() == 0)
                        return;

                    GuiItem item = null;

                    if (plugin.getPlayerManager().isUnlocked(player.getUniqueId(), waypoint.getName())) {
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
