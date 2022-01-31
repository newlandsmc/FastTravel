<h1 align="center">Fast Travel</h1>

Fasttravel is a plugin which allows staffs to set locations around the world as "waypoints" Players must visit a
waypoint to unlock it, and then they can teleport there in the future. Uses a GUI for player interface.

## Commands

The aliases for the command is *warp*, *waypoint*

| Commands                                    | Permissions       | Description                                                                             |
|---------------------------------------------|-------------------|-----------------------------------------------------------------------------------------|
| /fasttravel                                 | fasttravel.use    | Opens up a GUI for currently active waypoints                                           |
| /fasttravel create [name]                   | fasttravel.create | Creates a raw waypoint to config. Configure it and reload the plugin for activating it. |
| /fasttravel remove [name]                   | fasttravel.remove | Removes a waypoint from the plugin.                                                     |
| /fasttravel setactive [name] [true]/[false] | fasttravel.active | Set the status of a waypoint to active/inactive.                                        |
| /fasttravel reload                          | fasttravel.reload | Reloads the plugin. Reload this if you added new icons to the folder.                   |
| /fasttravel reload [name]                   | fasttravel.reload | Reloads the specified waypoint only.                                                    |
| /fasttravel tp [name]                       | fasttravel.tp     | Teleports you to the specified waypoint                                                 |
| /fasttravel tp [name] [player]                                            | fasttravel.tp.others                  | Teleports the specified player to the specified waypoint                                                                                        |

## Configuration

This is the plugin configuration. Everything is explained in the comments

```YAML
##When a player click for teleportation, how many sec he should wait before a teleportation commence
teleportation-delay-in-sec: 5

##This cool-down will be placed after a player gets teleported successfully. once active he/she will have to wait for X
##sec to initiate next teleportation
cooldown-between-teleportation-in-sec: 10

square-map-icon-size: 40
##GUI Configuration
#name: Name of the GUI
#rows: no of rows the plugin should have.
##NOTE: It should be less than 5, as the plugin adds a row as filler in bottom .
gui:
 name: "Waypoints"
 rows: 3
 buttons:
  unlocked:
   name: "<green>%name%"
   lore-to-add-at-last:
    - "<green>Click to visit the waypoint"
  locked:
   name: "<red>%name%"
   lore-to-add-at-last:
    - "<red>Locked! - visit %x% %z% to unlock."
  additional:
   enabled: true
   item: "base64:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I1NmU0OTA4NWY1NWQ1ZGUyMTVhZmQyNmZjNGYxYWZlOWMzNDMxM2VmZjk4ZTNlNTgyNDVkZWYwNmU1ODU4YyJ9fX0="
   name: "<gray>Home"
   lore:
    - "<white>Click to teleport back to home"
   command: "home"


messages:
 help:
  header: "<yellow><strikethrough>                         <reset> Waypoint Help <yellow><strikethrough>                         <reset>"
  ##Available Placeholder:
  #%command% -> Parses the command
  #%description% -> Parses the description
  command: "<gold>%command%<white>: %description%"
  footer: "<yellow><strikethrough>                                                                  <reset>"
 on-teleport-queue: "<yellow>Teleporting in 5 seconds, don't move..."
 teleport-commencing: "<yellow>Teleporting..."
 teleport-cancelled:
  on-move: "<red>Pending teleportation request cancelled."
  on-new-request: "<red>Pending teleportation request cancelled in favour for new teleportation request."
 ## Available Placeholder: %waypoint_name% -> Name of the waypoint
 waypoint-discovered: "<green>You discovered %waypoint_name%! Now you can teleport to this waypoint!"
 waypoint-locked: "<red>You must visit this location to unlock this waypoint."
 ## Available Placeholder: %cool-down% -> Cooldown in secs
 player-in-cooldown: "<red>you are in cool-down for %cool-down% secs. Try later!"
 no-permission: "<red>You do not have permission to do that."
 unknown-command: "<red>Unknown command"
```

## Waypoint Configuration

 ```YAML
 mytest: # Name of the waypoint. This name will be used in everywhere including the gui's
  icon-name: default.png #This will be the file name of the icon. If the file does not exists, it will use the default.png
  lore: #Description of the Waypoint
   - <white>Coming soon!
  icon: GRASS_BLOCK #What should be the icon that should be shown if the waypoint is unlocked for a player. You can use custom base64 heads for the same with the format "base64:value" 
  location: #This is a serialized location of the waypoint
   x: -207
   y: 63
   z: 63
   pitch: -12.899692
   world-name: world
   yaw: -86.821434
  gui: #Position of the waypoint that should be in the GUI
   col: 0
   row: 0
  offset-radius: #The radius around the location that need will create the bounding box
   y: 5
   z: 5
 ```

## To Developers

If you wish to add new storage system in the future, implement this package `com.semivanilla.fasttravel.data.DataImpl`

```JAVA
    boolean initStorage();

        CompletableFuture<Optional<PlayerData>>getIfPresent(@NotNull UUID playerUUID);

        void register(@NotNull UUID uuid);

        void remove(@NotNull UUID uuid);

        void update(@NotNull PlayerData data);
```

An example can be found in `com.semivanilla.fasttravel.data.storage.JSONStorage`

## Squaremap Hook

The plugin is hooking with [SquareMap](https://github.com/jpenilla/squaremap) to hook custom png images to the map.
Every image is registered on `com.semivanilla.fasttravel.hook.squaremap.ImageRegister` class.

The icons will be located at `/plugins/FastTravel/icons` and should be strictly **PNG** (.png)

## PlaceholderAPI

I have written a PlaceholderAPI class but its empty, really didn't knew what all should be added.

