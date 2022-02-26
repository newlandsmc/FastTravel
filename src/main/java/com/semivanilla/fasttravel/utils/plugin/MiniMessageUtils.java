package com.semivanilla.fasttravel.utils.plugin;

import com.semivanilla.fasttravel.utils.UtilityManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MiniMessageUtils {

    private static UtilityManager manager;
    private static BukkitAudiences audiences;
    private static MiniMessage miniMessage;
    private static final String BLANK_SPACE = " ";

    private static String prefix;
    private static boolean hasPrefix;

    public MiniMessageUtils(UtilityManager utilityManager) {
        manager = utilityManager;
        audiences = BukkitAudiences.create(manager.getPlugin());
        miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decoration())
                        .resolver(StandardTags.hoverEvent())
                        .resolver(StandardTags.clickEvent())
                        .resolver(StandardTags.keybind())
                        .resolver(StandardTags.translatable())
                        .resolver(StandardTags.translatable())
                        .resolver(StandardTags.insertion())
                        .resolver(StandardTags.font())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.rainbow())
                        .build())
                .strict(false)
                .build();

        prefix = "";
        hasPrefix = false;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if(StringUtils.isBlank(prefix))
            this.hasPrefix = false;
        else this.hasPrefix = true;
    }

    public static Component transform(String message){
        if(StringUtils.isBlank(message))
            return Component.empty();
        final String legacyConversion = ChatColor.translateAlternateColorCodes('&',message);
        return miniMessage.deserialize(message);
    }

    public static List<Component> transform(@NotNull List<String> messages){
        final List<Component> returnComponent = new ArrayList<Component>();
        if(messages.isEmpty())
            return returnComponent;

        messages.forEach(s -> {
            returnComponent.add(transform(s));
        });
        return returnComponent;
    }

    private static Component formatMessage(String message){
        if(StringUtils.isBlank(message))
            return Component.empty();
        StringBuilder sb = new StringBuilder();
        if(hasPrefix) {
            sb.append(prefix);
            sb.append(BLANK_SPACE);
        }
        sb.append(message);
        return transform(sb.toString());
    }

    private static List<Component> formatMessage(@NotNull List<String> messages){
        final List<Component> returnComponent = new ArrayList<Component>();
        if(messages.isEmpty())
            return returnComponent;

        messages.forEach((m) -> {
            returnComponent.add(formatMessage(m));
        });
        return returnComponent;
    }

    public static void sendMessage(@NotNull Player player,String message){
        if(StringUtils.isBlank(message))
            return;
        if(manager.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            audiences.player(player).sendMessage(formatMessage(PlaceholderAPI.setPlaceholders(player,message)));
        else
            audiences.player(player).sendMessage(formatMessage(message));
    }

    public static void sendSimpleMessage(@NotNull Player player, String message){
        if(manager.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            audiences.player(player).sendMessage(transform(PlaceholderAPI.setPlaceholders(player,message)));
        else audiences.player(player).sendMessage(transform(message));

    }

    public static void sendMessage(@NotNull Player player, List<String> messages){
        messages.forEach((m) -> {
            sendMessage(player,m);
        });
    }

    public static void sendMessage(@NotNull CommandSender sender, String message){
        if(StringUtils.isBlank(message))
            return;
        audiences.sender(sender).sendMessage(formatMessage(message));
    }

    public static void sendMessage(@NotNull CommandSender sender, List<String> message){
        message.forEach(m -> {
            sendMessage(sender, m);
        });
    }

    public static void sendActionBar(@NotNull Player player, String message){
        audiences.player(player).sendActionBar(transform(message));
    }

    public static void sendActionBar(@NotNull List<Player> players, String message){
        players.forEach((p) -> sendActionBar(p,message));
    }

    public static void setBossBar(@NotNull Player player,@NotNull String barString, float percent , BossBar.Color color, BossBar.Overlay style, int duration){
        final BossBar bossBar = BossBar.bossBar(transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                audiences.player(player).hideBossBar(bossBar);
            }
        }, 20L *duration);
    }

    public static BossBar setBossBar(@NotNull Player player,@NotNull String barString,float percent ,BossBar.Color color, BossBar.Overlay style){
        final BossBar bossBar = BossBar.bossBar(transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        return bossBar;
    }

    public static void sendTitle(@NotNull Player player, String header, String footer ){
        final Title title = Title.title(
                transform(header)
                ,transform(footer)
        );

        audiences.player(player).showTitle(title);
    }

    public static void sendTitle(@NotNull Player player, String header, String footer, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                transform(header)
                ,transform(footer)
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public static void sendTitle(@NotNull Player player, String header){
        final Title title = Title.title(
                transform(header),
                Component.empty()
        );

        audiences.player(player).showTitle(title);
    }

    public static void sendTitle(@NotNull Player player, String header, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                transform(header)
                ,Component.empty()
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public static void broadcast(String message){
        if(StringUtils.isBlank(message))
            return;
        audiences.all().sendMessage(transform(message));
    }

}
