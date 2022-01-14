package com.semivanilla.fasttravel.command.core;

public enum CommandResponse {
    WAYPOINT_COMMAND_NO_NAME_PROVIDED("<red>You haven't provided a name for the waypoint."),
    INVALID_ARGS("<red>Invalid args provided"),
    ALREADY_ACTIVE("<red>The waypoint is already active"),
    ALREADY_INACTIVE("<red>The waypoint is already inactive"),
    WAYPOINT_WITH_NAME_ALREADY_EXISTS("<red>A Waypoint with this name already exists!"),
    WAYPOINT_LOCATION_ALREADY_OVERLAPS("<red>There is already a waypoint within this radius. Please select a unique location"),
    RAW_WAYPOINT_CREATED("<green>A raw waypoint config has been created in the config. Please configure it and reload the plugin"),
    NO_WAYPOINT_WITH_NAME_EXISTS("<red>No waypoint with this name has been found!"),
    REMOVED_WAYPOINT("<green>Waypoint has been removed from the server"),
    ;

    private String response;

    CommandResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
