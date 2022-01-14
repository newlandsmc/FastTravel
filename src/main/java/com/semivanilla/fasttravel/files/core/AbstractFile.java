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

import java.util.*;

public abstract class AbstractFile {

    protected final FileHandler handler;
    protected FlatFile file;
    protected static final String BASE_IDENTIFIER = "base64:";

    public AbstractFile(FileHandler handler) {
        this.handler = handler;
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
        if(world == null)
            return Optional.empty();

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
            return Optional.of(Waypoint.buildFrom(name, getMaterial(name + ".icon"), file.getStringList(name + ".lore"), location.get(), file.getInt(name+".offset-radius.x"), file.getInt(name+".offset-radius.y"), file.getInt(name+".offset-radius.z")));
        }
        else {
            return Optional.empty();}
    }

    protected ItemStack getMaterial(@NotNull String path){
        final String materialString = file.getString(path);
        if(EnumUtils.isValidEnum(Material.class,materialString))
            return new ItemStack(Objects.requireNonNull(Material.getMaterial(materialString)));
        else {
            if(materialString.startsWith(BASE_IDENTIFIER)){
                final String texture = materialString.substring(BASE_IDENTIFIER.length());
                return ItemUtils.getHead(texture);
            }else {
                handler.getPlugin().getLogger().info("The material at "+path+" does not seems to be valid. Defaulting to Grass Block");
                return new ItemStack(Material.GRASS_BLOCK);
            }
        }
    }

}
