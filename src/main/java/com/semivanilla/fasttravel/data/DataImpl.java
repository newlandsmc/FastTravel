package com.semivanilla.fasttravel.data;

import com.semivanilla.fasttravel.model.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataImpl {

    boolean initStorage();

    CompletableFuture<Optional<PlayerData>> getIfPresent(@NotNull UUID playerUUID);

    void register(@NotNull UUID uuid);

    void remove(@NotNull UUID uuid);

    void update(@NotNull PlayerData data);


}
