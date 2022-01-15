package com.semivanilla.fasttravel.hook.squaremap;

import org.jetbrains.annotations.NotNull;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class ImageRegister {

    private final Squaremap hook;
    private final HashMap<String, Key> imageDirectory;

    public ImageRegister(Squaremap hook) {
        this.hook = hook;
        this.imageDirectory = new HashMap<String, Key>();
    }

    private Key of(@NotNull String name) {
        return Key.key(name);
    }

    public boolean containsImage(@NotNull String name) {
        return imageDirectory.containsKey(name);
    }

    public void registerAndPopulateMap() {
        hook.getManager().getPlugin().getFileHandler().getIconLoader().getIconPackIterator().forEachRemaining((iconFile) -> {
            final String iconName = iconFile.getName();
            try {
                final BufferedImage iconImage = ImageIO.read(iconFile);
                final Key iconKey = of(iconName);

                SquaremapProvider.get().iconRegistry().register(iconKey, iconImage);
                imageDirectory.put(iconName, iconKey);
            } catch (IOException e) {
                e.printStackTrace();
                hook.getManager().getPlugin().getLogger().severe("Unable to buffer up the image " + iconName + ". This icon will be skipped!");

            }
        });
        hook.getManager().getPlugin().getLogger().info("Successfully registered " + imageDirectory.size() + " image from squaremap");
    }

    public void unregisterAllImages() {
        final int size = imageDirectory.size();
        imageDirectory.forEach((n, key) -> {
            SquaremapProvider.get().iconRegistry().unregister(key);
        });
        hook.getManager().getPlugin().getLogger().info("Successfully unregistered " + size + " image from squaremap");
    }

    public Optional<Key> getImageKey(@NotNull String name) {
        return Optional.ofNullable(imageDirectory.get(name));
    }


}
