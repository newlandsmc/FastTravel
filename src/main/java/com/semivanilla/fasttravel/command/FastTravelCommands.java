package com.semivanilla.fasttravel.command;

import com.semivanilla.fasttravel.command.core.CommandHandler;
import com.semivanilla.fasttravel.command.core.CommandResponse;
import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("fasttravel")
@Alias({"warp","waypoint"})
public class FastTravelCommands extends CommandBase {

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

        if(handler.getPlugin().getWaypointManager().contains(name) || handler.getPlugin().getFileHandler().getWaypointConfiguration().contains(name)){
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
    @Completion({"#activepoints"})
    public void onCommandRemove(final CommandSender player, String name) {
        if (name == null) {
            MiniMessageUtils.sendMessage(player, CommandResponse.WAYPOINT_COMMAND_NO_NAME_PROVIDED.getResponse());
            return;
        }

        if (!handler.getPlugin().getWaypointManager().contains(name)) {
            MiniMessageUtils.sendMessage(player, CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            return;
        }

        handler.getPlugin().getWaypointManager().removeWaypoint(name);
        MiniMessageUtils.sendMessage(player,CommandResponse.REMOVED_WAYPOINT.getResponse());
    }

    @SubCommand("setactive")
    @Permission("fasttravel.active")
    @Completion({"#activepoints","#boolean"})
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
        if(status)
            MiniMessageUtils.sendMessage(player,CommandResponse.STATUS_TOGGLE_ENABLED.getResponse());
        else MiniMessageUtils.sendMessage(player,CommandResponse.STATUS_TOGGLED_DISABLED.getResponse());
    }

    @SubCommand("info")
    @Permission("fasttravel.info")
    @Completion({"#allpoints"})
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

    @SubCommand("reload")
    @Permission("fasttravel.reload")
    @Completion({"#activepoints"})
    public void onCommandReload(final CommandSender sender, @Optional String name){
        if(name == null){
            handler.getPlugin().handleReload();
            MiniMessageUtils.sendMessage(sender, CommandResponse.PLUGIN_RELOADED.getResponse());
        } else {
            if (handler.getPlugin().getFileHandler().getWaypointConfiguration().contains(name)) {
                if (handler.getPlugin().getWaypointManager().updateToPluginCache(name)) {
                    MiniMessageUtils.sendMessage(sender, CommandResponse.WAYPOINT_UPDATED.getResponse().replace("%name%", name));
                } else {
                    MiniMessageUtils.sendMessage(sender, CommandResponse.WAYPOINT_UPDATE_FAILED.getResponse());
                }
            } else {
                MiniMessageUtils.sendMessage(sender, CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            }
        }
    }

    @SubCommand("tp")
    @Permission("fasttravel.tp")
    @Completion({"#activepoints", "#players"})
    public void onTeleportCommand(final Player player, final String waypointName, @Optional Player anotherPlayer) {
        if (waypointName == null) {
            handler.getPlugin().handleReload();
            MiniMessageUtils.sendMessage(player, CommandResponse.PLUGIN_RELOADED.getResponse());
            return;
        }

        if (!handler.getPlugin().getWaypointManager().contains(waypointName)) {
            MiniMessageUtils.sendMessage(player, CommandResponse.NO_WAYPOINT_WITH_NAME_EXISTS.getResponse());
            return;
        }

        final Waypoint waypoint = handler.getPlugin().getWaypointManager().getWaypoint(waypointName);

        if (anotherPlayer != null) {
            if (player.hasPermission("fasttravel.tp.others")) {
                anotherPlayer.teleport(waypoint.getWaypoint());
                MiniMessageUtils.sendMessage(anotherPlayer, CommandResponse.TELEPORTED_TO_WAYPOINT_BY_OTHER.getResponse().replace("%name%", player.getName()));
            } else {
                MiniMessageUtils.sendMessage(player, CommandResponse.NO_PERMISSION.getResponse());
            }
        } else {
            //TODO remove after testing
            //player.teleport(waypoint.getWaypoint());
            handler.getPlugin().getPlayerManager().preparePlayerTransport(player, waypoint);
            MiniMessageUtils.sendMessage(player, CommandResponse.TELEPORTED_TO_WAYPOINT.getResponse());
        }

    }

}


