package com.semivanilla.fasttravel.command.core;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.command.FastTravelCommands;
import com.semivanilla.fasttravel.model.Waypoint;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class CommandHandler {

    private final FastTravel plugin;
    private final CommandManager commandManager;
    private final static ArrayList<String> booleanCompletion = new ArrayList<String>(){{
        add("true");
        add("false");
    }};

    public CommandHandler(FastTravel plugin) {
        this.plugin = plugin;
        commandManager = new CommandManager(plugin);
    }

    public void registerOthers(){
        commandManager.getCompletionHandler().register("#activepoints",input -> {
            return plugin.getWaypointManager().getAllWaypoints().stream().map(Waypoint::getName).toList();
        });

        commandManager.getCompletionHandler().register("#allpoints",input -> {
            return plugin.getWaypointManager().getAllWaypoints().stream().filter(Waypoint::isActive).map(Waypoint::getName).toList();
        });

        commandManager.getCompletionHandler().register("#boolean",input -> {
            return booleanCompletion;
        });


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
