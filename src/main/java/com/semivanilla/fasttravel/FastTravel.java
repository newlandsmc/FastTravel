package com.semivanilla.fasttravel;

import com.semivanilla.fasttravel.command.core.CommandHandler;
import com.semivanilla.fasttravel.data.DataImpl;
import com.semivanilla.fasttravel.data.storage.JsonStorage;
import com.semivanilla.fasttravel.files.core.FileHandler;
import com.semivanilla.fasttravel.hook.HookManager;
import com.semivanilla.fasttravel.listener.PlayerJoinListener;
import com.semivanilla.fasttravel.listener.PlayerLeaveListener;
import com.semivanilla.fasttravel.listener.PlayerMovementListener;
import com.semivanilla.fasttravel.listener.PlayerToggleSneakListener;
import com.semivanilla.fasttravel.manager.PlayerManager;
import com.semivanilla.fasttravel.manager.WaypointManager;
import com.semivanilla.fasttravel.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FastTravel extends JavaPlugin {

    private UtilityManager utilityManager;
    private FileHandler fileHandler;
    private WaypointManager waypointManager;
    private PlayerManager playerManager;
    private CommandHandler commandHandler;
    private HookManager hookManager;
    private DataImpl dataStorage;

    @Override
    public void onEnable() {
        utilityManager = new UtilityManager(this);
        fileHandler = new FileHandler(this);
        waypointManager = new WaypointManager(this);
        commandHandler = new CommandHandler(this);
        hookManager = new HookManager(this);
        dataStorage = new JsonStorage(this);
        playerManager = new PlayerManager(this);

        if (!fileHandler.createConfigurationFiles()) {
            getLogger().severe("The plugin is unable to create/fetch file configuration, Disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        fileHandler.loadAllConfigs();

        if (!dataStorage.initStorage()) {
            getLogger().severe("The plugin is unable to establish a connection to the storage interface, Disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(this), this);

        commandHandler.registerOthers();
        commandHandler.registerCommands();

        hookManager.initHooks();


        Test test = new Test(this);
        getCommand("test").setExecutor(test);

        getServer().getPluginManager().registerEvents(test, this);
    }

    public void handleReload() {
        if (!fileHandler.createConfigurationFiles()) {
            getLogger().severe("The plugin is unable to create/fetch file configuration, Disabling the plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        waypointManager.prepareWaypointReload();
        fileHandler.reloadAllConfiguration();
        hookManager.reloadHooks();
    }


    @Override
    public void onDisable() {
        hookManager.clearIcons();
    }

    public UtilityManager getUtilityManager() {
        return utilityManager;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public HookManager getHookManager() {
        return hookManager;
    }

    public WaypointManager getWaypointManager() {
        return waypointManager;
    }

    public DataImpl getDataStorage() {
        return dataStorage;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
