package com.semivanilla.fasttravel.data.storage;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.data.DataImpl;
import com.semivanilla.fasttravel.model.PlayerData;
import de.leonhard.storage.Json;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class JsonStorage implements DataImpl {

    private final FastTravel plugin;
    private Json storageCache;

    public JsonStorage(FastTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean initStorage() {
        storageCache = plugin.getUtilityManager().getFileUtils().createJson("player-data.json");
        return storageCache != null;
    }

    @Override
    public CompletableFuture<Optional<PlayerData>> getIfPresent(@NotNull UUID playerUUID) {
        return CompletableFuture.supplyAsync(new Supplier<Optional<PlayerData>>() {
            @Override
            public Optional<PlayerData> get() {
                if (storageCache.contains(playerUUID.toString())) {
                    return Optional.of(PlayerData.from(storageCache.getSection(playerUUID.toString())));
                } else return Optional.empty();
            }
        });
    }

    @Override
    public void register(@NotNull UUID uuid) {
        storageCache.set(uuid.toString(), new ArrayList<String>());
    }

    @Override
    public void remove(@NotNull UUID uuid) {
        storageCache.remove(uuid.toString());
    }

    @Override
    public void update(@NotNull PlayerData data) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                storageCache.set(data.playerID().toString(), data.visitedPoints());
            }
        });
    }
}
