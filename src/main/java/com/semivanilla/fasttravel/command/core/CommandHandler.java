package com.semivanilla.fasttravel.command.core;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.command.FastTravelCommands;
import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import me.mattstudios.mf.base.CommandManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class CommandHandler {

    private final FastTravel plugin;
    private final CommandManager commandManager;
    private final static ArrayList<String> booleanCompletion = new ArrayList<String>() {{
        add("true");
        add("false");
    }};

    public static final HashMap<String, String> commandDescriptionMapping = new HashMap<>() {{
        put("/fasttravel", "Open up a GUI of active waypoints.");
        put("/fasttravel create [name]", "Creates a raw waypoint to config. Configure it and reload the plugin for activating it.");
        put("/fasttravel remove [name]", "Removes a waypoint from the plugin.");
        put("/fasttravel setactive [name] true", "Set the status of a waypoint to active.");
        put("/fasttravel setactive [name] false", "Set the status of a waypoint to not active.");
        put("/fasttravel reload", "Reloads the plugin. Reload this if you added new icons to the folder.");
        put("/fasttravel reload [name]", "Reloads the specified waypoint only.");
        put("/fasttravel tp [name]", "Teleports you to the specified waypoint");
        put("/fasttravel tp [name] [player]", "Teleports the specified player to the specified waypoint");
    }};

    public CommandHandler(FastTravel plugin) {
        this.plugin = plugin;
        commandManager = new CommandManager(plugin, true);
    }

    public void registerOthers() {
        //Register TabCompletes
        commandManager.getCompletionHandler().register("#activepoints", input -> {
            return plugin.getWaypointManager().getAllWaypoints().stream().map(Waypoint::getName).toList();
        });

        commandManager.getCompletionHandler().register("#allpoints", input -> {
            return plugin.getWaypointManager().getAllWaypoints().stream().filter(Waypoint::isActive).map(Waypoint::getName).toList();
        });

        commandManager.getCompletionHandler().register("#boolean", input -> {
            return booleanCompletion;
        });

        //Register Default Messages
        commandManager.getMessageHandler().register("cmd.no.permission", sender -> {
            MiniMessageUtils.sendMessage(sender, plugin.getFileHandler().getConfiguration().getErrorMessageNoPerm());
        });

        commandManager.getMessageHandler().register("cmd.wrong.usage", sender -> {
            MiniMessageUtils.sendMessage(sender, plugin.getFileHandler().getConfiguration().getErrorMessageUnknownCommand());
        });

        commandManager.getMessageHandler().register("cmd.no.exists", sender -> {
            MiniMessageUtils.sendMessage(sender, plugin.getFileHandler().getConfiguration().getErrorMessageUnknownCommand());
        });

        commandManager.hideTabComplete(true);
    }

    public void registerCommands(){
        commandManager.register(
                new FastTravelCommands(this)
        );
    }

    public FastTravel getPlugin() {
        return plugin;
    }
}
