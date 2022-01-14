package com.semivanilla.fasttravel;

import com.semivanilla.fasttravel.command.core.CommandHandler;
import com.semivanilla.fasttravel.files.core.FileHandler;
import com.semivanilla.fasttravel.manager.WaypointManager;
import com.semivanilla.fasttravel.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FastTravel extends JavaPlugin {

    private UtilityManager utilityManager;
    private FileHandler fileHandler;
    private WaypointManager waypointManager;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        utilityManager = new UtilityManager(this);
        fileHandler = new FileHandler(this);
        waypointManager = new WaypointManager(this);
        commandHandler = new CommandHandler(this);

        if (!fileHandler.createConfigurationFiles()){
            getLogger().severe("The plugin is unable to create/fetch file configuration, Disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        fileHandler.loadAllConfigs();

        commandHandler.registerOthers();
        commandHandler.registerCommands();

        Test test = new Test(this);
        getCommand("test").setExecutor(test);
        getServer().getPluginManager().registerEvents(test, this);
    }

    public void handleReload(){
        if (!fileHandler.createConfigurationFiles()){
            getLogger().severe("The plugin is unable to create/fetch file configuration, Disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        waypointManager.prepareWaypointReload();
        fileHandler.loadAllConfigs();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public UtilityManager getUtilityManager() {
        return utilityManager;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }



    public WaypointManager getWaypointManager() {
        return waypointManager;
    }
}
