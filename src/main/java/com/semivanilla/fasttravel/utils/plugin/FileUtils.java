package com.semivanilla.fasttravel.utils.plugin;

import com.semivanilla.fasttravel.utils.UtilityManager;
import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

public class FileUtils {

    private final UtilityManager manager;

    public FileUtils(UtilityManager manager) {
        this.manager = manager;
    }

    public InputStream getResourceAsStream(@NotNull String name){
        return manager.getPlugin().getResource(name);
    }

    public FileUtils generateParentFolder(){
        if(manager.getPlugin().getDataFolder().exists())
            return this;
        manager.getPlugin().getDataFolder().mkdirs();
        return this;
    }

    public Config createConfiguration(){
        generateParentFolder();
        File configFile = new File(manager.getPlugin().getDataFolder(), "config.yml");
        Config config = LightningBuilder
                .fromFile(configFile)
                .addInputStream(this.getResourceAsStream("config.yml"))
                .setDataType(DataType.SORTED)
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.MANUALLY)
                .createConfig();
        config.set("version",manager.getPlugin().getDescription().getVersion());
        return config;
    }

    public Json createJson(@NotNull String fileName){
        return new Json(fileName,manager.getPlugin().getDataFolder().getPath()+ File.separator+"data");
    }

    public Yaml createYamlFile(@NotNull String fileName){
        return new Yaml(fileName,manager.getPlugin().getDataFolder().getPath()+ File.separator+"data");
    }

    public void executeAsyncIfExists(@NotNull String fileName, Consumer<File> toPerform){
        File file = new File(manager.getPlugin().getDataFolder(),fileName);
        if(file.exists()){
            manager.getPlugin().getServer().getScheduler().runTaskAsynchronously(manager.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    toPerform.accept(file);
                }
            });
        }
    }

    public void executeIfExists(@NotNull String fileName, Consumer<File> toPerform){
        File file = new File(manager.getPlugin().getDataFolder(),fileName);
        if(file.exists()){
            toPerform.accept(file);
        }
    }
}