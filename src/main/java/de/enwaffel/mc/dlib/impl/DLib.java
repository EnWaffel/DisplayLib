package de.enwaffel.mc.dlib.impl;

import de.enwaffel.mc.dlib.Display;
import de.enwaffel.mc.dlib.DisplayItem;
import de.enwaffel.mc.dlib.DisplayState;
import de.enwaffel.mc.dlib.util.BukkitUtils;
import de.enwaffel.mc.dlib.util.ImplClasses;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class DLib implements Listener {

    public static final HashMap<Display, DisplayImplMethods> DISPLAYS = new HashMap<>();
    public static Plugin PLUGIN = null;

    private static DLib listenerInstance;

    private static final String[] SUPPORTED_VERSIONS = {
            "v1_20_R3"
    };

    public static void attach(Plugin plugin) {
        if (PLUGIN != null) return;

        listenerInstance = new DLib();
        plugin.getServer().getPluginManager().registerEvents(listenerInstance, plugin);

        String version = BukkitUtils.getVersion();
        if (!Arrays.asList(SUPPORTED_VERSIONS).contains(version)) {
            throw new IllegalArgumentException("Unsupported version");
        }

        String pkg = "de.enwaffel.mc.dlib.impl." + version;

        try {
            ImplClasses.DISPLAY_CLASS = (Class<? extends Display>) Class.forName(pkg + ".DisplayImpl");
            ImplClasses.DISPLAY_STATE_CLASS = (Class<? extends DisplayState>) Class.forName(pkg + ".DisplayStateImpl");
            ImplClasses.DISPLAY_ITEM_CLASS = (Class<? extends DisplayItem>) Class.forName(pkg + ".DisplayItemImpl");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        PLUGIN = plugin;
    }

    public static void detach() {
        listenerInstance.active = false;
        PLUGIN = null;
    }

    private boolean active = true;

    DLib() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!active) return;

        for (Map.Entry<Display, DisplayImplMethods> set : DISPLAYS.entrySet()) {
            try {
                set.getValue().invClick.invoke(set.getKey(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!active) return;

        for (Map.Entry<Display, DisplayImplMethods> set : DISPLAYS.entrySet()) {
            try {
                set.getValue().invClose.invoke(set.getKey(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public record DisplayImplMethods(Method invClick, Method invClose) {
    }

}
