package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;

public final class Configuration extends AbstractFile {

    public Configuration(FileHandler handler) {
        super(handler);
    }

    private int offsetX,offsetY,offsetZ;

    @Override
    public boolean initConfig() {
        this.file = handler.getPlugin().getUtilityManager().getFileUtils().createConfiguration();
        return file != null;
    }

    @Override
    public void loadConfig() {
        this.offsetX = file.getInt("waypoint-radius.offset-x");
        this.offsetY = file.getInt("waypoint-radius.offset-y");
        this.offsetZ = file.getInt("waypoint-radius.offset-z");
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }
}
