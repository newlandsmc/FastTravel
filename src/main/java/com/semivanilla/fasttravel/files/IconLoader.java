package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IconLoader extends AbstractFile {

    public static final String ICON_FOLDER_NAME = "icons";
    private final List<File> iconPacks;
    private final File iconFolder;

    public IconLoader(FileHandler handler) {
        super(handler);
        this.iconPacks = new ArrayList<File>();
        this.iconFolder = new File(handler.getPlugin().getDataFolder() + File.separator + ICON_FOLDER_NAME);

    }

    @Override
    public boolean initConfig() {
        if (!iconFolder.exists())
            iconFolder.mkdirs();
        final boolean defaultCreated = saveResourceTo(getResourceAsInputStream("default.png"), ICON_FOLDER_NAME, "default.png");
        if (!defaultCreated)
            handler.getPlugin().getLogger().severe("Unable to save the default icon to the directory! The plugin will be disabled");
        return defaultCreated;
    }

    @Override
    public void loadConfig() {
        final File[] imageList = iconFolder.listFiles(
                new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().endsWith(".png");
                    }
                }
        );

        iconPacks.clear();
        iconPacks.addAll(Arrays.stream(imageList != null ? imageList : new File[0]).toList());
    }

    public List<File> getIconPacks() {
        return iconPacks;
    }

    public Iterator<File> getIconPackIterator() {
        return new ArrayList<File>(iconPacks).iterator();
    }

    public File getIconFolder() {
        return iconFolder;
    }

}

