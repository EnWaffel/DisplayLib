package de.enwaffel.mc.dlib.util;

import org.bukkit.Bukkit;

public final class BukkitUtils {

    private static final String VERSION;

    static {
        VERSION = Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[ 3 ];
    }

    public static String getVersion() {
        return VERSION;
    }

}
