package com.semivanilla.fasttravel.command;

import com.semivanilla.fasttravel.command.core.CommandHandler;
import com.semivanilla.fasttravel.command.core.CommandResponse;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import me.mattstudios.mf.annotations.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("fasttravel")
@Alias({"warp","waypoint"})
public class FastTravelCommands {

    private final CommandHandler handler;

    public FastTravelCommands(CommandHandler handler) {
        this.handler = handler;
    }

    @Default
    @Permission("fasttravel.use")
    public void onDefault(final Player player){
        player.sendMessage("Open GUI");
    }

    @SubCommand("create")
    @Permission("fasttravel.create")
    public void onCommandCreate(final Player player, String name){
        if(name == null){
            MiniMessageUtils.sendMessage(player, CommandResponse.WAYPOINT_COMMAND_NO_NAME_PROVIDED.getResponse());
            return;
        }

        if(handler.getPlugin().getWaypointManager().contains(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.WAYPOINT_WITH_NAME_ALREADY_EXISTS.getResponse());
            return;
        }

        if(handler.getPlugin().getWaypointManager().getIfInsideWaypoint(player.getLocation()).isPresent()){
            MiniMessageUtils.sendMessage(player,CommandResponse.WAYPOINT_LOCATION_ALREADY_OVERLAPS.getResponse());
            return;
        }

        handler.getPlugin().getWaypointManager().createNewWaypoint(player.getLocation(),name);
        MiniMessageUtils.sendMessage(player,CommandResponse.RAW_WAYPOINT_CREATED.getResponse());
    }

    @SubCommand("remove")
    @Permission("fasttravel.remove")
    public void onCommandRemove(final Player player, String name){
        if(name == null){
            MiniMessageUtils.sendMessage(player,CommandResponse.WAYPOINT_COMMAND_NO_NAME_PROVIDED.getResponse());
            return;
        }

        if(!handler.getPlugin().getWaypointManager().contains(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            return;
        }

        handler.getPlugin().getWaypointManager().removeWaypoint(name);
        MiniMessageUtils.sendMessage(player,CommandResponse.REMOVED_WAYPOINT.getResponse());
    }

    @SubCommand("setactive")
    @Permission("fasttravel.active")
    public void onCommandSetActive(final CommandSender player, String name, Boolean status){
        if(name == null){
            MiniMessageUtils.sendMessage(player,CommandResponse.WAYPOINT_COMMAND_NO_NAME_PROVIDED.getResponse());
            return;
        }

        if(status == null){
            MiniMessageUtils.sendMessage(player,CommandResponse.INVALID_ARGS.getResponse());
            return;
        }

        if(status == true && handler.getPlugin().getWaypointManager().isWaypointActive(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.ALREADY_ACTIVE.getResponse());
            return;
        }

        if(status == false && !handler.getPlugin().getWaypointManager().isWaypointActive(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.ALREADY_INACTIVE.getResponse());
            return;
        }

        if(!handler.getPlugin().getWaypointManager().contains(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            return;
        }

        handler.getPlugin().getWaypointManager().setActiveFor(name,status);
    }

    @SubCommand("info")
    @Permission("fasttravel.info")
    public void onCommandInfo(final Player player, String name){
        if(name == null){
            MiniMessageUtils.sendMessage(player,CommandResponse.WAYPOINT_COMMAND_NO_NAME_PROVIDED.getResponse());
            return;
        }

        if(!handler.getPlugin().getWaypointManager().contains(name)){
            MiniMessageUtils.sendMessage(player,CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            return;
        }

        player.sendMessage("TODO");
        //TODO
    }



}


