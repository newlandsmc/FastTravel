package com.semivanilla.fasttravel.files;

import com.semivanilla.fasttravel.files.core.AbstractFile;
import com.semivanilla.fasttravel.files.core.FileHandler;

public final class Configuration extends AbstractFile {

    private int teleportationDelay;

    public Configuration(FileHandler handler) {
        super(handler);
    }


    @Override
    public boolean initConfig() {
        this.file = handler.getPlugin().getUtilityManager().getFileUtils().createConfiguration();
        return file != null;
    }

    @Override
    public void loadConfig() {
        this.teleportationDelay = this.file.getInt("teleportation-delay-in-sec");
    }

    public int getTeleportationDelay() {
        return teleportationDelay;
    }
}
