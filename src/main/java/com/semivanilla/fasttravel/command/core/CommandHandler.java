package com.semivanilla.fasttravel.command.core;

import com.semivanilla.fasttravel.FastTravel;
import com.semivanilla.fasttravel.command.FastTravelCommands;
import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import me.mattstudios.mf.base.CommandManager;

import java.util.ArrayList;

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
        //Register TabCompleters
        commandManager.getCompletionHandler().register("#activepoints",input -> {
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
