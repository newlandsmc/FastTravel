package com.semivanilla.fasttravel.files.core;

import com.semivanilla.fasttravel.model.Waypoint;
import com.semivanilla.fasttravel.utils.plugin.ItemUtils;
import com.semivanilla.fasttravel.utils.plugin.MiniMessageUtils;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.sections.FlatFileSection;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractFile {

    protected final FileHandler handler;
    protected FlatFile file;
    protected static final String BASE_IDENTIFIER = "base64:";

    public AbstractFile(FileHandler handler) {
        this.handler = handler;
        if (!handler.getPlugin().getDataFolder().exists())
            handler.getPlugin().getDataFolder().mkdirs();
    }

    public abstract boolean initConfig();

    public abstract void loadConfig();

    public boolean reloadConfig(){
        if(!initConfig())
            return false;

        loadConfig();
        return true;
    }

    public FlatFileSection getConfigurationSection(@NotNull String path){
        return new FlatFileSection(this.file,path);
    }

    protected Component getMessageComponent(@NotNull String path){
        return MiniMessageUtils.transform(file.getString(path));
    }

    protected List<Component> getMessageComponents(@NotNull String path){
        return MiniMessageUtils.transform(file.getStringList(path));
    }

    protected Optional<Location> deserializeLocation(@NotNull String path){
        World world = handler.getPlugin().getServer().getWorld(file.getString(path+".world-name"));
        if(world == null) {
            handler.getPlugin().getLogger().severe("The world for "+path+" seems to be null. The deserialization of the waypoint has been failed and wont be updated");
            return Optional.empty();
        }
        final int x = file.getInt(path+".x");
        final int y = file.getInt(path+".y");
        final int z = file.getInt(path+".z");
        final float yaw = file.getFloat(path+".yaw");
        final float pitch = file.getFloat(path+".pitch");

        return Optional.of(new Location(world,x,y,z,yaw,pitch));
    }

    protected Optional<Waypoint> deserializeWaypoint(@NotNull String name){
        Optional<Location> location = deserializeLocation(name+".location");
        if(location.isPresent()) {
            return Optional.of(Waypoint.buildFrom(name, getMaterial(name + ".icon"), file.getStringList(name + ".lore"), location.get(), file.getInt(name + ".offset-radius.x"), file.getInt(name + ".offset-radius.y"), file.getInt(name + ".offset-radius.z"), file.getString(name + ".icon-name")));
        } else {
            handler.getPlugin().getLogger().severe("Failed to obtain location for the waypoint. Deserialization failed!");
            return Optional.empty();
        }
    }

    protected ItemStack getMaterial(@NotNull String path){
        final String materialString = file.getString(path);
        if(EnumUtils.isValidEnum(Material.class,materialString))
            return new ItemStack(Objects.requireNonNull(Material.getMaterial(materialString)));
        else {
            if (materialString.startsWith(BASE_IDENTIFIER)) {
                final String texture = materialString.substring(BASE_IDENTIFIER.length());
                return ItemUtils.getHead(texture);
            } else {
                handler.getPlugin().getLogger().info("The material at " + path + " does not seems to be valid. Defaulting to Grass Block");
                return new ItemStack(Material.GRASS_BLOCK);
            }
        }
    }

    public InputStream getResourceAsInputStream(@NotNull String name) {
        return this.handler.getPlugin().getResource(name);
    }

    public boolean saveResourceTo(@NotNull InputStream resourceStream, @NotNull String folderName, @NotNull String fileName) {
        final File file = new File(handler.getPlugin().getDataFolder() + File.separator + folderName, fileName);
        if (!file.exists()) {
            try (OutputStream outputStream = new FileOutputStream(file, false)) {
                resourceStream.transferTo(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

}

